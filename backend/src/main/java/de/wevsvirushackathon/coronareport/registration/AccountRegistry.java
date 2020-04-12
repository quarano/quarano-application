package de.wevsvirushackathon.coronareport.registration;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.wevsvirushackathon.coronareport.authentication.Account;
import de.wevsvirushackathon.coronareport.authentication.AccountService;
import de.wevsvirushackathon.coronareport.authentication.RoleType;
import de.wevsvirushackathon.coronareport.client.Client;
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
	public Client registerAccountForClient(Client client, AccountRegistrationDetails details) {

		// validate registration
		// #TODO
		
		activationCodeService.useCode(details.getClientCode(), details.getClientId());

		createAndStoreAccountFrom(details);

		// mark registration completed in client
		client.setRegistrationTimestamp(new Timestamp(System.currentTimeMillis()));

		return client;

	}

	private Account createAndStoreAccountFrom(AccountRegistrationDetails details) {

		return accountService.createAndStoreAccount(details.getUsername(), details.getUnencryptedPassword(),
				details.getFirstname(), details.getLastmname(), details.getDepartmentId(), details.getClientId(),
				RoleType.ROLE_USER);

	}
}
