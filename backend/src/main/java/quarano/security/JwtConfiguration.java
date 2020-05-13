package quarano.security;

import quarano.account.Account;

import java.util.List;
import java.util.function.Function;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

/**
 * @author Oliver Drotbohm
 */
public interface JwtConfiguration {

	JwtDecoder getJwtDecoder();

	Converter<Jwt, ? extends AbstractAuthenticationToken> getJwtConverter(Function<String, Account> accountSource);

	List<String> getAllowedOrigins();
}
