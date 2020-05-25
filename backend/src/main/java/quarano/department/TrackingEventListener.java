package quarano.department;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.event.EventListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.tracking.ContactPerson;
import quarano.tracking.DiaryEntry;
import quarano.tracking.DiaryEntry.DiaryEntryAdded;
import quarano.tracking.DiaryEntry.DiaryEntryUpdated;
import quarano.tracking.DiaryManagement;
import quarano.tracking.Encounter;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.EncounterReported;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;
import quarano.tracking.TrackedPersonRepository;

/**
 * @author Oliver Drotbohm
 * @author Patrick Otto
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TrackingEventListener {

	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull TrackedPersonRepository trackedPeople;
	private final @NonNull DiaryManagement diaryEntries;

	@EventListener
	void on(EncounterReported event) {

		verifyEnrollmentCompleted(event);
		handleContactCaseCreationBasedOnEncounter(event);
	}

	/**
	 * If a DiaryEntry is added it might contain new contacts to new ContactPerson, which need to be turned into
	 * contact-cases
	 *
	 * @param event Event
	 */
	@EventListener
	void on(DiaryEntryAdded event) {
		handleContactCaseCreationBasedOnDiaryEntry(event.getEntry());
	}
	
	/**
	 * If a DiaryEntry is updated it might contain new contacts to new ContactPerson, which need to be
	 * turned into contact-cases
	 *
	 * @param event Event
	 */
	@EventListener
	void on(DiaryEntryUpdated event) {
		handleContactCaseCreationBasedOnDiaryEntry(event.getEntry());
	}

	private void verifyEnrollmentCompleted(EncounterReported event) {

		var trackedCase = findTrackedCaseFor(event.getPersonIdentifier());
		var enrollment = trackedCase.getEnrollment();

		if (!enrollment.isCompletedQuestionnaire()) {
			throw new EnrollmentException("Cannot add contacts prior to completing the enrollment questionnaire!");
		}
	}

	private void handleContactCaseCreationBasedOnDiaryEntry(DiaryEntry entry) {
		try {
			List<ContactPerson> contactPersons = entry.getContacts();

			for (ContactPerson person : contactPersons) {
				if (isFirstContactWith(person, entry, null)) {
					createContactCase(entry.getTrackedPersonId(), person);
				}
			}
		} catch (Exception e) {

			// just log the error, do not stop usual saving process of the diary entry
			log.error("Error during automatical contact-case creation check for diary entry " + entry.getId(), e);
		}
	}

	/**
	 * Checks if there has been a contact to target person before, either as encounter given during enrolement or by
	 * adding it later or via diary-entry
	 */
	private boolean isFirstContactWith(ContactPerson newContactPerson, @Nullable DiaryEntry entry,
			@Nullable Encounter encounter) {
		if (!cases.findByOriginContacts(newContactPerson).isEmpty()) {
			return false;
		}
		
		var trackedPersonId = newContactPerson.getOwnerId();

		// get all contact person of diary entries
		Stream<DiaryEntry> entries = diaryEntries.findDiaryFor(trackedPersonId).stream();

		List<DiaryEntry> entriesAsList = entries.collect(Collectors.toList());

		// ignore current diary entry if this contact came from a diary, because it will always
		// contain current contact
		if (entry != null) {
			entriesAsList.remove(entry);
		}

		List<ContactPerson> allContactPersonOfTrackedPerson = entriesAsList.stream()
				.flatMap(it -> it.getContacts().stream()) //
				.collect(Collectors.toList());

		// get all contact person of encounters
		var contactOwner = trackedPeople.findById(trackedPersonId);

		// ignore current encounter if this contact came from an encounter
		if (encounter == null) {
			allContactPersonOfTrackedPerson.addAll( //
					contactOwner.get().getEncounters().map(Encounter::getContact) //
							.toList());
		} else {
			allContactPersonOfTrackedPerson.addAll( //
					contactOwner.get().getEncounters() //
							.filter(it -> !it.getId().equals(encounter.getId())) //
							.map(Encounter::getContact) //
							.toList());
		}

		return !allContactPersonOfTrackedPerson.contains(newContactPerson);
	}

	/**
	 * Listens for first encounters with contacts and creates new cases from these contacts if they came from an Index
	 * person
	 */
	private void handleContactCaseCreationBasedOnEncounter(EncounterReported event) {

		ContactPerson contactPerson = event.getEncounter().getContact();

		try {

			if (this.isFirstContactWith(contactPerson, null, event.getEncounter())
					&& event.isFirstEncounterWithTargetPerson()) {

				createContactCase(event.getPersonIdentifier(), contactPerson);
			}

		} catch (Exception e) {

			// just log the error, do not stop usual saving process of the encounter
			log.error("Error during automatical contact-case creation check for contact " + contactPerson.getId(), e);
		}
	}

	private void createContactCase(TrackedPersonIdentifier trackedPersonId, ContactPerson contactPerson) {
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
