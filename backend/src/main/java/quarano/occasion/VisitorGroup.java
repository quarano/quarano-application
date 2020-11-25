package quarano.occasion;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import quarano.core.QuaranoAggregate;
import quarano.occasion.VisitorGroup.VisitorGroupIdentifier;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jmolecules.ddd.types.Identifier;
import org.springframework.util.Assert;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Table(name = "visitor_groups")
@NoArgsConstructor
public class VisitorGroup extends QuaranoAggregate<VisitorGroup, VisitorGroupIdentifier> {

	@OneToMany(cascade = CascadeType.ALL) //
	@JoinColumn(name = "visitor_group_id") //
	private List<Visitor> visitors;
	private LocalDateTime start, end;
	private OccasionCode occasionCode;
	private String comment;
	private String locationName;

	public VisitorGroup(LocalDateTime start, OccasionCode eventCode) {

		Assert.notNull(start, "Start date must not be null!");

		this.id = VisitorGroupIdentifier.of(UUID.randomUUID());
		this.start = start;
		this.occasionCode = eventCode;
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
