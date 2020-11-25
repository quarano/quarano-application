package quarano.occasion;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.QuaranoAggregate;
import quarano.tracking.Address;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jmolecules.ddd.types.Identifier;

@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Table(name = "visitors")
public class Visitor extends QuaranoAggregate<Visitor, Visitor.VisitorIdentifier> {

	private String firstName, lastName;
	private EmailAddress emailAddress;
	private PhoneNumber phoneNumber;
	private Address address = new Address();

	public Visitor(String firstName, String lastName, Address address) {

		this.id = VisitorIdentifier.of(UUID.randomUUID());
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(
			access = AccessLevel.PRIVATE, force = true)
	public static class VisitorIdentifier implements Identifier, Serializable {

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
