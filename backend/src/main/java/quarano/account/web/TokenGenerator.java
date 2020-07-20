package quarano.account.web;

import quarano.account.Account;
import quarano.core.web.QuaranoHttpHeaders;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

/**
 * A component that can generate authentication tokens for {@link Account}s.
 *
 * @author Oliver Drotbohm
 * @author Jan Stamer
 */
public interface TokenGenerator {

	/**
	 * Returns an authentication token for the given account.
	 *
	 * @param account must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	String generateTokenFor(Account account);

	default HttpEntity<?> toTokenResponse(Account account) {

		return ResponseEntity.ok()
				.header(QuaranoHttpHeaders.AUTH_TOKEN, generateTokenFor(account))
				.body(new AccountRepresentation(account));
	}
}
