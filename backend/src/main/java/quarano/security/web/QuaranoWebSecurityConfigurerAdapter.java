package quarano.security.web;

import static quarano.core.web.QuaranoHttpHeaders.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.Account;
import quarano.account.AccountService;
import quarano.account.RoleType;
import quarano.security.JwtConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configures security settings and defines which url pattern can be accessed by which role
 *
 * @author Patrick Otto
 * @author Oliver Drotbohm
 */
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class QuaranoWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

	private final JwtConfiguration configuration;
	private final AccountService accounts;

	private static final String[] SWAGGER_UI_WHITELIST = {

			// -- swagger ui
			"/swagger-resources/**",
			"/swagger-ui.html",
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/webjars/**" };

	@Bean
	JwtDecoder jwtDecoder() {
		return configuration.getJwtDecoder();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		Function<String, Account> accountSource = username -> accounts.findByUsername(username)
				.orElseThrow(
						() -> new UsernameNotFoundException(String.format("Couldn't find account for username %s!", username)));

		httpSecurity.oauth2ResourceServer()
				.jwt().jwtAuthenticationConverter(configuration.getJwtConverter(accountSource));

		httpSecurity.authorizeRequests(it -> {
			it.mvcMatchers("/api/login").permitAll();
			it.mvcMatchers("/api/password/**").permitAll();
			it.mvcMatchers("/api/registration").permitAll();
			it.mvcMatchers("/api/registration/checkcode/**").permitAll();
			it.mvcMatchers("/api/registration/checkusername/**").permitAll();
			it.mvcMatchers("/api/frontendtexts").permitAll();
			it.mvcMatchers("/api/hd/accounts/**").access("hasRole('" + RoleType.ROLE_HD_ADMIN + "')");
			it.mvcMatchers("/api/hd/**").access(hasAnyRole(RoleType.ROLE_HD_CASE_AGENT, RoleType.ROLE_HD_ADMIN));
			it.mvcMatchers("/api/user/**").authenticated();
			it.mvcMatchers("/api/symptoms").authenticated();
			it.mvcMatchers("/api/**").access("hasRole('" + RoleType.ROLE_USER + "')");
			it.mvcMatchers(SWAGGER_UI_WHITELIST).permitAll();
			it.mvcMatchers("/").permitAll();
			it.mvcMatchers("/docs/**").permitAll();
			it.mvcMatchers("/h2_console/**").permitAll();
			it.mvcMatchers("/**").permitAll();
		});
		// this will ignore only h2-console csrf, spring security 4+
		httpSecurity.csrf().ignoringAntMatchers("/h2-console/**");
		// this will allow frames with same origin which is much more safe
		httpSecurity.headers().frameOptions().sameOrigin();

		httpSecurity.csrf().disable().cors(it -> {
			it.configurationSource(corsConfigurationSource());
		});
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.mvcMatchers(
						"/**/**.js",
						"/**/**.css",
						"/**/**.html"
				);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(this.configuration.getAllowedOrigins());
		configuration.setAllowCredentials(true);
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(
				List.of(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, HttpHeaders.ORIGIN, AUTH_TOKEN));
		configuration.setExposedHeaders(List.of(AUTH_TOKEN));

		log.debug("Configuring HTTP-Security:");
		log.debug("Set Allowed-Origins: " + configuration.getAllowedOrigins());
		log.debug("Set Allowed-Headers: " + configuration.getAllowedHeaders());
		log.debug("Exposed Headers: " + configuration.getExposedHeaders());

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	private static String hasAnyRole(RoleType... roleType) {

		return Arrays.stream(roleType)
				.map(RoleType::name)
				.collect(Collectors.joining("', '", "hasAnyRole('", "')"));
	}
}
