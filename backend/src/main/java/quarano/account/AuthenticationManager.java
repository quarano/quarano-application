package quarano.account;

import java.util.Optional;

/**
 * @author Oliver Drotbohm
 */
public interface AuthenticationManager {
	Optional<Account> getCurrentUser();
}
