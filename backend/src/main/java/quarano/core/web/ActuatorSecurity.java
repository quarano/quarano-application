package quarano.core.web;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration(proxyBeanMethods = false)
@Order(90)
@RequiredArgsConstructor
class ActuatorSecurity extends WebSecurityConfigurerAdapter {

	private final CorsConfigurationSource cors;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.requestMatcher(EndpointRequest.toAnyEndpoint())
				.authorizeRequests((requests) -> requests.anyRequest().permitAll())
				.csrf().disable()
				.cors(it -> it.configurationSource(cors));
	}
}
