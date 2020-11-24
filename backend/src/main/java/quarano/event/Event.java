package quarano.event;

import lombok.*;
import org.jmolecules.ddd.types.Identifier;
import quarano.core.QuaranoAggregate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Setter
@Getter
@Entity
@Table(name = "events")
public class Event extends QuaranoAggregate<Event, Event.EventIdentifier> {

	@OneToMany(cascade = CascadeType.ALL,
			mappedBy = "eventCode") private List<VisitorGroup> visitorGroups;
	private LocalDateTime start;
	private LocalDateTime end;
	private String title;
	private @Setter(AccessLevel.NONE) EventCode eventCode;

	public Event(String title, LocalDateTime start, LocalDateTime end, EventCode eventCode) {
		this.start = start;
		this.end = end;
		this.title = title;
		this.id = EventIdentifier.of(UUID.randomUUID());
		this.visitorGroups = new ArrayList<>();
		this.eventCode = eventCode;
	}

	Event registerVisitorGroup(VisitorGroup visitorGroup) {
		this.visitorGroups.add(visitorGroup);
		return this;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
	public static class EventIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -8938479214117686141L;

		private final UUID eventId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return eventId.toString();
		}
	}
}
