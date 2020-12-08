package quarano.department;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.account.Department;
import quarano.core.EmailAddress;
import quarano.diary.Diary;
import quarano.diary.DiaryManagement;
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactWays;
import quarano.tracking.Encounter;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.EncounterReported;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

/**
 * @author Oliver Drotbohm
 * @author Patrick Otto
 */
@QuaranoUnitTest
class TrackingEventListenerUnitTests {

	@Mock TrackedCaseRepository cases;
	@Mock TrackedPersonRepository people;
	@Mock DiaryManagement diaries;
	@Mock Diary diary;

	TrackingEventListener listener;

	@BeforeEach
	public void setup() {
		listener = new TrackingEventListener(cases, people, diaries);
	}

	@Test
	void rejectsEncounterReportIfEnrollmentQuestionnaireIncomplete() {

		var person = TrackedPersonDataInitializer.createTanja();
		var trackedCase = new TrackedCase(person, CaseType.INDEX, new Department("Mannheim"))
				.markInRegistration()
				.markRegistrationCompleted()
				.submitEnrollmentDetails();
		var contactPerson = new ContactPerson("Michaela", "Mustermann",
				ContactWays.ofEmailAddress("michaela@mustermann.de"))
						.setOwnerId(person.getId());

		var event = EncounterReported.firstEncounter(Encounter.with(contactPerson, LocalDate.now()), person.getId());

		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));

		assertThatExceptionOfType(EnrollmentException.class).isThrownBy(() -> listener.on(event));

		trackedCase.submitEnrollmentDetails()
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
	void createContactCaseWithTypeContactMedical() {

		var person = TrackedPersonDataInitializer.createTanja();
		var encounter = createFirstEncounterWithMichaelaFor(person);

		encounter.getContact()
				.setIsHealthStaff(Boolean.TRUE)
				.setIsSenior(Boolean.TRUE)//
				.setHasPreExistingConditions(Boolean.TRUE);

		var trackedCase = createIndexCaseFor(person);
		var event = EncounterReported.firstEncounter(encounter, person.getId());

		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		ArgumentCaptor<TrackedCase> argumentCaptor = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor.capture());

		TrackedCase capturedArgument = argumentCaptor.getValue();

		assertIdentityOfMichaela(capturedArgument);
		assertThat(capturedArgument.isMedicalContactCase()).isTrue();
	}

	@Test // CORE-185
	void createContactCaseWithTypeContactVulnerableAsSenior() {

		var person = TrackedPersonDataInitializer.createTanja();
		var encounter = createFirstEncounterWithMichaelaFor(person);
		encounter.getContact()
				.setIsHealthStaff(null)
				.setIsSenior(true);
		var trackedCase = createIndexCaseFor(person);

		var event = EncounterReported.firstEncounter(encounter, person.getId());

		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		var argumentCaptor = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor.capture());

		var capturedArgument = argumentCaptor.getValue();

		assertIdentityOfMichaela(capturedArgument);
		assertThat(capturedArgument.isVulnerableContactCase()).isTrue();
	}

	@Test // CORE-185
	void createContactCaseWithTypeContactVulnerableWithExistingPreConditions() {

		var person = TrackedPersonDataInitializer.createTanja();
		var encounter = createFirstEncounterWithMichaelaFor(person);

		encounter.getContact()
				.setIsHealthStaff(null)
				.setIsSenior(null)
				.setHasPreExistingConditions(true);

		var trackedCase = createIndexCaseFor(person);
		var event = EncounterReported.firstEncounter(encounter, person.getId());

		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		var argumentCaptor = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor.capture());

		var capturedArgument = argumentCaptor.getValue();

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
		var contactPerson = new ContactPerson("Michaela", "Mustermann", contactWays)
				.assignOwner(person);

		return person.reportContactWith(contactPerson, LocalDate.now());
	}

	private TrackedCase createIndexCaseFor(TrackedPerson person) {

		return new TrackedCase(person, CaseType.INDEX, new Department("Mannheim"))
				.markInRegistration()
				.markRegistrationCompleted()
				.submitEnrollmentDetails()
				.submitQuestionnaire(new MinimalQuestionnaire());
	}
}
