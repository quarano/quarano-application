package quarano.department;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import quarano.QuaranoUnitTest;
import quarano.account.Department;
import quarano.department.TrackedCase.CaseConvertedToIndex;
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactWays;
import quarano.tracking.Encounter;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonDataInitializer;

/**
 * @author Jens Kutzsche
 */
@QuaranoUnitTest
class TrackedCaseEventListenerTests {

	private static final String MAX = "Max";
	private static final String MORITZ = "Moritz";
	private static final String MUSTERMANN = "Mustermann";
	private static final String MANNHEIM = "Mannheim";
	
	private static final String FIRST_NAME = "trackedPerson.firstName";
	private static final String LAST_NAME = "trackedPerson.lastName";
	private static final String DEPARTMENT_NAME = "department.name";

	@Mock TrackedCaseRepository cases;

	@Captor ArgumentCaptor<TrackedCase> captor;

	private TrackedCaseEventListener listener;

	@BeforeEach
	void setup() {

		listener = new TrackedCaseEventListener(cases);
	}

	@Test
	void testIgnoreNonIndexCase() {

		var contactDate = LocalDate.now();
		var person = TrackedPersonDataInitializer.createTanja();
		createEncounterWithMaxFor(person, contactDate);
		createEncounterWithMoritzFor(person, contactDate);
		var trackedCase = createCaseFor(person, CaseType.CONTACT);

		listener.on(CaseConvertedToIndex.of(trackedCase));

		verify(cases, never()).save(any());
	}

	@Test
	void testCreateContactCase() {

		var contactDate = LocalDate.now().minusDays(5);
		var person = TrackedPersonDataInitializer.createTanja();
		var trackedCase = createCaseFor(person, CaseType.INDEX);
		createEncounterWithMaxFor(person, contactDate);
		createEncounterWithMoritzFor(person, contactDate);

		listener.on(CaseConvertedToIndex.of(trackedCase));

		verify(cases, times(2)).save(captor.capture());

		assertThat(captor.getAllValues()).hasSize(2)//
				.extracting(FIRST_NAME, LAST_NAME, DEPARTMENT_NAME)//
				.containsOnly(tuple(MAX, MUSTERMANN, MANNHEIM), tuple(MORITZ, MUSTERMANN, MANNHEIM));
	}

	@Test
	void testCreateNoDuplicateCase() {

		var contactDate = LocalDate.now();
		var person = TrackedPersonDataInitializer.createTanja();
		var encounter = createEncounterWithMaxFor(person, contactDate);
		var trackedCase = createCaseFor(person, CaseType.INDEX);

		person.reportContactWith(encounter.getContact(), contactDate.minusDays(1));
		person.reportContactWith(encounter.getContact(), contactDate.minusDays(2));

		when(cases.existsByOriginContacts(encounter.getContact())).thenReturn(false, true, true);

		listener.on(CaseConvertedToIndex.of(trackedCase));

		verify(cases, times(1)).save(captor.capture());
		verify(cases, times(3)).existsByOriginContacts(encounter.getContact());

		assertThat(captor.getAllValues()).hasSize(1)//
				.extracting(FIRST_NAME, LAST_NAME, DEPARTMENT_NAME)//
				.containsOnly(tuple(MAX, MUSTERMANN, MANNHEIM));
	}

	private Encounter createEncounterWithMaxFor(TrackedPerson person, LocalDate date) {

		var contactWays = ContactWays.ofEmailAddress("max@mustermann.de");
		var contactPerson = new ContactPerson(MAX, MUSTERMANN, contactWays) //
				.assignOwner(person);

		return person.reportContactWith(contactPerson, date);
	}

	private Encounter createEncounterWithMoritzFor(TrackedPerson person, LocalDate date) {

		var contactWays = ContactWays.ofEmailAddress("moritz@mustermann.de");
		var contactPerson = new ContactPerson(MORITZ, MUSTERMANN, contactWays) //
				.assignOwner(person);

		return person.reportContactWith(contactPerson, date);
	}

	private TrackedCase createCaseFor(TrackedPerson person, CaseType type) {

		return new TrackedCase(person, type, new Department(MANNHEIM)) //
				.submitEnrollmentDetails() //
				.submitQuestionnaire(new MinimalQuestionnaire());
	}
}
