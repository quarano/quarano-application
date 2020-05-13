package quarano.tracking.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.department.TrackingEventListener;
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactPersonRepository;
import quarano.tracking.ContactWays;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.EncounterReported;
import quarano.tracking.TrackedPersonRepository;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.moduliths.test.PublishedEvents;
import org.moduliths.test.PublishedEventsExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
@ExtendWith(PublishedEventsExtension.class)
class TrackedPersonIntegrationTests {

	private final TrackedPersonRepository people;
	private final ContactPersonRepository contacts;

	@MockBean TrackingEventListener listener;

	@Test
	void publishesEventOnEncounterReport(PublishedEvents events) {

		var person = people.save(new TrackedPerson("Michael", "Mustermann"));
		var contact = contacts
				.save(new ContactPerson("Michaela", "Mustermann", ContactWays.ofEmailAddress("michaela@mustermann.de")));

		person.reportContactWith(contact, LocalDate.now());

		people.save(person);

		var expected = events.ofType(EncounterReported.class) //
				.matching(it -> it.getPersonIdentifier().equals(person.getId())) //
				.matching(it -> it.isFirstEncounterWithTargetPerson());

		assertThat(expected).hasSize(1);
	}
	
	@Test
	void firstEncounterFlagSetCorrectlyOnSecondEncounter(PublishedEvents events) {

		var person = people.save(new TrackedPerson("Michael", "Mustermann"));
		var contact = contacts
				.save(new ContactPerson("Michaela", "Mustermann", ContactWays.ofEmailAddress("michaela@mustermann.de")));

		person.reportContactWith(contact, LocalDate.now().minusDays(1));
		person.reportContactWith(contact, LocalDate.now());

		people.save(person);

		var expectedEventsTotal = events.ofType(EncounterReported.class) //
				.matching(it -> it.getPersonIdentifier().equals(person.getId())); //
		assertThat(expectedEventsTotal).hasSize(2);
		
		var expectedEventsFirstEncounter = events.ofType(EncounterReported.class) //
				.matching(it -> it.getPersonIdentifier().equals(person.getId())) //
				.matching(it -> it.isFirstEncounterWithTargetPerson());
		assertThat(expectedEventsFirstEncounter).hasSize(1);
		
	}
}
