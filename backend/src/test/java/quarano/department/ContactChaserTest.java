package quarano.department;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.account.Department;
import quarano.core.PhoneNumber;
import quarano.department.ContactChaser.Contact;
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactWays;
import quarano.tracking.Encounter;
import quarano.tracking.Encounters;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@QuaranoUnitTest
class ContactChaserTest {
	@Mock
	private TrackedCaseRepository cases;

	private ContactChaser contactChaser;

	@BeforeEach
	void setUp() {
		contactChaser = new ContactChaser(cases);
	}

	@Test
	void testFindIndexContactsForIndexCase() {
		assertThat(contactChaser.findIndexContactsFor(indexCase())).isEmpty();
	}

	@Test
	void testFindIndexContactsForContactCase() {
		var indexPersonId = TrackedPersonIdentifier.of(UUID.randomUUID());
		TrackedCase contactCase = contactCase(CaseType.CONTACT, indexPersonId);

		TrackedCase indexCase = indexCase(contactCase.getOriginContacts().get(0));
		when(cases.findByTrackedPerson(indexPersonId)).thenReturn(Optional.of(indexCase));

		List<Contact> contacts = contactChaser.findIndexContactsFor(contactCase).get().collect(Collectors.toList());
		assertThat(contacts).hasSize(1);
		assertThat(contacts.get(0).getCaseId()).isEqualTo(indexCase.getId());
		assertThat(contacts.get(0).getContactAt()).isEqualTo(LocalDate.now());
		assertThat(contacts.get(0).getPerson()).isEqualTo(indexCase.getTrackedPerson());
		assertThat(contacts.get(0).getContactPerson()).isEqualTo(contactCase.getOriginContacts().get(0));
	}

	@Test
	void testFindIndexContactsForMedicalContactCase() {
		var indexPersonId = TrackedPersonIdentifier.of(UUID.randomUUID());
		TrackedCase contactCase = contactCase(CaseType.CONTACT_MEDICAL, indexPersonId);

		TrackedCase indexCase = indexCase(contactCase.getOriginContacts().get(0));
		when(cases.findByTrackedPerson(indexPersonId)).thenReturn(Optional.of(indexCase));

		List<Contact> contacts = contactChaser.findIndexContactsFor(contactCase).get().collect(Collectors.toList());
		assertThat(contacts).hasSize(1);
		assertThat(contacts.get(0).getCaseId()).isEqualTo(indexCase.getId());
		assertThat(contacts.get(0).getContactAt()).isEqualTo(LocalDate.now());
		assertThat(contacts.get(0).getPerson()).isEqualTo(indexCase.getTrackedPerson());
		assertThat(contacts.get(0).getContactPerson()).isEqualTo(contactCase.getOriginContacts().get(0));
	}

	@Test
	void testFindIndexContactsForContactCaseWithNoIndexCaseFound() {
		var indexPersonId = TrackedPersonIdentifier.of(UUID.randomUUID());
		TrackedCase contactCase = contactCase(CaseType.CONTACT_MEDICAL, indexPersonId);

		when(cases.findByTrackedPerson(indexPersonId)).thenReturn(Optional.empty());

		List<Contact> contacts = contactChaser.findIndexContactsFor(contactCase).get().collect(Collectors.toList());
		assertThat(contacts).hasSize(0);
	}

	private TrackedCase indexCase() {
		return new TrackedCase(new TrackedPerson("firstName", "lastName"), CaseType.INDEX, new Department("test"));
	}

	private TrackedCase indexCase(ContactPerson contactPerson) {
		TrackedPerson trackedPerson = spy(new TrackedPerson("indexFirstName", "indexLastName"));
		when(trackedPerson.getEncounters()).thenReturn(Encounters.of(List.of(
			Encounter.with(contactPerson, LocalDate.now())
		)));
		return new TrackedCase(
				trackedPerson, CaseType.INDEX, new Department("test")
		);
	}

	private TrackedCase contactCase(CaseType caseType, TrackedPersonIdentifier indexPersonId) {

		return new TrackedCase(new TrackedPerson("firstName", "lastName"), caseType, new Department("test"))
				.setOriginContacts(List.of(
						new ContactPerson("firstName", "lastName", ContactWays.builder().phoneNumber(PhoneNumber.of("013291")).build())
						.setOwnerId(indexPersonId)
				));
	}
}
