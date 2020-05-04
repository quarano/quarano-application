package quarano.department;

import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.AccountService;
import quarano.account.Department.DepartmentIdentifier;
import quarano.account.Password.UnencryptedPassword;
import quarano.department.activation.ActivationCode;
import quarano.department.activation.ActivationCodeService;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;
import quarano.tracking.TrackedPersonRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
public class RegistrationManagement {

	private final @NonNull AccountService accounts;
	private final @NonNull TrackedPersonRepository trackedPeople;
	private final @NonNull ActivationCodeService activationCodes;

	public Try<ActivationCode> initiateRegistration(TrackedCase trackedCase) {

		var departmentId = trackedCase.getDepartment().getId();
		var personId = trackedCase.getTrackedPerson().getId();

		return activationCodes.createActivationCode(personId, departmentId) //
				.andThen(() -> trackedCase.markInRegistration());
	}

	public Optional<ActivationCode> getPendingActivationCode(TrackedPersonIdentifier id) {
		return activationCodes.getPendingActivationCode(id);
	}

	public Try<Account> createTrackedPersonAccount(RegistrationDetails details) {

		return Try.success(details) //
				.filter(it -> isUsernameAvailable(it.getUsername()), RegistrationException::forInvalidUsername)
				.flatMapTry(it -> activationCodes.redeemCode(it.getActivationCodeIdentifier()).map(it::apply)) //
				.flatMap(this::checkIdentity) //
				.flatMap(this::applyTrackedPerson) //
				.map(this::toAccount);
	}

	public boolean isUsernameAvailable(String userName) {
		return accounts.isUsernameAvailable(userName);
	}

	private Try<RegistrationDetails> applyTrackedPerson(RegistrationDetails details) {

		return trackedPeople.findById(details.getTrackedPersonId()) //
				.map(person -> details.apply(person)) //
				.orElseGet(() -> Try.failure(new RegistrationException("No tracked person found!")));
	}

	/**
	 * Checks the identity of the registering user by comparing the provided date of birth with the data stored by the
	 * health department during case creation
	 *
	 * @param details
	 * @param trackedPersonId
	 */
	private Try<RegistrationDetails> checkIdentity(RegistrationDetails details) {

		return trackedPeople.findById(details.getTrackedPersonId()).map(Try::success) //
				.orElseGet(() -> Try.failure(new RegistrationException(
						"No tracked person found that belongs to activation code '" + details.getActivationCodeLiteral() + "'")))
				.filter(person -> person.hasBirthdayOf(details.getDateOfBirth()),
						() -> RegistrationException.forInvalidBirthDay(details)) //
				.map(__ -> details);
	}

	private Account toAccount(RegistrationDetails details) {

		var account = accounts.createTrackedPersonAccount(details.getUsername(), details.getUnencryptedPassword(),
				details.getFirstname(), details.getLastname(), details.getDepartmentId());

		trackedPeople.findById(details.getTrackedPersonId()) //
				.map(it -> it.markAccountRegistration(account, LocalDateTime.now())) //
				.map(trackedPeople::save);

		return account;
	}

	/**
	 * @param username
	 * @param password
	 * @param firstname
	 * @param lastname
	 * @param departmentIdDep1
	 * @param validTrackedPerson2IdDep1
	 */
	Account createTrackedPersonAccount(String username, UnencryptedPassword password, String firstname, String lastname,
			DepartmentIdentifier departmentIdDep1, TrackedPersonIdentifier validTrackedPerson2IdDep1) {

		var details = new RegistrationDetails() //
				.setUsername(username) //
				.setUnencryptedPassword(password) //
				.setFirstname(firstname) //
				.setLastname(lastname) //
				.setDepartmentId(departmentIdDep1) //
				.setTrackedPersonId(validTrackedPerson2IdDep1);

		return toAccount(details);
	}
}
