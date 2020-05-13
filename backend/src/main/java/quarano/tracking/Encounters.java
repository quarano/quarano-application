package quarano.tracking;

import lombok.RequiredArgsConstructor;
import quarano.tracking.Encounter.EncounterIdentifier;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
public class Encounters implements Streamable<Encounter> {

	private final List<Encounter> encounters;

	public boolean hasBeenInTouchWith(ContactPerson person) {

		return encounters.stream() //
				.anyMatch(it -> it.getContact().equals(person));
	}

	public Optional<Encounter> havingIdOf(EncounterIdentifier id) {
		return encounters.stream() //
				// .filter(it -> it.hasId(id)) //
				.findFirst();
	}

	public boolean hasAtLeastOneEncounter() {
		return !encounters.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Encounter> iterator() {
		return encounters.iterator();
	}
}
