package quarano.tracking;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author Jens Kutzsche
 */
class EncountersUnitTests {

	@Test
	void numberOfEncountersCorrect() {
		assertThat(createEncounter().getNumberOfEncounters()).isEqualTo(3);
	}

	@Test
	void numberOfUniqueContactsCorrect() {
		assertThat(createEncounter().getNumberOfUniqueContacts()).isEqualTo(2);
	}

	static Encounters createEncounter() {
		var cp1 = mock(ContactPerson.class);
		var cp2 = mock(ContactPerson.class);

		var encounterList = List.of(Encounter.withPerson(cp1, LocalDate.now()), Encounter.withPerson(cp1, LocalDate.now()),
				Encounter.withPerson(cp2, LocalDate.now()));
		var encounters = Encounters.of(encounterList);
		return encounters;
	}
}
