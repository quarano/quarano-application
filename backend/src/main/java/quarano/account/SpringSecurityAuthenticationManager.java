package quarano.account;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
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

		return Optional.ofNullable(SecurityContextHolder.getContext() //
				.getAuthentication()) //
				.map(Authentication::getPrincipal) //
				.filter(Account.class::isInstance) //
				.map(Account.class::cast);
	}
}
