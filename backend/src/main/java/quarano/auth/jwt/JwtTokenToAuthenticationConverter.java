package quarano.auth.jwt;

import lombok.RequiredArgsConstructor;
import quarano.auth.RoleType;

import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * An {@link AuthenticationProvider} implementation that creates an authentication based on the validity of a JWT token
 *
 * @author Patrick Otto
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
public class JwtTokenToAuthenticationConverter implements Converter<Jwt, JwtAuthenticatedProfile> {

	private final JwtTokenService jwtService;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public JwtAuthenticatedProfile convert(Jwt source) {

		var token = source.getTokenValue();
		var username = jwtService.getUsernameFromToken(token);
		var personIdentifier = jwtService.getTrackedPersonIdFromToken(token);
		var roles = jwtService.getRolesFromToken(token);
		var grantedRoleTypes = roles.stream() //
				.map(roleName -> RoleType.valueOf(roleName)) //
				.collect(Collectors.toList());

		return new JwtAuthenticatedProfile(username, grantedRoleTypes, personIdentifier);
	}
}
