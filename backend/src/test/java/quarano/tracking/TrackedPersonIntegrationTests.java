package quarano.tracking;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoIntegrationTest
class TrackedPersonIntegrationTests {

	private final TrackedPersonRepository people;
	private final ContactPersonRepository contacts;

	@Test
	public void registersEncounter() {

		var personId = TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2;
		var person = people.findById(personId).orElseThrow();

		var contact = contacts.save(new ContactPerson("Max", "Mustermann", ContactWays.ofEmailAddress("max@mustermann.de")) //
				.assignOwner(person));

		person.reportContactWith(contact, LocalDate.now());

		people.save(person);
	}
}
