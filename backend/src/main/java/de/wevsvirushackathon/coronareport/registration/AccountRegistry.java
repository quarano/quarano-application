package de.wevsvirushackathon.coronareport.registration;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.wevsvirushackathon.coronareport.authentication.Account;
import de.wevsvirushackathon.coronareport.authentication.AccountService;
import de.wevsvirushackathon.coronareport.authentication.RoleType;
import de.wevsvirushackathon.coronareport.registration.ActivationCode.ActivationCodeIdentifier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountRegistry {

	private final @NonNull ActivationCodeService activationCodeService;
	private final @NonNull AccountService accountService;

	@Transactional
	public void registerAccountForClient(AccountRegistrationDetails details)
			throws CodeNotFoundException, ActivationCodeExpiredException, ActivationNotActiveException {

		// validate username
		// #TODO

		ActivationCode code = activationCodeService.redeemCode(ActivationCodeIdentifier.of(UUID.fromString(details.getClientCode())));
		
		// get client that belongs to the registration
		details.setClientId(code.getClientId());

		createAndStoreAccountFrom(details);

	}

	private Account createAndStoreAccountFrom(AccountRegistrationDetails details) {

		return accountService.createAndStoreAccount(details.getUsername(), details.getUnencryptedPassword(),
				details.getFirstname(), details.getLastmname(), details.getDepartmentId(), details.getClientId(),
				RoleType.ROLE_USER);

	}
}
