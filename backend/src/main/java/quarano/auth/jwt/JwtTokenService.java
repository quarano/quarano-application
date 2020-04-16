package quarano.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Retriev standard and application specific information from a JWT token created by quarano application
 *
 * @author Patrick Otto
 */
@Component
class JwtTokenService {
	private String secret;
	private String roleClaimAttribute;
	private String trackedPersonIdClaimAttribute;

	@Autowired
	public JwtTokenService(@Value("${jwt.authentication.secret}") String secret,
			@Value("${jwt.authentication.claim.role}") String roleClaimAttribute,
			@Value("${jwt.authentication.claim.trackedpersonid}") String trackedPersonIdClaimAttribute) {
		this.secret = secret;
		this.roleClaimAttribute = roleClaimAttribute;
		this.trackedPersonIdClaimAttribute = trackedPersonIdClaimAttribute;
	}

	/**
	 * Retrieves the clientId from the claims of the token
	 *
	 * @param token
	 * @return
	 */
	@Nullable
	public TrackedPersonIdentifier getTrackedPersonIdFromToken(String token) {

		var claims = getAllClaimsFromToken(token);
		var idClaim = claims.get(trackedPersonIdClaimAttribute, String.class);

		return idClaim == null ? null : TrackedPersonIdentifier.of(UUID.fromString(idClaim));
	}

	/**
	 * Retrieves the username from the claims of the token
	 *
	 * @param token
	 * @return
	 */
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	/**
	 * Retrieve the roles of the user in String represantation
	 *
	 * @param token
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getRolesFromToken(String token) {

		var claims = getAllClaimsFromToken(token);

		return claims.get(roleClaimAttribute, List.class);
	}

	/**
	 * Retrieve the Expiration date of the token, using the standard expiration attribute of JWT
	 *
	 * @param token
	 * @return
	 */
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	/**
	 * Checks if the token is not expired Returns true if the token is not expired
	 *
	 * @param token
	 * @return
	 */
	private Boolean isTokenNotExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.after(new Date());
	}

	/**
	 * Checks if the token is valid by checking if it is expired
	 *
	 * @param token
	 * @return
	 */
	public Optional<Boolean> validateToken(String token) {
		return isTokenNotExpired(token) ? Optional.of(Boolean.TRUE) : Optional.empty();
	}

}
