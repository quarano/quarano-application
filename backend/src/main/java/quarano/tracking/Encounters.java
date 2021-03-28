package quarano.tracking;

import static java.util.stream.Collectors.*;

import lombok.RequiredArgsConstructor;
import quarano.tracking.Encounter.EncounterIdentifier;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
public class Encounters implements Streamable<Encounter> {

	private final List<Encounter> encounters;

	public boolean hasBeenInTouchWith(ContactPerson person) {

		return encounters.stream()
				.anyMatch(it -> it.getContact().equals(person));
	}

	public boolean hasBeenAt(Location location) {

		return encounters.stream()
				.anyMatch(it -> it.getLocation().equals(location));
	}

	public Optional<Encounter> getEncounter(ContactPerson person, LocalDate date) {

		return encounters.stream()
				.filter(it -> it.happenedOn(date))
				.filter(it -> it.isEncounterWith(person))
				.findFirst();
	}

	public Optional<Encounter> getEncounter(Location location, LocalDate date) {

		return encounters.stream()
				.filter(it -> it.happenedOn(date))
				.filter(it -> it.isEncounterAt(location))
				.findFirst();
	}

	public Optional<Encounter> getEncounter(ContactPerson person, Location location, LocalDate date) {

		return encounters.stream()
				.filter(it -> it.happenedOn(date))
				.filter(it -> it.isEncounterWith(person))
				.filter(it -> it.isEncounterAt(location))
				.findFirst();
	}

	public Optional<Encounter> havingIdOf(EncounterIdentifier id) {
		return encounters.stream()
				.filter(it -> it.hasId(id))
				.findFirst();
	}

	public boolean hasAtLeastOneEncounter() {
		return !encounters.isEmpty();
	}

	public Optional<LocalDate> getDateOfFirstEncounterWith(ContactPerson contact) {

		return encounters.stream()
				.filter(it -> it.isEncounterWith(contact))
				.sorted(Comparator.comparing(Encounter::getDate))
				.findFirst()
				.map(Encounter::getDate);
	}

	/**
	 * @since 1.4
	 */
	public Optional<LocalDate> getDateOfLastEncounterWith(ContactPerson contact) {

		return encounters.stream()
				.filter(it -> it.isEncounterWith(contact))
				.map(Encounter::getDate)
				.reduce(BinaryOperator.maxBy(Comparator.naturalOrder()));
	}

	public Map<ContactPerson, List<Encounter>> getEncountersGroupedByContactPerson() {
		return encounters.stream()
				.collect(Collectors.groupingBy(Encounter::getContact));
	}

	public Map<ContactPerson, List<LocalDate>> getContactDatesGroupedByContactPerson() {

		var contactDatesGroupedByContactPerson = encounters.stream()//
				.collect(groupingBy(Encounter::getContact, mapping(Encounter::getDate, Collectors.toList())));

		contactDatesGroupedByContactPerson.forEach((key, list) -> list.sort(Comparator.naturalOrder()));

		return contactDatesGroupedByContactPerson;
	}

	public long getNumberOfEncounters() {
		return encounters.size();
	}

	public long getNumberOfUniqueContacts() {
		return encounters.stream().map(Encounter::getContact).distinct().count();
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
