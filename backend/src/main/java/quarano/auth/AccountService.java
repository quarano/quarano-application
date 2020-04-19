package quarano.auth;

import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.auth.Password.EncryptedPassword;
import quarano.auth.Password.UnencryptedPassword;
import quarano.department.Department.DepartmentIdentifier;
import quarano.registration.ActivationCodeService;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;
import quarano.tracking.TrackedPersonRepository;

import java.util.Optional;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountService {

	private static final String INVALID_BIRTHDAY = "Given date of birth does not match date of birth of case!";
	private final @NonNull PasswordEncoder passwordEncoder;
	private final @NonNull AccountRepository accounts;
	private final @NonNull RoleRepository roles;
	private final @NonNull TrackedPersonRepository trackedPersonRepo;
	private final @NonNull ActivationCodeService activationCodes;

	/**
	 * creates a new account, encrypts the password and stores it
	 *
	 * @param username
	 * @param password
	 * @param firstname
	 * @param lastename
	 * @param departmentId
	 * @param clientId
	 * @param roleType
	 * @return
	 */
	public Account createAccount(String username, UnencryptedPassword password, String firstname, String lastename,
			DepartmentIdentifier departmentId, TrackedPersonIdentifier trackedPersonId, RoleType roleType) {

		var encryptedPassword = EncryptedPassword.of(passwordEncoder.encode(password.asString()));
		var role = roles.findByName(roleType.toString());
		var account = accounts
				.save(new Account(username, encryptedPassword, firstname, lastename, departmentId, trackedPersonId, role));

		log.info("Created account for client " + trackedPersonId + " with username " + username);

		return account;
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

	@Transactional
	public Try<Account> registerAccountForClient(AccountRegistrationDetails details) {

		return Try.success(details) //
				.filter(it -> isUsernameAvailable(it.getUsername()),
						() -> new AccountRegistrationException("Username " + details.getUsername() + " already taken!"))
				.flatMapTry(it -> activationCodes.redeemCode(it.getActivationCodeIdentifier()).map(it::apply)) //
				.flatMap(this::checkIdentity) //
				.flatMap(this::applyTrackedPerson) //
				.map(this::toAccount);
	}

	public boolean matches(@Nullable UnencryptedPassword candidate, EncryptedPassword existing) {

		Assert.notNull(existing, "Existing password must not be null!");

		return Optional.ofNullable(candidate).//
				map(c -> passwordEncoder.matches(c.asString(), existing.asString())).//
				orElse(false);
	}

	private Try<AccountRegistrationDetails> applyTrackedPerson(AccountRegistrationDetails details) {

		var identifier = details.getTrackedPersonId();

		if (!identifier.isPresent()) {
			return Try.success(details);
		}

		return identifier.flatMap(trackedPersonRepo::findById) //
				.map(person -> details.apply(person)) //
				.orElseGet(() -> Try.failure(new AccountRegistrationException("No person found!")));
	}

	/**
	 * Checks the identitiy of the registering user by comparing the provided date of birth with the data stored by the
	 * health department during case creation
	 *
	 * @param details
	 * @param trackedPersonId
	 */
	private Try<AccountRegistrationDetails> checkIdentity(AccountRegistrationDetails details) {

		var identifier = details.getTrackedPersonId();

		if (!identifier.isPresent()) {
			return Try.success(details);
		}

		return identifier.flatMap(trackedPersonRepo::findById) //
				.map(Try::success) //
				.orElseGet(() -> Try.failure(new AccountRegistrationException(
						"No tracked person found that belongs to activation code '" + details.getActivationCodeLiteral() + "'")))
				.filter(person -> person.hasBirthdayOf(details.getDateOfBirth()),
						() -> new AccountRegistrationException(INVALID_BIRTHDAY)) //
				.map(__ -> details);
	}

	private Account toAccount(AccountRegistrationDetails details) {

		return createAccount(details.getUsername(), details.getUnencryptedPassword(), details.getFirstname(),
				details.getLastname(), details.getDepartmentId(), details.getTrackedPersonId().orElse(null),
				RoleType.ROLE_USER);
	}
}
