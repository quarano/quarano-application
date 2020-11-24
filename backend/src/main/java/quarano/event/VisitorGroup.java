package quarano.event;

import lombok.*;
import org.jmolecules.ddd.types.Identifier;
import quarano.core.QuaranoAggregate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "visitor_groups")
@NoArgsConstructor
public class VisitorGroup extends QuaranoAggregate<VisitorGroup, VisitorGroup.VisitorGroupIdentifier> {

	@OneToMany @JoinColumn(name = "visitor_group_id") private List<Visitor> visitorList;
	private LocalDateTime start, end;
	private EventCode eventCode;
	private String comment;
	private String locationName;

	public VisitorGroup(LocalDateTime start, EventCode eventCode) {
		this.id = VisitorGroupIdentifier.of(UUID.randomUUID());
		this.start = start;
		this.eventCode = eventCode;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
	public static class VisitorGroupIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -8938479214117686141L;

		private final UUID visitorGroupId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return visitorGroupId.toString();
		}
	}
}
