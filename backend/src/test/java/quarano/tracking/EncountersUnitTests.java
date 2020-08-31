package quarano.tracking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
		var tp1 = mock(TrackedPerson.class);
		var tp2 = mock(TrackedPerson.class);
		var cp1 = mock(ContactPerson.class);
		var cp2 = mock(ContactPerson.class);

		when(cp1.getTrackedPerson()).thenReturn(tp1);
		when(cp2.getTrackedPerson()).thenReturn(tp2);

		var encounterList = List.of(Encounter.with(cp1, LocalDate.now()), Encounter.with(cp1, LocalDate.now()),
				Encounter.with(cp2, LocalDate.now()));
		var encounters = Encounters.of(encounterList);
		return encounters;
	}
}
