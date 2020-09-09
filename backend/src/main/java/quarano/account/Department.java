package quarano.account;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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

import org.jddd.core.types.Identifier;

/**
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 */
@Entity
@Table(name = "departments")
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Department extends QuaranoAggregate<Department, DepartmentIdentifier> {

	private @Getter @Column(unique = true) String name;

	private @Getter @Column(unique = true) String rkiCode;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "department_id")
	private @Getter Set<DepartmentContact> contacts = new HashSet<>();

	public Department(String name) {
		this(name, "0.0.0.0.");
	}

	public Department(String name, String rkiCode) {
		this(name, UUID.randomUUID(), rkiCode);
	}

	public Department(String name, UUID uuid, String rkiCode) {
		this(name, DepartmentIdentifier.of(uuid), rkiCode);
	}

	// fixed Id department for tests and integration data
	Department(String name, DepartmentIdentifier fixedId, String rkiCode) {

		this.id = fixedId;
		this.name = name;
		this.rkiCode = rkiCode;
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
