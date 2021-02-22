package quarano.user.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.Account.PasswordResetRequested;
import quarano.account.AccountService;
import quarano.core.AsyncTransactionalEventListener;
import quarano.core.CoreProperties;
import quarano.core.EmailSender;
import quarano.core.EmailTemplates.Keys;
import quarano.tracking.TrackedPersonEmailFactory;

import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

/**
 * Event listener to send out password reset emails.
 *
 * @author Oliver Drotbohm
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordResetEmailEventListener {

	private final EmailSender emails;
	private final AccountService accounts;
	private final TrackedPersonEmailFactory emailFactory;
	private final CoreProperties properties;

	@AsyncTransactionalEventListener
	void on(PasswordResetRequested event) {

		var account = accounts.findById(event.getAccountId()).orElseThrow();

		var url = new UriTemplate(UserLinkRelations.PASSWORD_RESET_URI_TEMPLATE)
				.expand(account.getPasswordResetToken().getToken()).toASCIIString();

		var parameters = new HashMap<String, Object>();
		parameters.put("fullName", account.getFullName());
		parameters.put("resetUrl", properties.getHostRelativeUrl(url));

		var email = emailFactory.getEmailFor(account, "PasswordReset.subject", Keys.RESET_PASSWORD, parameters);

		log.info("Sending out password reset email to {}.", email.getTo());

		emails.sendMail(email);
	}
}
