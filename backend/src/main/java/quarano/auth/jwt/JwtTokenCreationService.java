package quarano.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import quarano.auth.Account;
import quarano.auth.Role;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * A class to create JWT token holding quarano specific user information
 *
 * @author Patrick Otto
 */
@Component
class JwtTokenCreationService {

	private String secret;

	private Long expiration;
	private String roleClaimAttribute;
	private String trackedPersonIdClaimAttribute;

	public JwtTokenCreationService(@Value("${jwt.authentication" + ".secret}") String secret,
			@Value("${jwt.provider.expiration}") Long expiration,
			@Value("${jwt.authentication.claim.role}") String roleClaimAttribute,
			@Value("${jwt.authentication.claim.trackedpersonid}") String trackedPersonIdClaimAttribute) {
		this.secret = secret;
		this.expiration = expiration;
		this.roleClaimAttribute = roleClaimAttribute;
		this.trackedPersonIdClaimAttribute = trackedPersonIdClaimAttribute;
	}

	public String generateToken(Account it) {

		String username = it.getUsername();
		List<Role> roles = it.getRoles();
		TrackedPersonIdentifier trackedPersonId = it.getTrackedPersonId();

		final Date createdDate = new Date();
		final Date expirationDate = calculateExpirationDate(createdDate);

		// map roles to a list of rolenames
		Map<String, Object> claims = new HashMap<>();
		claims.put(roleClaimAttribute, roles.stream().map(Role::toString).collect(Collectors.toList()));
		if (trackedPersonId != null) {
			claims.put(trackedPersonIdClaimAttribute, trackedPersonId.toString());
		}

		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(createdDate).setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	private Date calculateExpirationDate(Date createdDate) {
		return new Date(createdDate.getTime() + expiration * 1000);
	}
}
