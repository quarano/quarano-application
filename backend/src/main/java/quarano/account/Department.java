package quarano.account;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import quarano.account.Department.DepartmentIdentifier;
import quarano.account.DepartmentContact.ContactType;
import quarano.core.QuaranoAggregate;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jmolecules.ddd.types.Identifier;

/**
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 * @author Jens Kutzsche
 */
@Entity
@Table(name = "departments")
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Department extends QuaranoAggregate<Department, DepartmentIdentifier> {

	private @Getter @Column(unique = true) String name;

	private @Getter @Setter(value = AccessLevel.PACKAGE) @Column(unique = true) String rkiCode;
	private @Getter @Setter(value = AccessLevel.PACKAGE) String federalState;
	private @Getter @Setter(value = AccessLevel.PACKAGE) String district;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "department_id")
	private @Getter Set<DepartmentContact> contacts = new HashSet<>();

	public Department(String name) {
		this(name, "0.0.0.0.", "", "");
	}

	public Department(String name, String rkiCode, String federalState, String district) {
		this(name, UUID.randomUUID(), rkiCode, federalState, district);
	}

	public Department(String name, UUID uuid, String rkiCode, String federalState, String district) {
		this(name, DepartmentIdentifier.of(uuid), rkiCode, federalState, district);
	}

	// fixed Id department for tests and integration data
	Department(String name, DepartmentIdentifier fixedId, String rkiCode, String federalState, String district) {

		this.id = fixedId;
		this.name = name;
		this.rkiCode = rkiCode;
		this.federalState = federalState;
		this.district = district;
	}

	Set<DepartmentContact> getContacts() {
		return Collections.unmodifiableSet(contacts);
	}

	Department setContacts(Set<DepartmentContact> contacts) {

		this.contacts.clear();
		this.contacts.addAll(contacts);

		return this;
	}

	public Optional<DepartmentContact> getContact(ContactType contactType) {
		return getContacts().stream()
				.filter(contact -> contact.getType().equals(contactType))
				.findFirst();
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class DepartmentIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = 7871473225101042167L;

		final UUID departmentId;

		@Override
		public String toString() {
			return departmentId.toString();
		}
	}
}
