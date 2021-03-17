package quarano.tracking;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import quarano.core.QuaranoEntity;
import quarano.tracking.Encounter.EncounterIdentifier;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.jmolecules.ddd.types.Identifier;
import org.springframework.util.Assert;

/**
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 */
@Entity
@Table(name = "encounters")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Encounter extends QuaranoEntity<TrackedPerson, EncounterIdentifier> {

	@ManyToOne @JoinColumn(name = "contact_person_id")
	private final @Getter ContactPerson contact;

	@ManyToOne @JoinColumn(name = "location_id")
	private @Getter Location location;

	@Column(name = "encounter_date")
	private final @Getter LocalDate date;

	private Encounter(ContactPerson contact, LocalDate date) {

		Assert.notNull(contact, "ContactPerson must not be null!");
		Assert.notNull(date, "Date must not be null!");

		this.id = EncounterIdentifier.of(UUID.randomUUID());
		this.contact = contact;
		this.date = date;
	}

	private Encounter(ContactPerson contact, Location location, LocalDate date) {

		Assert.notNull(contact, "ContactPerson must not be null!");
		Assert.notNull(location, "Location must not be null!");
		Assert.notNull(date, "Date must not be null!");

		this.id = EncounterIdentifier.of(UUID.randomUUID());
		this.contact = contact;
		this.location = location;
		this.date = date;
	}

	public static Encounter with(ContactPerson person, LocalDate date) {
		return new Encounter(person, date);
	}

	public static Encounter withPersonAtLocation(ContactPerson person, Location location, LocalDate date) {
		return new Encounter(person, location, date);
	}

	public boolean isEncounterWith(ContactPerson person) {
		return this.contact == person;
	}

	public boolean hasId(EncounterIdentifier id) {
		return this.id.equals(id);
	}

	public boolean happenedOn(LocalDate date) {
		return this.date.equals(date);
	}

	public boolean isSameAs(Encounter that) {

		return this.date.equals(that.date)
				&& this.contact.equals(that.contact);
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
	public static class EncounterIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = -5998761917714158567L;
		private final UUID encounterId;
	}
}
