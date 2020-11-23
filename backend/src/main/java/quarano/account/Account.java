package quarano.account;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;
import quarano.account.Account.AccountIdentifier;
import quarano.account.Department.DepartmentIdentifier;
import quarano.account.Password.EncryptedPassword;
import quarano.core.EmailAddress;
import quarano.core.QuaranoAggregate;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.event.types.DomainEvent;
import org.springframework.util.Assert;

/**
 * An account of a user. Can be connected to a {@link TrackedPerson} by the {@link TrackedPersonIdentifier} or can be a
 * HD employee account
 *
 * @author Patrick Otto
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 */
@Entity
@Table(name = "accounts")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Getter
@Setter(AccessLevel.PACKAGE)
public class Account extends QuaranoAggregate<Account, AccountIdentifier> {

	private EncryptedPassword password;
	private String username, firstname, lastname;
	private EmailAddress email;

	private @Setter(AccessLevel.NONE) DepartmentIdentifier departmentId;

	@ManyToMany(fetch = FetchType.EAGER) //
	private List<Role> roles = new ArrayList<>();

	private @Setter(AccessLevel.NONE) @Nullable PasswordResetToken passwordResetToken;

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

	public boolean isTrackedPerson() {
		return hasAnyRole(RoleType.ROLE_USER);
	}

	public boolean isCaseAgent() {
		return hasAnyRole(RoleType.ROLE_HD_CASE_AGENT);
	}

	/**
	 * Determines if a user has any kind of admin role
	 *
	 * @return
	 */
	public boolean isAdmin() {
		return hasAnyRole(RoleType.ROLE_HD_ADMIN, RoleType.ROLE_QUARANO_ADMIN);
	}

	/**
	 * Sets the given password and wipes a potentially existing {@link PasswordResetToken}.
	 *
	 * @param password must not be {@literal null}.
	 * @return
	 * @since 1.4
	 */
	Account setPassword(EncryptedPassword password) {

		Assert.notNull(password, "Password! must not be null!");

		this.password = password;
		this.passwordResetToken = null;

		return this;
	}

	/**
	 * Returns whether a password reset was requested.
	 *
	 * @return
	 * @see #requestPasswordReset(LocalDateTime)
	 * @since 1.4
	 */
	public boolean wasPasswordResetRequested() {
		return passwordResetToken != null;
	}

	/**
	 * Returns whether the {@link Account} has a valid {@link PasswordResetToken}.
	 *
	 * @return
	 * @see #requestPasswordReset(LocalDateTime)
	 * @since 1.4
	 */
	boolean hasValidPasswordResetToken() {
		return passwordResetToken != null && passwordResetToken.isValid();
	}

	/**
	 * Returns whether the given reset token is the one currently registered in {@link PasswordResetToken}.
	 *
	 * @param token must not be {@literal null}.
	 * @return
	 * @see #requestPasswordReset(LocalDateTime)
	 * @since 1.4
	 */
	boolean isValidResetToken(UUID token) {

		Assert.notNull(token, "Token must not be null!");

		return passwordResetToken != null && passwordResetToken.isValid(token);
	}

	/**
	 * Requests a password reset expiring at the given date.
	 *
	 * @param expiryDate must not be {@literal null}.
	 * @return a {@link PasswordResetToken} to authorize the password reset.
	 * @since 1.4
	 */
	PasswordResetToken requestPasswordReset(LocalDateTime expiryDate) {

		Assert.notNull(expiryDate, "Expiry date must not be null!");

		this.passwordResetToken = PasswordResetToken.expiringAt(expiryDate);

		registerEvent(PasswordResetRequested.of(getId()));

		return passwordResetToken;
	}

	@Value
	@Embeddable
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class PasswordResetToken {

		@Column(name = "password_reset_token") //
		UUID token;

		@Column(name = "password_reset_expiry_date") //
		LocalDateTime expiryDate;

		public static PasswordResetToken expiringAt(LocalDateTime date) {
			return new PasswordResetToken(UUID.randomUUID(), date);
		}

		boolean isValid() {
			return LocalDateTime.now().isBefore(expiryDate);
		}

		boolean isValid(UUID token) {
			return isValid() && this.token.equals(token);
		}
	}

	private boolean hasAnyRole(RoleType... roles) {

		var candidates = List.of(roles);

		return this.roles.stream()
				.map(Role::getRoleType).anyMatch(candidates::contains);
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class AccountIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = 7871473225101042167L;

		final UUID accountId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return accountId.toString();
		}
	}

	@Value(staticConstructor = "of")
	public static class PasswordResetRequested implements DomainEvent {
		AccountIdentifier accountId;
	}
}
