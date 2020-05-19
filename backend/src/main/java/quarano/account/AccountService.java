package quarano.account;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.Account.AccountIdentifier;
import quarano.account.Department.DepartmentIdentifier;
import quarano.account.Password.EncryptedPassword;
import quarano.account.Password.UnencryptedPassword;
import quarano.core.EmailAddress;

import java.util.ArrayList;
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

		var role = roles.findByName(RoleType.ROLE_USER.toString());
		var account = accounts.save(new Account(username, encrypt(password), firstname, lastname, departmentId, role));

		return account;
	}

	public Account createStaffAccount(String username, UnencryptedPassword password, String firstname, String lastname,
			EmailAddress email, DepartmentIdentifier departmentId, List<RoleType> roleTypes) {

		var encryptedPassword = EncryptedPassword.of(passwordEncoder.encode(password.asString()));

		var roleList = roleTypes.stream().map(it -> roles.findByName(it.toString())).collect(Collectors.toList());
		var account = accounts
				.save(new Account(username, encryptedPassword, firstname, lastname, email, departmentId, roleList));

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

		List<RoleType> roles = new ArrayList<>();
		roles.add(roleType);
		return createStaffAccount(username, password, firstname, lastname, email, departmentId, roles);

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
		return accounts.findByUsername(userName).isEmpty();
	}

	public boolean matches(@Nullable UnencryptedPassword candidate, EncryptedPassword existing) {

		Assert.notNull(existing, "Existing password must not be null!");

		return Optional.ofNullable(candidate).//
				map(c -> passwordEncoder.matches(c.asString(), existing.asString())).//
				orElse(false);
	}

	public List<Account> findStaffAccountsFor(DepartmentIdentifier departmentId) {
		return accounts.findAccountsFor(departmentId) //
				.filter(it -> hasDepartmentRoles(it)) //
				.collect(Collectors.toList());
	}

	/**
	 * Check if the account has at least one role that is a department-role
	 *
	 * @param account
	 * @return
	 */
	private boolean hasDepartmentRoles(Account account) {
		return account.getRoles().stream() //
				.filter(it -> it.getRoleType().isDepartmentRole()) //
				.findFirst() //
				.isPresent();
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
		return accounts.save(account.setPassword(encrypt(password)));
	}

	private EncryptedPassword encrypt(UnencryptedPassword password) {
		return EncryptedPassword.of(passwordEncoder.encode(password.asString()));
	}
}
