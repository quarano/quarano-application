package quarano.department;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactPerson.ContactPersonAdded;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import javax.persistence.EntityNotFoundException;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContactPersonCreationEventListener {

	private final TrackedCaseRepository cases;

	/**
	 * Listens for newly created contact persons and creates new cases from these contacts if they came from an Index
	 * person
	 * 
	 * @param event
	 */
	@EventListener
	void on(ContactPersonAdded event) {

		try {
			TrackedPersonIdentifier ownerId = event.getContactPerson().getOwnerId();
			TrackedCase caseOfContactInitializer = cases.findByTrackedPerson(ownerId)
					.orElseThrow(() -> new EntityNotFoundException());

			// only contacts of index-cases shall be converted to new cases automatically
			if (caseOfContactInitializer.getType() == CaseType.INDEX) {

				TrackedPerson person = createTrackedPersonFormContact(event.getContactPerson());
				TrackedCase contactCase = new TrackedCase(person, CaseType.CONTACT, caseOfContactInitializer.getDepartment());

				cases.save(contactCase);

				log.info("Created automatic contact-case from contact " + event.getContactPerson().getId());
			}

		} catch (Exception e) {
			log.error("Error during automatical contact-case creation check for contact " + event.getContactPerson().getId(),
					e);
		}

	}

	private TrackedPerson createTrackedPersonFormContact(ContactPerson contact) {
		TrackedPerson person = new TrackedPerson(contact.getFirstName(), contact.getLastName());

		person.setEmailAddress(contact.getEmailAddress());
		person.setMobilePhoneNumber(contact.getMobilePhoneNumber());
		person.setPhoneNumber(contact.getPhoneNumber());

		return person;
	}
}
