package quarano.account;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import quarano.account.Account.AccountIdentifier;
import quarano.account.Department.DepartmentIdentifier;
import quarano.account.Password.EncryptedPassword;
import quarano.core.EmailAddress;
import quarano.core.QuaranoAggregate;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import org.jddd.core.types.Identifier;

/**
 * An account of a user. Can be connected to a {@link TrackedPerson} by the {@link TrackedPersonIdentifier} or can be a
 * HD employee account
 *
 * @author Patrick Otto
 */
@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Account extends QuaranoAggregate<Account, AccountIdentifier> {

	@Getter @Setter private String username;
	@Getter private final EncryptedPassword password;

	@Getter @Setter private String firstname;
	@Getter @Setter private String lastname;
	
	@Getter @Setter private EmailAddress email;

	@Getter  private DepartmentIdentifier departmentId;

	@ManyToMany(fetch = FetchType.EAGER) //
	@Getter @Setter private List<Role> roles = new ArrayList<>();

	public Account(String username, EncryptedPassword password, String firstname, String lastname, EmailAddress email,
			DepartmentIdentifier departmentId, List<Role> roles) {
		super();

		this.id = AccountIdentifier.of(UUID.randomUUID());
		this.username = username.trim();
		this.firstname = firstname;
		this.lastname = lastname;
		this.departmentId = departmentId;
		this.password = password;
		this.email = email;

		roles.stream().forEach(it -> this.roles.add(it));
	}
	
	public Account(String username, EncryptedPassword password, String firstname, String lastname,
			DepartmentIdentifier departmentId, Role role) {
	
		this(username, password, firstname, lastname, null, departmentId, new ArrayList<>());
		this.add(role);
	}

	// for testing purposes
	Account(String username, EncryptedPassword password, String firstname, String lastname,
			DepartmentIdentifier departmentId, Role role, UUID fixedUID) {

		super();

		this.id = AccountIdentifier.of(fixedUID);
		this.username = username.trim();
		this.firstname = firstname;
		this.lastname = lastname;
		this.departmentId = departmentId;
		this.password = password;

		this.add(role);
	}

	public boolean belongsTo(DepartmentIdentifier id) {
		return this.departmentId.equals(id);
	}

	public String getFullName() {
		return firstname.concat(" ").concat(lastname);
	}

	public UUID getIdAsUuid() {
		return id.accountId;
	}

	/**
	 * Adds a role to the roles list of the user if is not already included
	 *
	 * @param roletype
	 */
	public Account add(Role role) {

		if (roles.contains(role)) {
			return this;
		}

		roles.add(role);

		return this;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class AccountIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = 7871473225101042167L;
		final UUID accountId;

		public String toString() {
			return accountId.toString();
		}
	}

	public boolean isTrackedPerson() {
		return this.roles.contains(new Role(RoleType.ROLE_USER));
	}

	/**
	 * Determines if a user has any kind of admin role
	 *
	 * @return
	 */
	public boolean hasAdminRole() {
		return this.roles.contains(Role.of(RoleType.ROLE_HD_ADMIN))
				|| this.roles.contains(Role.of(RoleType.ROLE_QUARANO_ADMIN));
	}
}
