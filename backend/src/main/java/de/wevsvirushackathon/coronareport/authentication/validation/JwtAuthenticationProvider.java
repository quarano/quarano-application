package de.wevsvirushackathon.coronareport.authentication.validation;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import de.wevsvirushackathon.coronareport.authentication.RoleType;
import io.jsonwebtoken.JwtException;

/**
 * An {@link AuthenticationProvider} implementation that creates an
 * authentication based on the validity of a JWT token
 * 
 * @author Patrick Otto
 *
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

	private final JwtTokenService jwtService;

	public JwtAuthenticationProvider() {
		this(null);
	}

	@Autowired
	public JwtAuthenticationProvider(JwtTokenService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		try {
			String token = (String) authentication.getCredentials();
			String username = jwtService.getUsernameFromToken(token);
			Long clientId = jwtService.getClientIdFromToken(token);
			List<String> roles = jwtService.getRolesFromToken(token);
			List<RoleType> grantedRoleTypes = roles.stream().map(roleName -> RoleType.valueOf(roleName))
					.collect(Collectors.toList());

			return jwtService.validateToken(token)
					.map(aBoolean -> new JwtAuthenticatedProfile(username, grantedRoleTypes, clientId))
					.orElseThrow(() -> new JwtAuthenticationException("JWT Token validation failed"));

		} catch (JwtException ex) {
			log.error(String.format("Invalid JWT Token: %s", ex.getMessage()));
			throw new JwtAuthenticationException("Failed to verify token");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthentication.class.equals(authentication);
	}
}