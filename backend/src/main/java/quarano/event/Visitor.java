package quarano.event;

import lombok.*;
import org.jmolecules.ddd.types.Identifier;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.QuaranoAggregate;
import quarano.tracking.Address;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Data
@Table(name = "visitors")
public class Visitor extends QuaranoAggregate<Visitor, Visitor.VisitorIdentifier> {

	private String firstName, lastName;
	private EmailAddress emailAddress;
	private PhoneNumber phoneNumber;
	private Address address = new Address();

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(
			access = AccessLevel.PRIVATE, force = true)
	public static class VisitorIdentifier
			implements Identifier, Serializable {

		private static final long serialVersionUID = -8938479214117686141L;

		private final UUID visitorId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return visitorId.toString();
		}
	}
}
