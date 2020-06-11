package quarano.department;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.Department;
import quarano.department.TrackedCase.CaseConvertedToIndex;
import quarano.tracking.ContactPerson;
import quarano.tracking.Encounter;

/**
 * @author Jens Kutzsche
 */
@Slf4j
@Component("department.TrackedCaseEventListener")
@RequiredArgsConstructor
class TrackedCaseEventListener {

	private final @NonNull TrackedCaseRepository cases;

	@EventListener
	void on(CaseConvertedToIndex event) {

		var trackedCase = event.getTrackedCase();
		var department = trackedCase.getDepartment();

		// Only contacts of index-cases shall be converted to new cases automatically
		if (!trackedCase.isIndexCase()) {
			return;
		}

		trackedCase.getTrackedPerson().getEncounters()//
				.map(Encounter::getContact)//
				.forEach(it -> createContactCase(department, it));
	}

	private void createContactCase(Department department, ContactPerson contactPerson) {

		if (cases.existsByOriginContacts(contactPerson)) {
			return;
		}

		cases.save(TrackedCase.of(contactPerson, department));

		log.info("Created automatic contact case from contact " + contactPerson.getId());
	}
}
