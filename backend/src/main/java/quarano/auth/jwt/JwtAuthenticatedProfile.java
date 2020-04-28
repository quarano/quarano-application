package quarano.auth.jwt;

import quarano.auth.Account;
import quarano.auth.RoleType;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class JwtAuthenticatedProfile extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 6843604992995383584L;

	private final Account account;

	public JwtAuthenticatedProfile(JwtToken token, Function<String, Account> account) {
		this(account.apply(token.getUsername()), token.getRoleTypes());
	}

	public JwtAuthenticatedProfile(Account account, List<RoleType> grantedRoleTypes) {

		super(toAuthorities(grantedRoleTypes));

		this.account = account;
	}

	private static Collection<? extends GrantedAuthority> toAuthorities(List<RoleType> roles) {

		return roles.stream() //
				.map(RoleType::getCode) //
				.map(SimpleGrantedAuthority::new) //
				.collect(Collectors.toUnmodifiableList());
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	@Nullable
	public Object getDetails() {
		return account.getTrackedPersonId();
	}

	@Override
	public Object getPrincipal() {
		return account;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

	}

	@Override
	public String getName() {
		return account.getUsername();
	}
}
