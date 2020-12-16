package quarano.sormas_integration;

import de.quarano.sormas.client.api.CaseControllerApi;
import de.quarano.sormas.client.api.PersonControllerApi;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.DepartmentRepository;
import quarano.core.SormasIdentifier;
import quarano.core.web.MapperWrapper;
import quarano.department.CaseType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.SormasCaseIdentifier;
import quarano.department.TrackedCaseRepository;
import quarano.sormas_integration.SyncTimes.DataTypes;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;
import quarano.tracking.web.TrackedPersonDto;

import java.time.Instant;
import java.util.stream.Collectors;

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
class SyncToSormasJob {

	private final @NonNull SyncTimesRepository syncTimes;
	private final @NonNull ChangesForSyncRepository changes;
	private final @NonNull TrackedPersonRepository persons;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull DepartmentRepository departments;
	private final @NonNull CaseControllerApi sormasCaseAPI;
	private final @NonNull PersonControllerApi sormasPersonAPI;
	private final @NonNull MapperWrapper mapper;

	@Scheduled(fixedDelay = 15000)
	void sync() {

		var syncTime = Instant.now().toEpochMilli();

		var lastSyncTime = syncTimes.findById(DataTypes.Cases).map(SyncTimes::getLastSync).orElse(0l);
		var sormasCases = sormasCaseAPI.getAllCases(lastSyncTime);

		lastSyncTime = syncTimes.findById(DataTypes.Persons).map(SyncTimes::getLastSync).orElse(0l);
		var sormasPersons = sormasPersonAPI.getAllPersons(lastSyncTime);

		var trackedPersons = sormasPersons.stream().map(it -> {

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
		})
				.collect(Collectors.toList());

		persons.saveAll(trackedPersons);

		syncTimes.save(SyncTimes.of(DataTypes.Persons, syncTime));

		var trackedCases = sormasCases.stream().map(it -> {

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
		}).collect(Collectors.toList());

		cases.saveAll(trackedCases);

		syncTimes.save(SyncTimes.of(DataTypes.Cases, syncTime));

		// var groupedChanges = changes.findAll().stream().collect(Collectors.groupingBy(it -> it.getRefId()));
		//
		// groupedChanges.entrySet().forEach(it -> {
		//
		// var caseOpt = cases.findById(TrackedCaseIdentifier.of(it.getKey()));
		//
		// caseOpt.ifPresent(refCase -> {
		//
		// var sormasCases = refCase.getSormasCaseId() != null
		// ? sormasCaseAPI.getByUuids6(List.of(refCase.getSormasCaseId().toString()))
		// : List.<CaseDataDto> of();
		//
		// var sormasCase = sormasCases.isEmpty()
		// ? new CaseDataDto()
		// : sormasCases.get(0);
		//
		// sormasCase.setExternalID(refCase.getId().toString());
		//
		// var ret = sormasCaseAPI.postCases(List.of(sormasCase));
		//
		// System.out.println(ret);
		// });

		// var person = persons.findById(TrackedPersonIdentifier.of(it.getKey()));

		// sormasPersonAPI.getByUuids18(List.of(person.))

		// sormasPersonAPI.postPersons(null);

		// changes.deleteAll(it.getValue());
		// });
	}
}
