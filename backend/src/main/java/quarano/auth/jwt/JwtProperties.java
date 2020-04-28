/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import lombok.RequiredArgsConstructor;
import quarano.auth.Account;
import quarano.auth.Role;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
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
class JwtProperties implements JwtTokenGenerator {

	static final String ROLE_CLAIM = "aut";
	static final String PERSON_CLAIM = "pid";

	private final String secret;
	private final Duration expiration;

	/*
	 * (non-Javadoc)
	 * @see quarano.auth.jwt.JwtTokenGenerator#generateTokenFor(quarano.auth.Account)
	 */
	public String generateTokenFor(Account account) {

		return Jwts.builder() //
				.setClaims(toClaims(account)) //
				.setSubject(account.getUsername()) //
				.setIssuedAt(new Date()) //
				.setExpiration(getExpirationDate()) //
				.signWith(SignatureAlgorithm.HS512, secret) //
				.compact();
	}

	JwtDecoder getJwtDecoder() {

		var decoded = TextCodec.BASE64.decode(secret);
		var key = new SecretKeySpec(decoded, SignatureAlgorithm.HS512.getJcaName());

		return NimbusJwtDecoder.withSecretKey(key) //
				.macAlgorithm(MacAlgorithm.HS512) //
				.build();
	}

	Converter<Jwt, ? extends AbstractAuthenticationToken> getJwtConverter(Function<String, Account> accountSource) {
		return jwt -> new JwtAuthenticatedProfile(createToken(jwt.getTokenValue()), accountSource);
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
		var roles = account.getRoles().stream().map(Role::toString).collect(Collectors.toList());
		var identifier = account.getTrackedPersonId();

		claims.put(ROLE_CLAIM, roles);

		if (identifier != null) {
			claims.put(PERSON_CLAIM, identifier.toString());
		}

		return claims;
	}

	private Date getExpirationDate() {

		var expirationDate = LocalDateTime.now().plus(expiration);
		return LocalDateTimeToDateConverter.INSTANCE.convert(expirationDate);
	}
}
