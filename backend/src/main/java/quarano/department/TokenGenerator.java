package quarano.department;

import quarano.account.Account;

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
}
