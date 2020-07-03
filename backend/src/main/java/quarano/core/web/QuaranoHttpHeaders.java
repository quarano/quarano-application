package quarano.core.web;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

/**
 * Defines application specific HTTP headers.
 *
 * @author Jan Stamer
 * @author Oliver Drotbohm
 */
public interface QuaranoHttpHeaders {

	/**
	 * Header for JWT Authorization Token.
	 */
	String AUTH_TOKEN = "X-Auth-Token";

	/**
	 * Creates a new response {@link HttpEntity} with the given authentication token.
	 *
	 * @param token must not be {@literal null} or empty.
	 * @return
	 */
	public static HttpEntity<?> toTokenResponse(String token) {

		Assert.hasText(token, "Token must not be null or empty!");

		return ResponseEntity.noContent() //
				.header(AUTH_TOKEN, token) //
				.build();
	}
}
