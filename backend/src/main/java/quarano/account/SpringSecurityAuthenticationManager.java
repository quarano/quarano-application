package quarano.account;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Spring Security based implementation of {@link AuthenticationManager}.
 *
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class SpringSecurityAuthenticationManager implements AuthenticationManager {

	/*
	 * (non-Javadoc)
	 * @see quarano.auth.AuthenticationManager#getCurrentUser()
	 */
	@Override
	public Optional<Account> getCurrentUser() {

		return Optional.ofNullable(SecurityContextHolder.getContext()
				.getAuthentication())
				.map(Authentication::getPrincipal)
				.filter(Account.class::isInstance)
				.map(Account.class::cast);
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.account.AuthenticationManager#hasRole(quarano.account.RoleType)
	 */
	@Override
	public boolean hasRole(RoleType role) {

		Assert.notNull(role, "Role type must not be null!");

		return getCurrentRoles().anyMatch(role::equals);
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.account.AuthenticationManager#isAnonymousOrHasRoleMatching(java.util.function.Predicate)
	 */
	@Override
	public boolean isAnonymousOrHasRoleMatching(Predicate<RoleType> predicate) {

		Assert.notNull(predicate, "Predicate must not be null!");

		return isAnonymous() || getCurrentRoles().anyMatch(predicate);
	}

	/**
	 * Returns all {@link RoleType}s held by the current {@link Authentication}.
	 *
	 * @return will never be {@literal null}.
	 */
	private static Stream<RoleType> getCurrentRoles() {

		return Optional.ofNullable(SecurityContextHolder.getContext()
				.getAuthentication())
				.stream()
				.flatMap(it -> it.getAuthorities().stream())
				.map(GrantedAuthority::getAuthority)
				.filter(RoleType::isRoleType)
				.map(RoleType::valueOf);
	}

	/**
	 * Returns whether the current {@link Authentication} is considered anonymous. That is if either no
	 * {@link Authentication} exists at all or it contains a {@code ROLE_ANONYMOUS} granted authority.
	 *
	 * @return
	 */
	private static boolean isAnonymous() {

		var authentication = SecurityContextHolder.getContext().getAuthentication();

		return authentication == null
				|| authentication.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority)
						.anyMatch("ROLE_ANONYMOUS"::equals);
	}
}
