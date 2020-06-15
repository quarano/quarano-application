package quarano.tracking;

import lombok.RequiredArgsConstructor;
import quarano.tracking.Encounter.EncounterIdentifier;

import java.time.LocalDate;
import java.util.Comparator;
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

	public Optional<Encounter> getEncounter(ContactPerson person, LocalDate date) {

		return encounters.stream() //
				.filter(it -> it.happenedOn(date)) //
				.filter(it -> it.isEncounterWith(person)) //
				.findFirst();
	}

	public Optional<Encounter> havingIdOf(EncounterIdentifier id) {
		return encounters.stream() //
				.filter(it -> it.hasId(id)) //
				.findFirst();
	}

	public boolean hasAtLeastOneEncounter() {
		return !encounters.isEmpty();
	}

	public Optional<LocalDate> getDateOfFirstEncounterWith(ContactPerson contact) {

		return encounters.stream() //
				.filter(it -> it.isEncounterWith(contact)) //
				.sorted(Comparator.comparing(Encounter::getDate))
				.findFirst() //
				.map(Encounter::getDate);
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
