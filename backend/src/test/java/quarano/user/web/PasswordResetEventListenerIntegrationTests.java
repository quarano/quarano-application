package quarano.user.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.account.Account.PasswordResetRequested;
import quarano.account.AccountService;
import quarano.core.EmailAddress;
import quarano.util.TestEmailServer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.moduliths.test.PublishedEvents;
import org.moduliths.test.PublishedEventsExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link PasswordResetEventListener}.
 *
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoIntegrationTest
@ExtendWith(PublishedEventsExtension.class)
class PasswordResetEventListenerIntegrationTests {

	private final AccountService accounts;
	private final TestEmailServer mailServer;

	@Test // CORE-92
	@DirtiesContext // As we need to change the state of an account
	@Transactional(propagation = Propagation.NOT_SUPPORTED) // As the event listener is triggered on commit
	void sendsEmail(PublishedEvents events) throws Exception {

		var username = "DemoAccount";
		var token = accounts.requestPasswordReset(EmailAddress.of("markus.hanser@testtest.de"), username).orElseThrow();
		var account = accounts.findByUsername(username).orElseThrow();

		// Event published for the account the reset was requested for
		assertThat(events.ofType(PasswordResetRequested.class)
				.matching(it -> it.getAccountId().equals(account.getId())))
						.hasSize(1);

		mailServer.assertEmailSentWithBody(it -> it.contains(token.getToken().toString()));
	}
}
