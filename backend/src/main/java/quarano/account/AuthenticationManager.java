package quarano.account;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Access to the currently logged in {@link Account} and {@link RoleType}s.
 *
 * @author Oliver Drotbohm
 */
public interface AuthenticationManager {

	Optional<Account> getCurrentUser();

	/**
	 * Returns whether the currently authenticated principal has the given {@link RoleType}.
	 *
	 * @param role must not be {@literal null}.
	 * @return
	 * @since 1.4
	 */
	boolean hasRole(RoleType role);

	/**
	 * Returns whether the current authentication is anonymous or the authenticated principal has a role matching the
	 * given {@link Predicate}.
	 *
	 * @param predicate must not be {@literal null}.
	 * @return
	 * @since 1.4
	 */
	boolean isAnonymousOrHasRoleMatching(Predicate<RoleType> predicate);
}
