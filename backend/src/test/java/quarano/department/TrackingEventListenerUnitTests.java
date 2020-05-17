package quarano.department;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.account.Department;
import quarano.core.EmailAddress;
import quarano.tracking.*;
import quarano.tracking.DiaryEntry.DiaryEntryAdded;
import quarano.tracking.TrackedPerson.EncounterReported;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 * @author Patrick Otto
 */
@QuaranoUnitTest
class TrackingEventListenerUnitTests {

	@Mock TrackedCaseRepository cases;
	@Mock TrackedPersonRepository people;
	@Mock DiaryEntryRepository diaryEntries;
	@Mock Diary diary;
	
	private TrackingEventListener listener;
	
	@BeforeEach
	void setup() {
		 listener = new TrackingEventListener(cases, people, diaryEntries);
	}

	@Test
	void rejectsEncounterReportIfEnrollmentQuestionnaireIncomplete() {

		var person = TrackedPersonDataInitializer.createTanja();
		var trackedCase = new TrackedCase(person, CaseType.INDEX, new Department("Mannheim", UUID.randomUUID())) //
				.submitEnrollmentDetails();
		var contactPerson = new ContactPerson("Michaela", "Mustermann",
				ContactWays.ofEmailAddress("michaela@mustermann.de"));
		contactPerson.setOwnerId(person.getId());
		var event = EncounterReported.firstEncounter(Encounter.with(contactPerson, LocalDate.now()), person.getId());
		
		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));
		when(diaryEntries.findByTrackedPersonId(person.getId())).thenReturn(diary);
		when(people.findById(person.getId())).thenReturn(Optional.of(person));		

		assertThatExceptionOfType(EnrollmentException.class).isThrownBy(() -> {
			listener.on(event);
		});

		trackedCase.submitEnrollmentDetails() //
				.submitQuestionnaire(new MinimalQuestionnaire());

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();
	}

	@Test
	void createContactCaseAutomaticallyOnFirstEncounter() {

		var person = TrackedPersonDataInitializer.createTanja();
		var encounter = createFirstEncounterWithMichaelaFor(person);
		var trackedCase = createIndexCaseFor(person);

		var event = EncounterReported.firstEncounter(encounter, person.getId());

		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));
		when(diaryEntries.findByTrackedPersonId(person.getId())).thenReturn(diary);
		when(people.findById(person.getId())).thenReturn(Optional.of(person));

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		ArgumentCaptor<TrackedCase> argumentCaptor = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor.capture());

		TrackedCase capturedArgument = argumentCaptor.getValue();

		assertIdentityOfMichaela(capturedArgument);
		
		assertThat(capturedArgument.isContactCase()).isTrue();
	}
	
	@Test
	void createNoContactCaseAutomaticallyForSubsequentEncounter() {

		var person = TrackedPersonDataInitializer.createTanja();
		var encounter1 = createFirstEncounterWithMichaelaFor(person);
		
		var trackedCase = createIndexCaseFor(person);
		var event = EncounterReported.firstEncounter(encounter1, person.getId());


		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));
		when(diaryEntries.findByTrackedPersonId(person.getId())).thenReturn(diary);
		when(people.findById(person.getId())).thenReturn(Optional.of(person));

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();
		ArgumentCaptor<TrackedCase> argumentCaptor = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor.capture());
		
		var encounter2 = person.reportContactWith(encounter1.getContact(), LocalDate.now().minusDays(1));
		var event2 = EncounterReported.subsequentEncounter(encounter2, person.getId());
		assertThatCode(() -> listener.on(event2)).doesNotThrowAnyException();
		// check that number of save calls has not increased
		verify(cases, times(1)).save(argumentCaptor.capture());

	}
	
	@Test
	void createNoContactCaseAutomaticallyForDiaryEntry() {

		// Sandra is an Index case
		var sandra = TrackedPersonDataInitializer.createSandra();
		var trackedCase = createIndexCaseFor(sandra);
		when(cases.findByTrackedPerson(sandra.getId())).thenReturn(Optional.of(trackedCase));
		
		ContactPerson contact1FromDiary = new ContactPerson("Hans", "Meier", ContactWays.ofIdentificationHint("test"));
		contact1FromDiary.setOwnerId(sandra.getId());
		ContactPerson contact1bFromDiary = new ContactPerson("Hans2", "Meier2", ContactWays.ofIdentificationHint("test"));
		contact1bFromDiary.setOwnerId(sandra.getId());
		ContactPerson contact1cFromDiary = new ContactPerson("Hans3", "Meier3", ContactWays.ofIdentificationHint("test"));
		contact1cFromDiary.setOwnerId(sandra.getId());
		ContactPerson contact1dFromDiary = new ContactPerson("Hans4", "Meier4", ContactWays.ofIdentificationHint("test"));
		contact1dFromDiary.setOwnerId(sandra.getId());
		ContactPerson contact2FromEncounters= new ContactPerson("Lisa", "Lustig", ContactWays.ofIdentificationHint("test"));
		contact2FromEncounters.setOwnerId(sandra.getId());		
		ContactPerson contact3FromEncounters= new ContactPerson("Bert", "König", ContactWays.ofIdentificationHint("test"));
		contact3FromEncounters.setOwnerId(sandra.getId());				
		ContactPerson contact4FromEncounters= new ContactPerson("Christian", "Schneider", ContactWays.ofIdentificationHint("test"));
		contact4FromEncounters.setOwnerId(sandra.getId());			
		
		var entry1OfSandraWithOneContact = DiaryEntry.of(Slot.eveningOf(LocalDate.now().minusDays(2)), sandra) //
				.setBodyTemperature(BodyTemperature.of(37.5F));
		entry1OfSandraWithOneContact.setContacts(List.of(contact1FromDiary));
		
		var entry1bOfSandraWithTwoContacts = DiaryEntry.of(Slot.eveningOf(LocalDate.now().minusDays(2)), sandra) //
				.setBodyTemperature(BodyTemperature.of(37.5F));
		entry1bOfSandraWithTwoContacts.setContacts(List.of(contact1bFromDiary, contact1cFromDiary));
		
		var entry2OfSandraWithNoContact = DiaryEntry.of(Slot.morningOf(LocalDate.now().minusDays(2)), sandra) //
				.setBodyTemperature(BodyTemperature.of(36.5F));
		
		var newEntryOfSandraWithContactFromEncounter = DiaryEntry.of(Slot.morningOf(LocalDate.now().minusDays(1)), sandra) //
				.setBodyTemperature(BodyTemperature.of(36.5F));
		newEntryOfSandraWithContactFromEncounter.setContacts(List.of(contact2FromEncounters));
		
		var newEntryOfSandraWithNewContact = DiaryEntry.of(Slot.morningOf(LocalDate.now()), sandra) //
				.setBodyTemperature(BodyTemperature.of(36.5F));
		newEntryOfSandraWithNewContact.setContacts(List.of(contact1dFromDiary));
		
		Streamable<DiaryEntry> entriesOfSandra = new  Streamable<DiaryEntry>() {
			@Override
			public Iterator<DiaryEntry> iterator() {
				return List.of(entry1OfSandraWithOneContact, entry1bOfSandraWithTwoContacts).iterator();
			}
		}; 
		
		when(diaryEntries.findByTrackedPersonId(sandra.getId())).thenReturn(Diary.of(entriesOfSandra));
		when(people.findById(sandra.getId())).thenReturn(Optional.of(sandra));
		
		Encounter encounter1InThePastWithNewContact = sandra.reportContactWith(contact2FromEncounters, LocalDate.now().minusDays(3));		
//		Encounter subsequentEncounterInThePast = sandra.reportContactWith(contact2FromEncounters, LocalDate.now().minusDays(2));		
//		Encounter subsequentEncounterNow = sandra.reportContactWith(contact2FromEncounters, LocalDate.now());	
//		Encounter newEncounterNow = sandra.reportContactWith(contact4FromEncounters, LocalDate.now());		

		
		// it is the first encounter with a new person => should create a contact case
		var eventFirstEncounter = EncounterReported.firstEncounter(encounter1InThePastWithNewContact, sandra.getId());
		assertThatCode(() -> listener.on(eventFirstEncounter)).doesNotThrowAnyException();
		ArgumentCaptor<TrackedCase> argumentCaptor1 = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor1.capture());
				
		// it is the first encounter but with a person already listed in the diary => should NOT create a contact case
		Encounter encounterWithDiaryPerson = sandra.reportContactWith(contact1FromDiary, LocalDate.now());
		var eventFirstEncounterWithExistingDiaryContact = EncounterReported.firstEncounter(encounterWithDiaryPerson, sandra.getId());
		assertThatCode(() -> listener.on(eventFirstEncounterWithExistingDiaryContact)).doesNotThrowAnyException();
		ArgumentCaptor<TrackedCase> argumentCaptor2 = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor2.capture());
		
		// a diary entry with no contact can never create contact-cases => should NOT create a contact case
		var eventDiaryEntryWithNoContact = DiaryEntryAdded.of(entry2OfSandraWithNoContact);
		assertThatCode(() -> listener.on(eventDiaryEntryWithNoContact)).doesNotThrowAnyException();
		ArgumentCaptor<TrackedCase> argumentCaptor3 = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor3.capture());
		
		// a diary entry with a contact which was already listed as encounter => should NOT create a contact case
		var eventDiaryEntryWithExistingContactFromEncounter = DiaryEntryAdded.of(newEntryOfSandraWithContactFromEncounter);
		assertThatCode(() -> listener.on(eventDiaryEntryWithExistingContactFromEncounter)).doesNotThrowAnyException();
		ArgumentCaptor<TrackedCase> argumentCaptor4 = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor4.capture());
		
	// a diary entry with a contact which was already listed as encounter => should create an contact-case
		var eventDiaryEntryWithNewContact = DiaryEntryAdded.of(newEntryOfSandraWithNewContact);
		assertThatCode(() -> listener.on(eventDiaryEntryWithNewContact)).doesNotThrowAnyException();
		ArgumentCaptor<TrackedCase> argumentCaptor5 = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(2)).save(argumentCaptor5.capture());


	}	

	@Test
	void createContactCaseWithTypeContactMedical() {

		var person = TrackedPersonDataInitializer.createTanja();
		var encounter = createFirstEncounterWithMichaelaFor(person);
		ContactPerson contact = encounter.getContact();
		contact.setIsHealthStaff(Boolean.TRUE);
		contact.setIsSenior(Boolean.TRUE);
		contact.setHasPreExistingConditions(Boolean.TRUE);
		var trackedCase = createIndexCaseFor(person);

		var event = EncounterReported.firstEncounter(encounter, person.getId());
		
		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));
		when(diaryEntries.findByTrackedPersonId(person.getId())).thenReturn(diary);
		when(people.findById(person.getId())).thenReturn(Optional.of(person));

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		ArgumentCaptor<TrackedCase> argumentCaptor = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor.capture());

		TrackedCase capturedArgument = argumentCaptor.getValue();

		assertIdentityOfMichaela(capturedArgument);
		assertThat(capturedArgument.isMedicalContactCase()).isTrue();
		
	}

	@Test
	void createContactCaseWithTypeContactVulnerableAsSenior() {

		var person = TrackedPersonDataInitializer.createTanja();
		var encounter = createFirstEncounterWithMichaelaFor(person);
		ContactPerson contact = encounter.getContact();
		contact.setIsHealthStaff(null);
		contact.setIsSenior(Boolean.TRUE);
		var trackedCase = createIndexCaseFor(person);

		var event = EncounterReported.firstEncounter(encounter, person.getId());

		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));
		when(diaryEntries.findByTrackedPersonId(person.getId())).thenReturn(diary);
		when(people.findById(person.getId())).thenReturn(Optional.of(person));

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		ArgumentCaptor<TrackedCase> argumentCaptor = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor.capture());

		TrackedCase capturedArgument = argumentCaptor.getValue();

		assertIdentityOfMichaela(capturedArgument);
		assertThat(capturedArgument.isVulnerableContactCase()).isTrue();

	}

	@Test
	void createContactCaseWithTypeContactVulnerableWithExistingPreConditions() {

		var person = TrackedPersonDataInitializer.createTanja();
		var encounter = createFirstEncounterWithMichaelaFor(person);
		ContactPerson contact = encounter.getContact();
		contact.setIsHealthStaff(null);
		contact.setIsSenior(null);
		contact.setHasPreExistingConditions(Boolean.TRUE);
		var trackedCase = createIndexCaseFor(person);

		var event = EncounterReported.firstEncounter(encounter, person.getId());

		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));
		when(diaryEntries.findByTrackedPersonId(person.getId())).thenReturn(diary);
		when(people.findById(person.getId())).thenReturn(Optional.of(person));

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		ArgumentCaptor<TrackedCase> argumentCaptor = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor.capture());

		TrackedCase capturedArgument = argumentCaptor.getValue();

		assertIdentityOfMichaela(capturedArgument);
		assertThat(capturedArgument.isVulnerableContactCase()).isTrue();

	}
	
	@Test
	void originContactIsSetCorrectly() {

		var person = TrackedPersonDataInitializer.createTanja();
		var encounter = createFirstEncounterWithMichaelaFor(person);
		var trackedCase = createIndexCaseFor(person);

		var event = EncounterReported.firstEncounter(encounter, person.getId());

		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));
		when(diaryEntries.findByTrackedPersonId(person.getId())).thenReturn(diary);
		when(people.findById(person.getId())).thenReturn(Optional.of(person));

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		ArgumentCaptor<TrackedCase> argumentCaptor = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor.capture());

		var origins = argumentCaptor.getValue().getOriginContacts();
		assertThat(origins.size() == 1);
		assertThat(origins.get(0).equals(encounter.getContact()));
	}	
	

	private void assertIdentityOfMichaela(TrackedCase capturedArgument) {
		assertThat(capturedArgument.getTrackedPerson().getFirstName()).isEqualTo("Michaela");
		assertThat(capturedArgument.getTrackedPerson().getLastName()).isEqualTo("Mustermann");
		assertThat(capturedArgument.getTrackedPerson().getEmailAddress())
				.isEqualTo(EmailAddress.of("michaela@mustermann.de"));
	}
	
	
	private Encounter createFirstEncounterWithMichaelaFor(TrackedPerson person) {
		
		var contactWays = ContactWays.ofEmailAddress("michaela@mustermann.de");
		var contactPerson = new ContactPerson("Michaela", "Mustermann", contactWays) //
				.assignOwner(person);
		var encounter = person.reportContactWith(contactPerson, LocalDate.now());
		
		return encounter;
	}
	

	private TrackedCase createIndexCaseFor(TrackedPerson person) {
		var trackedCase = new TrackedCase(person, CaseType.INDEX, new Department("Mannheim", UUID.randomUUID())) //
				.submitEnrollmentDetails() //
				.submitQuestionnaire(new MinimalQuestionnaire());
		return trackedCase;
	}
}
