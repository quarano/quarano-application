package quarano.auth.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import quarano.auth.RoleType;

public class JwtAuthenticatedProfile implements Authentication {

    private final String username;
    private final Long clientId;
    
    private List<RoleType> roles = new ArrayList<>();
    
	public JwtAuthenticatedProfile(String username, List<RoleType> grantedRoleTypes, Long clientId) {
		this.username = username;
		this.roles = grantedRoleTypes;
		this.clientId = clientId;
	}    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    	for(RoleType role: roles) {
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
        return this.clientId;
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