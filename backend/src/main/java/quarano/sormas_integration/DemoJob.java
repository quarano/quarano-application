package quarano.sormas_integration;

import de.quarano.sormas.client.api.CaseControllerApi;
import de.quarano.sormas.client.api.PersonControllerApi;
import de.quarano.sormas.client.api.TaskControllerApi;
import de.quarano.sormas.client.model.CaseDataDto;
import de.quarano.sormas.client.model.PersonDto;
import de.quarano.sormas.client.model.TaskDto;
import de.quarano.sormas.client.model.TaskType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.DepartmentRepository;
import quarano.core.SormasIdentifier;
import quarano.core.web.MapperWrapper;
import quarano.department.CaseType;
import quarano.department.RegistrationManagement;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.SormasCaseIdentifier;
import quarano.department.TrackedCaseRepository;
import quarano.sormas_integration.SyncTimes.DataTypes;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;
import quarano.tracking.web.TrackedPersonDto;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This class collects all old data and anonymizes the personal data to be GDPR compliant.
 *
 * @author Jens Kutzsche
 * @since 1.4
 */
@Component
@Slf4j
@RequiredArgsConstructor
class DemoJob {

	private final @NonNull SyncTimesRepository syncTimes;
	private final @NonNull ChangesForSyncRepository changes;
	private final @NonNull TrackedPersonRepository persons;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull DepartmentRepository departments;
	private final @NonNull CaseControllerApi sormasCaseApi;
	private final @NonNull PersonControllerApi sormasPersonApi;
	private final @NonNull TaskControllerApi taskControllerApi;
	private final @NonNull RegistrationManagement registration;
	private final @NonNull MapperWrapper mapper;

	@Scheduled(fixedDelay = 15000)
	@Transactional
	void run() {

		var now = Instant.now().toEpochMilli();
		var lastTime = syncTimes.findById(DataTypes.Tasks).map(SyncTimes::getLastSync).orElse(0l);

		var tasks = taskControllerApi.getAll7(lastTime);

		for (TaskDto task : tasks) {
			if (isInTracking(task)) {

				var caze = sormasCaseApi.getByUuids6(List.of(task.getCaze().getUuid()));

				if (!caze.isEmpty()) {

					var person = sormasPersonApi.getByUuids18(List.of(caze.get(0).getPerson().getUuid()));

					if (!person.isEmpty()) {

						if (StringUtils.isBlank(person.get(0).getEmailAddress())) {

							task.setCreatorComment("E-Mail fehlt!");
							task.setChangeDate(OffsetDateTime.now());
							taskControllerApi.postTasks(List.of(task));
						} else {
							savePerson(person.get(0));
							var c = saveCase(caze.get(0));

							var department = departments.findAll().iterator().next();

							if (c == null || !c.belongsTo(department.getId())) {
								continue;
							}

							registration.initiateRegistration(c);
						}
					}
				}
			}
		}

		syncTimes.save(SyncTimes.of(DataTypes.Tasks, now));
	}

	private TrackedPerson savePerson(PersonDto it) {
		var personOpt = persons.findBySormasId(SormasIdentifier.of(it.getUuid()));

		var dto = mapper.map(it, TrackedPersonDto.class);

		TrackedPerson person;
		if (personOpt.isPresent()) {
			person = mapper.map(dto, personOpt.get());
		} else {
			person = mapper.map(dto, TrackedPerson.class);
		}

		person.setSormasId(SormasIdentifier.of(it.getUuid()));

		return person;
	}

	private TrackedCase saveCase(CaseDataDto it) {
		var caseOpt = cases.findBySormasCaseId(SormasCaseIdentifier.of(it.getUuid()));

		TrackedCase caze = caseOpt.orElseGet(() -> {

			var department = departments.findAll().iterator().next();
			var person = persons.findBySormasId(SormasIdentifier.of(it.getPerson().getUuid()));

			persons.delete(person.get());

			return new TrackedCase(person.get(), CaseType.INDEX, department);
		});

		caze.setSormasCaseId(SormasCaseIdentifier.of(it.getUuid()));

		// return mapper.map(it, caze);
		return caze;
	}

	private boolean isInTracking(TaskDto it) {
		return it.getTaskType() == TaskType.CONTACT_TRACING;
	}
}
