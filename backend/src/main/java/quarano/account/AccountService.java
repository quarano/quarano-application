package quarano.account;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.Account.AccountIdentifier;
import quarano.account.Department.DepartmentIdentifier;
import quarano.account.Password.EncryptedPassword;
import quarano.account.Password.UnencryptedPassword;
import quarano.core.EmailAddress;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class AccountService {

	private final @NonNull PasswordEncoder passwordEncoder;
	private final @NonNull AccountRepository accounts;
	private final @NonNull RoleRepository roles;
	private final @NonNull AuthenticationManager authentication;

	/**
	 * creates a new account, encrypts the password and stores it
	 *
	 * @param username
	 * @param password
	 * @param firstname
	 * @param lastname
	 * @param departmentId
	 * @param clientId
	 * @param roleType
	 * @return
	 */
	public Account createTrackedPersonAccount(String username, UnencryptedPassword password, String firstname,
			String lastname, DepartmentIdentifier departmentId) {

		var role = roles.findByType(RoleType.ROLE_USER);
		var account = accounts.save(new Account(username, encrypt(password), firstname, lastname, departmentId, role));

		return account;
	}

	public Account createStaffAccount(String username, UnencryptedPassword password, String firstname, String lastname,
			EmailAddress email, DepartmentIdentifier departmentId, List<RoleType> roleTypes) {

		var roleList = roleTypes.stream()
				.map(it -> roles.findByType(it))
				.collect(Collectors.toList());

		var account = accounts
				.save(new Account(username, encrypt(password), firstname, lastname, email, departmentId, roleList));

		log.info("Created staff account for " + username);

		return account;
	}

	public Account saveStaffAccount(Account account) {

		var storedAccount = accounts.save(account);

		log.info("Updated staff account for " + storedAccount.getUsername());

		return storedAccount;
	}

	public Account createStaffAccount(String username, UnencryptedPassword password, String firstname, String lastname,
			EmailAddress email, DepartmentIdentifier departmentId, RoleType roleType) {
		return createStaffAccount(username, password, firstname, lastname, email, departmentId, List.of(roleType));
	}

	public boolean isValidUsername(String candidate) {

		if (!isUsernameAvailable(candidate)) {
			return false;
		}
		// check username pattern
		return true;
	}

	public Optional<Account> findByUsername(String username) {
		return accounts.findByUsername(username);
	}

	public boolean isUsernameAvailable(String userName) {
		return accounts.findByUsername(userName)
				.isEmpty();
	}

	public boolean matches(@Nullable UnencryptedPassword candidate, EncryptedPassword existing) {

		Assert.notNull(existing, "Existing password must not be null!");

		return Optional.ofNullable(candidate)
				.map(c -> passwordEncoder.matches(c.asString(), existing.asString()))
				.orElse(false);
	}

	public List<Account> findStaffAccountsFor(DepartmentIdentifier departmentId) {
		return accounts.findAccountsFor(departmentId)
				.filter(it -> hasDepartmentRoles(it))
				.collect(Collectors.toList());
	}

	public Optional<Account> findById(AccountIdentifier accountId) {
		return accounts.findById(accountId);
	}

	public void deleteAccount(AccountIdentifier accountIdToDelete) {

		accounts.deleteById(accountIdToDelete);

		log.info("Account with accountId " + accountIdToDelete + " has been deleted.");
	}

	/**
	 * Changes the password of the given {@link Account} to the given {@link UnencryptedPassword} password.
	 *
	 * @param password must not be {@literal null}.
	 * @param account must not be {@literal null}.
	 * @return the account with the new password applied.
	 */
	public Account changePassword(UnencryptedPassword password, Account account) {

		if (account.isTrackedPerson() && !isPasswordChangeBySamePerson(account)) {
			throw new IllegalArgumentException("Tracked people can only change their passwords themselves!");
		}

		log.debug("Reset password for account {}.", account.getUsername());

		return accounts.save(account.setPassword(encrypt(password, account)));
	}

	/**
	 * Check if the account has at least one role that is a department-role
	 *
	 * @param account
	 * @return
	 */
	private boolean hasDepartmentRoles(Account account) {

		return account.getRoles()
				.stream()
				.filter(it -> it.getRoleType().isDepartmentRole())
				.findFirst()
				.isPresent();
	}

	private boolean isPasswordChangeBySamePerson(Account account) {
		return authentication.getCurrentUser().map(account::equals).orElse(false);
	}

	/**
	 * Creates a {@link EncryptedPassword} that's not expired.
	 *
	 * @param password must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	private EncryptedPassword encrypt(UnencryptedPassword password) {
		return encrypt(password, false);
	}

	/**
	 * Creates an {@link EncryptedPassword} for the given {@link UnencryptedPassword}. The expiry settings will be derived
	 * from the given target {@link Account} the password is to be encrypted for. This will be the case if it's not
	 * someone changing their own password but only if an admin changes the password for someone else.
	 *
	 * @param password must not be {@literal null}.
	 * @param account must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	private EncryptedPassword encrypt(UnencryptedPassword password, Account account) {

		Assert.notNull(password, "Password must not be null!");
		Assert.notNull(account, "Account must not be null!");

		var expire = authentication.getCurrentUser()
				.filter(it -> !account.equals(it))
				.map(Account::isAdmin)
				.orElse(false);

		return encrypt(password, expire);
	}

	/**
	 * Encrypts the given {@link UnencryptedPassword} and marks it as expired if needed.
	 *
	 * @param password must not be {@literal null}.
	 * @param expirePassword whether to mark the encrypted password as expired.
	 * @return will never be {@literal null}.
	 */
	private EncryptedPassword encrypt(UnencryptedPassword password, boolean expirePassword) {

		Assert.notNull(password, "Password must not be null!");

		var encoded = passwordEncoder.encode(password.asString());

		return expirePassword
				? EncryptedPassword.of(encoded, LocalDateTime.now())
				: EncryptedPassword.of(encoded);
	}
}
