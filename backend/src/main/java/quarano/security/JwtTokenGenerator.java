package quarano.security;

import quarano.account.Account;

/**
 * @author Oliver Drotbohm
 */
public interface JwtTokenGenerator {

	String generateTokenFor(Account account);
}
