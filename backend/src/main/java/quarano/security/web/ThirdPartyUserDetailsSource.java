package quarano.security.web;

import quarano.account.RoleType;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * An {@link AuthenticationUserDetailsService} that will always grant the {@code ROLE_THIRD_PARTY}.
 *
 * @author Oliver Drotbohm
 * @since 1.4
 */
enum ThirdPartyUserDetailsSource
		implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	INSTANCE;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.AuthenticationUserDetailsService#loadUserDetails(org.springframework.security.core.Authentication)
	 */
	@Override
	@SuppressWarnings("null")
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
		return new User(token.getPrincipal().toString(), "¯\\_(ツ)_/¯",
				AuthorityUtils.createAuthorityList(RoleType.ROLE_THIRD_PARTY.getCode()));
	}
}
