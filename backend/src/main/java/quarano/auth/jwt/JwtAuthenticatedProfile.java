package quarano.auth.jwt;

import quarano.auth.RoleType;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtAuthenticatedProfile extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 6843604992995383584L;

	private final String username;
	private final TrackedPersonIdentifier trackedPersonId;

	public JwtAuthenticatedProfile(String username, List<RoleType> grantedRoleTypes,
			TrackedPersonIdentifier trackedPersonId) {

		super(toAuthorities(grantedRoleTypes));

		this.username = username;
		this.trackedPersonId = trackedPersonId;
	}

	private static Collection<? extends GrantedAuthority> toAuthorities(List<RoleType> roles) {

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (RoleType role : roles) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getCode()));
		}
		return grantedAuthorities;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		return this.trackedPersonId;
	}

	@Override
	public Object getPrincipal() {
		return username;
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
		return username;
	}
}
