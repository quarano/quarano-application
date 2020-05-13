package quarano.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.Role;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.Jsr310Converters.LocalDateTimeToDateConverter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * @author Oliver Drotbohm
 */
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "quarano.jwt")
class JwtProperties implements JwtTokenGenerator, JwtConfiguration {

	static final String ROLE_CLAIM = "aut";
	static final String PERSON_CLAIM = "pid";

	private final String secret;
	private final Duration expiration;
	private final List<String> allowedOrigins;

	/*
	 * (non-Javadoc)
	 * @see quarano.auth.jwt.JwtTokenGenerator#generateTokenFor(quarano.auth.Account)
	 */
	@Override
	public String generateTokenFor(Account account) {

		return Jwts.builder() //
				.setClaims(toClaims(account)) //
				.setSubject(account.getUsername()) //
				.setIssuedAt(new Date()) //
				.setExpiration(getExpirationDate()) //
				.signWith(SignatureAlgorithm.HS512, secret) //
				.compact();
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.security.JwtConfiguration#getJwtDecoder()
	 */
	@Override
	public JwtDecoder getJwtDecoder() {

		var decoded = TextCodec.BASE64.decode(secret);
		var key = new SecretKeySpec(decoded, SignatureAlgorithm.HS512.getJcaName());

		return NimbusJwtDecoder.withSecretKey(key) //
				.macAlgorithm(MacAlgorithm.HS512) //
				.build();
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.security.JwtConfiguration#getJwtConverter(java.util.function.Function)
	 */
	@Override
	public Converter<Jwt, ? extends AbstractAuthenticationToken> getJwtConverter(
			Function<String, Account> accountSource) {
		return jwt -> new JwtAuthenticatedProfile(createToken(jwt.getTokenValue()), accountSource);
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.security.JwtConfiguration#getAllowedOrigins()
	 */
	@Override
	public List<String> getAllowedOrigins() {
		return allowedOrigins != null ? allowedOrigins : List.of("*");
	}

	JwtToken createToken(String source) {

		Claims claims = Jwts.parser() //
				.setSigningKey(secret) //
				.parseClaimsJws(source) //
				.getBody();

		return JwtToken.of(claims);
	}

	private Map<String, Object> toClaims(Account account) {

		var claims = new HashMap<String, Object>();
		var roles = account.getRoles().stream() //
				.map(Role::toString) //
				.collect(Collectors.toUnmodifiableList());

		claims.put(ROLE_CLAIM, roles);

		return claims;
	}

	private Date getExpirationDate() {

		var expirationDate = LocalDateTime.now().plus(expiration);
		return LocalDateTimeToDateConverter.INSTANCE.convert(expirationDate);
	}
}
