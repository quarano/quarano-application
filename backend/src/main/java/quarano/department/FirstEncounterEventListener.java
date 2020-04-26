package quarano.department;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactPerson.ContactPersonAdded;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.EncounterReported;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import javax.persistence.EntityNotFoundException;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FirstEncounterEventListener {

	private final TrackedCaseRepository cases;

	/**
	 * Listens for first encounters with contacts and creates new cases from these contacts if they came from an Index
	 * person
	 * 
	 * @param event
	 */
	@EventListener
	void on(EncounterReported event) {

		ContactPerson contactPerson = event.getEncounter().getContact();

		try {

			if (event.isFirstEncounterWithTargetPerson()) {
				TrackedPersonIdentifier triggeringPerson = event.getPersonIdentifier();
				TrackedCase caseOfContactInitializer = cases.findByTrackedPerson(triggeringPerson)
						.orElseThrow(() -> new EntityNotFoundException());

				// only contacts of index-cases shall be converted to new cases automatically
				if (caseOfContactInitializer.getType() == CaseType.INDEX) {

					TrackedPerson person = new TrackedPerson(contactPerson);
					TrackedCase contactCase = new TrackedCase(person, CaseType.CONTACT, caseOfContactInitializer.getDepartment());

					cases.save(contactCase);

					log.info("Created automatic contact-case from contact " + contactPerson.getId());
				}
			}

		} catch (Exception e) {
			// just log the error, do not stop usual saving process of the encounter
			log.error("Error during automatical contact-case creation check for contact " + contactPerson.getId(), e);
		}

	}

}
