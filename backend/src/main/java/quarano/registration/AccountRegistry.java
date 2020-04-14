package quarano.registration;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.wevsvirushackathon.coronareport.infrastructure.errorhandling.InconsistentDataException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.auth.Account;
import quarano.auth.AccountService;
import quarano.auth.RoleType;
import quarano.registration.ActivationCode.ActivationCodeIdentifier;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;
import quarano.tracking.TrackedPersonRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountRegistry {

	private final @NonNull ActivationCodeService activationCodeService;
	private final @NonNull AccountService accountService;
	private final @NonNull TrackedPersonRepository trackedPersonRepo;

	@Transactional
	public void registerAccountForClient(AccountRegistrationDetails details)
			throws CodeNotFoundException, ActivationCodeExpiredException, ActivationNotActiveException,
			InconsistentDataException, InvalidAuthentificationDataException {

		// validate username
		// #TODO

		ActivationCode code = activationCodeService
				.redeemCode(ActivationCodeIdentifier.of(details.getActivationCodeLiteral()));

		// get Tracked person and check identity
		checkIdentity(details, code.getTrackedPersonId());

		// get client that belongs to the registration
		details.setTrackedPersonId(code.getTrackedPersonId());

		createAndStoreAccountFrom(details);

	}

	/**
	 * Checks the identitiy of the registering user by comparing the provided date
	 * of birth with the data stored by the health department during case creation
	 * 
	 * @param details
	 * @param trackedPersonId
	 * @throws InconsistentDataException
	 * @throws InvalidAuthentificationDataException
	 */
	private void checkIdentity(AccountRegistrationDetails details, TrackedPersonIdentifier trackedPersonId)
			throws InconsistentDataException, InvalidAuthentificationDataException {

		TrackedPerson person = this.trackedPersonRepo.findById(trackedPersonId).orElseThrow(
				() -> new InconsistentDataException("No tracked person found that belongs to activation code '"
						+ details.getActivationCodeLiteral() + "'"));

		// check if given date of registering user matches date stored by health
		// department during case creation
		if (!person.getDateOfBirth().equals(details.getDateOfBirth())) {
			throw new InvalidAuthentificationDataException("Given date of birth does not match date of birth of case");
		}
	}

	private Account createAndStoreAccountFrom(AccountRegistrationDetails details) {

		return accountService.createAndStoreAccount(details.getUsername(), details.getUnencryptedPassword(),
				details.getFirstname(), details.getLastname(), details.getDepartmentId(), details.getTrackedPersonId(),
				RoleType.ROLE_USER);

	}
}
