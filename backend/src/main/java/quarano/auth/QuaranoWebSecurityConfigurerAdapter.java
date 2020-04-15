package quarano.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import quarano.auth.jwt.JwtTokenToAuthenticationConverter;

import java.util.Arrays;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configures security settings and defines which url pattern can be accessed by which role
 *
 * @author Patrick Otto
 * @author Oliver Drotbohm
 */
@EnableWebSecurity
public class QuaranoWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

	private final Log logger = LogFactory.getLog(QuaranoWebSecurityConfigurerAdapter.class);

	@Autowired JwtTokenToAuthenticationConverter jwtAuthenticationProvider;

	private static final String[] SWAGGER_UI_WHITELIST = {

			// -- swagger ui
			"/swagger-resources/**", //
			"/swagger-ui.html", //
			"/v2/api-docs", //
			"/webjars/**" };

	@Bean
	public JwtDecoder jwtDecoder(@Value("${jwt.authentication.secret}") String secret) {

		var decoded = TextCodec.BASE64.decode(secret);
		var key = new SecretKeySpec(decoded, SignatureAlgorithm.HS512.getJcaName());

		return NimbusJwtDecoder.withSecretKey(key) //
				.macAlgorithm(MacAlgorithm.HS512) //
				.build();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		logger.debug(
				"Configuring HTTP security, allowing public access to '/login', 'clinet/register' and 'swagger-ui.html'");

		httpSecurity.oauth2ResourceServer() //
				.jwt(it -> it.jwtAuthenticationConverter(jwtAuthenticationProvider));

		httpSecurity.authorizeRequests(it -> {
			it.mvcMatchers(SWAGGER_UI_WHITELIST).permitAll();
			it.mvcMatchers("/login").permitAll();
			it.mvcMatchers("/api/registration").permitAll();
			it.mvcMatchers("/api/registration/checkcode/**").permitAll(); //
			it.mvcMatchers("/api/registration/checkusername/**").permitAll(); //
			it.mvcMatchers("/api/user/me").authenticated(); //
			it.mvcMatchers("/**").access("hasRole('" + RoleType.ROLE_USER + "')"); //
			it.mvcMatchers("/hd/**").access("hasRole('" + RoleType.ROLE_HD_CASE_AGENT + "')"); //
			it.mvcMatchers("/hd/**").access("hasRole('" + RoleType.ROLE_HD_ADMIN + "')"); //
		});

		httpSecurity.csrf().disable().cors();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*")); // Problem!
		configuration.setAllowCredentials(true);
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration
				.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token", "client-code", "Origin"));
		configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
