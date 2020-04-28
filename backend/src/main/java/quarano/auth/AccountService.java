package quarano.auth;

import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.auth.Password.EncryptedPassword;
import quarano.auth.Password.UnencryptedPassword;
import quarano.department.Department.DepartmentIdentifier;
import quarano.department.TrackedCase;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;
import quarano.tracking.TrackedPersonRepository;

import java.util.Optional;

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
	private final @NonNull TrackedPersonRepository trackedPeople;
	private final @NonNull ActivationCodeService activationCodes;
	private final @NonNull AccountProperties configuration;

	public Try<Account> createTrackedPersonAccount(AccountRegistrationDetails details) {

		return Try.success(details) //
				.filter(it -> isUsernameAvailable(it.getUsername()), AccountRegistrationException::forInvalidUsername)
				.flatMapTry(it -> activationCodes.redeemCode(it.getActivationCodeIdentifier()).map(it::apply)) //
				.flatMap(this::checkIdentity) //
				.flatMap(this::applyTrackedPerson) //
				.map(this::toAccount);
	}

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
	Account createTrackedPersonAccount(String username, UnencryptedPassword password, String firstname, String lastname,
			DepartmentIdentifier departmentId, TrackedPersonIdentifier trackedPersonId) {

		var encryptedPassword = EncryptedPassword.of(passwordEncoder.encode(password.asString()));
		var role = roles.findByName(RoleType.ROLE_USER.toString());
		var account = accounts
				.save(new Account(username, encryptedPassword, firstname, lastname, departmentId, trackedPersonId, role));

		// Mark registration on TrackedPerson
		trackedPeople.findById(trackedPersonId).map(it -> it.markAccountRegistration(configuration.getRegistrationDate())) //
				.map(trackedPeople::save);

		log.info("Created account for tracked person " + trackedPersonId + " with username " + username);

		return account;
	}

	public Account createStaffAccount(String username, UnencryptedPassword password, String firstname, String lastname,
			DepartmentIdentifier departmentId, RoleType roleType) {

		var encryptedPassword = EncryptedPassword.of(passwordEncoder.encode(password.asString()));
		var role = roles.findByName(roleType.toString());
		var account = accounts
				.save(new Account(username, encryptedPassword, firstname, lastname, departmentId, null, role));

		log.info("Created staff account for " + username);

		return account;
	}

	public boolean isValidUsername(String candidate) {

		if (!isUsernameAvailable(candidate)) {
			return false;
		}
		// check username pattern
		return true;
	}

	public Optional<Account> findByCase(TrackedCase trackedCase) {
		return accounts.findByTrackedPersonId(trackedCase.getTrackedPerson().getId());
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

	private Try<AccountRegistrationDetails> applyTrackedPerson(AccountRegistrationDetails details) {

		return trackedPeople.findById(details.getTrackedPersonId()) //
				.map(person -> details.apply(person)) //
				.orElseGet(() -> Try.failure(new AccountRegistrationException("No tracked person found!")));
	}

	/**
	 * Checks the identitiy of the registering user by comparing the provided date of birth with the data stored by the
	 * health department during case creation
	 *
	 * @param details
	 * @param trackedPersonId
	 */
	private Try<AccountRegistrationDetails> checkIdentity(AccountRegistrationDetails details) {

		return trackedPeople.findById(details.getTrackedPersonId()).map(Try::success) //
				.orElseGet(() -> Try.failure(new AccountRegistrationException(
						"No tracked person found that belongs to activation code '" + details.getActivationCodeLiteral() + "'")))
				.filter(person -> person.hasBirthdayOf(details.getDateOfBirth()),
						() -> AccountRegistrationException.forInvalidBirthDay(details)) //
				.map(__ -> details);
	}

	private Account toAccount(AccountRegistrationDetails details) {

		return createTrackedPersonAccount(details.getUsername(), details.getUnencryptedPassword(), details.getFirstname(),
				details.getLastname(), details.getDepartmentId(), details.getTrackedPersonId());
	}
}
