package quarano.occasion;

import lombok.*;
import org.jmolecules.ddd.types.Identifier;
import quarano.core.QuaranoAggregate;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.occasion.Occasion.OccasionIdentifier;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "occasions")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Occasion extends QuaranoAggregate<Occasion, OccasionIdentifier> {

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "occasionCode") //
	private List<VisitorGroup> visitorGroups;
	private @Setter LocalDateTime start, end;
	private @Setter String title;
	private OccasionCode occasionCode;
	private TrackedCaseIdentifier trackedCaseId;

	Occasion(String title, LocalDateTime start, LocalDateTime end, OccasionCode eventCode,
			TrackedCaseIdentifier trackedCaseId) {

		this.id = OccasionIdentifier.of(UUID.randomUUID());
		this.start = start;
		this.end = end;
		this.title = title;
		this.visitorGroups = new ArrayList<>();
		this.occasionCode = eventCode;
		this.trackedCaseId = trackedCaseId;
	}

	Occasion registerVisitorGroup(VisitorGroup visitorGroup) {

		this.visitorGroups.add(visitorGroup);
		return this;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
	public static class OccasionIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -8938479214117686141L;

		private final UUID occasionId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return occasionId.toString();
		}
	}
}
