package quarano.department;

import quarano.account.Account;

/**
 * @author Oliver Drotbohm
 */
public interface TokenGenerator {

	String generateTokenFor(Account account);
}
