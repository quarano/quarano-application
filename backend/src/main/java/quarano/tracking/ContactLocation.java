package quarano.tracking;

import lombok.*;
import org.jddd.core.types.Identifier;
import quarano.core.QuaranoEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A specific contact event at a certain location
 */
@Entity
@Table(name = "contact_locations")
@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ContactLocation extends QuaranoEntity<TrackedPerson, ContactLocation.ContactLocationId> {

	// The name of the place or an event, e.g. "Tanzschule Müller" or "Geburtstagsfeier"
	private String name;
	private @ManyToOne @JoinColumn(name = "location_id") Location location;

	// This is not a q.t.ContactPerson but rather the person to contact for the given location
	// Both name and person has to be in this table instead of "locations" because multiple events can happen at the same
	// place.
	private String contactPerson;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String notes;

	public ContactLocation(String name, Location location, String contactPerson, LocalDateTime startTime,
			LocalDateTime endTime, String notes) {
		super();
		this.id = ContactLocationId.of(UUID.randomUUID());
		this.name = name;
		this.location = location;
		this.contactPerson = contactPerson;
		this.startTime = startTime;
		this.endTime = endTime;
		this.notes = notes;
	}

	@Embeddable
	@Value
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static final class ContactLocationId implements Identifier, Serializable {
		private static final long serialVersionUID = -1142558629781774129L;
		private final UUID contactLocationId;

		@Override
		public String toString() {
			return contactLocationId.toString();
		}
	}
}
