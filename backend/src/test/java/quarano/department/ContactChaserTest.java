package quarano.department;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.account.Department;
import quarano.core.PhoneNumber;
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

import org.assertj.core.data.Index;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@QuaranoUnitTest
class ContactChaserTest {

	@Mock TrackedCaseRepository cases;

	@InjectMocks ContactChaser contactChaser;

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
		var contactCase = contactCase(CaseType.CONTACT, indexPersonId);

		var indexCase = indexCase(contactCase.getOriginContacts().get(0));
		when(cases.findByTrackedPerson(indexPersonId)).thenReturn(Optional.of(indexCase));

		var contacts = contactChaser.findIndexContactsFor(contactCase);

		assertThat(contacts).hasSize(1).satisfies(contact -> {

			assertThat(contact.getCaseId()).isEqualTo(indexCase.getId());
			assertThat(contact.getContactAt()).isEqualTo(LocalDate.now());
			assertThat(contact.getPerson()).isEqualTo(indexCase.getTrackedPerson());
			assertThat(contact.getContactPerson()).isEqualTo(contactCase.getOriginContacts().get(0));

		}, Index.atIndex(0));
	}

	@Test
	void testFindIndexContactsForMedicalContactCase() {

		var indexPersonId = TrackedPersonIdentifier.of(UUID.randomUUID());
		var contactCase = contactCase(CaseType.CONTACT_MEDICAL, indexPersonId);

		var indexCase = indexCase(contactCase.getOriginContacts().get(0));
		when(cases.findByTrackedPerson(indexPersonId)).thenReturn(Optional.of(indexCase));

		var contacts = contactChaser.findIndexContactsFor(contactCase);

		assertThat(contacts).hasSize(1).satisfies(contact -> {

			assertThat(contact.getCaseId()).isEqualTo(indexCase.getId());
			assertThat(contact.getContactAt()).isEqualTo(LocalDate.now());
			assertThat(contact.getPerson()).isEqualTo(indexCase.getTrackedPerson());
			assertThat(contact.getContactPerson()).isEqualTo(contactCase.getOriginContacts().get(0));

		}, Index.atIndex(0));
	}

	@Test
	void testFindIndexContactsForContactCaseWithNoIndexCaseFound() {

		var indexPersonId = TrackedPersonIdentifier.of(UUID.randomUUID());
		var contactCase = contactCase(CaseType.CONTACT_MEDICAL, indexPersonId);

		when(cases.findByTrackedPerson(indexPersonId)).thenReturn(Optional.empty());

		assertThat(contactChaser.findIndexContactsFor(contactCase)).isEmpty();
	}

	private static TrackedCase indexCase() {
		return new TrackedCase(new TrackedPerson("firstName", "lastName"), CaseType.INDEX, new Department("test"));
	}

	private static TrackedCase indexCase(ContactPerson contactPerson) {

		var person = spy(new TrackedPerson("indexFirstName", "indexLastName"));
		when(person.getEncounters()).thenReturn(Encounters.of(List.of(Encounter.with(contactPerson, LocalDate.now()))));

		return new TrackedCase(person, CaseType.INDEX, new Department("test"));
	}

	private static TrackedCase contactCase(CaseType caseType, TrackedPersonIdentifier indexPersonId) {

		return new TrackedCase(new TrackedPerson("firstName", "lastName"), caseType, new Department("test"))
				.setOriginContacts(List.of(new ContactPerson("firstName", "lastName",
						ContactWays.builder().phoneNumber(PhoneNumber.of("013291")).build()).setOwnerId(indexPersonId)));
	}
}
