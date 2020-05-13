package quarano.security;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import quarano.account.RoleType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
class JwtToken {

	private final Claims claims;

	@SuppressWarnings("unchecked")
	public List<RoleType> getRoleTypes() {

		List<String> roles = claims.get(JwtProperties.ROLE_CLAIM, List.class);

		return roles.stream() //
				.map(RoleType::valueOf) //
				.collect(Collectors.toUnmodifiableList());
	}

	public String getUsername() {
		return claims.getSubject();
	}
}
