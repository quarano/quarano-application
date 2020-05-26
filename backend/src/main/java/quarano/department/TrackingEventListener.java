package quarano.department;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.diary.DiaryManagement;
import quarano.tracking.ContactPerson;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.EncounterReported;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;
import quarano.tracking.TrackedPersonRepository;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 * @author Patrick Otto
 * @author Jens Kutzsche
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TrackingEventListener {

	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull TrackedPersonRepository trackedPeople;
	private final @NonNull DiaryManagement diaries;

	@EventListener
	void on(EncounterReported event) {

		verifyEnrollmentCompleted(event);
		handleContactCaseCreationBasedOnEncounter(event);
	}

	private void verifyEnrollmentCompleted(EncounterReported event) {

		var trackedCase = findTrackedCaseFor(event.getPersonIdentifier());
		var enrollment = trackedCase.getEnrollment();

		if (!enrollment.isCompletedQuestionnaire()) {
			throw new EnrollmentException("Cannot add contacts prior to completing the enrollment questionnaire!");
		}
	}

	/**
	 * Listens for first encounters with contacts and creates new cases from these contacts if they came from an Index
	 * person
	 */
	private void handleContactCaseCreationBasedOnEncounter(EncounterReported event) {

		ContactPerson contactPerson = event.getEncounter().getContact();

		try {

			if (event.isFirstEncounterWithTargetPerson()) {
				createContactCase(event.getPersonIdentifier(), contactPerson);
			}

		} catch (Exception e) {

			// just log the error, do not stop usual saving process of the encounter
			log.error("Error during automatic contact-case creation check for contact " + contactPerson.getId(), e);
		}
	}

	private void createContactCase(TrackedPersonIdentifier trackedPersonId, ContactPerson contactPerson) {

		if (cases.existsByOriginContacts(contactPerson)) {
			return;
		}

		var caseOfContactInitializer = findTrackedCaseFor(trackedPersonId);

		// Only contacts of index-cases shall be converted to new cases automatically

		if (!caseOfContactInitializer.isIndexCase()) {
			return;
		}

		var person = new TrackedPerson(contactPerson);
		var caseType = CaseType.CONTACT;

		// CORE-185 medical staff overwrites the others
		if (contactPerson.getIsHealthStaff() == Boolean.TRUE) {
			caseType = CaseType.CONTACT_MEDICAL;
		} else if (contactPerson.isVulnerable()) {
			caseType = CaseType.CONTACT_VULNERABLE;
		}

		cases.save(new TrackedCase(person, caseType, caseOfContactInitializer.getDepartment(), contactPerson));

		log.info("Created automatic contact case from contact " + contactPerson.getId());
	}

	private TrackedCase findTrackedCaseFor(TrackedPersonIdentifier identifier) {

		return cases.findByTrackedPerson(identifier) //
				.orElseThrow(() -> new IllegalStateException("No tracked case found for tracked person " + identifier + "!"));
	}
}
