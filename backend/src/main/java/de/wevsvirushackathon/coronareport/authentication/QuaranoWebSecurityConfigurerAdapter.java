package de.wevsvirushackathon.coronareport.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import de.wevsvirushackathon.coronareport.authentication.validation.JwtAuthenticationEntryPoint;
import de.wevsvirushackathon.coronareport.authentication.validation.JwtAuthenticationProvider;
import de.wevsvirushackathon.coronareport.authentication.validation.JwtAuthenticationTokenFilter;

/**
 * Configures security settings and defines which url pattern can be accessed by which role
 * @author Patrick Otto
 *
 */
@EnableWebSecurity
public class QuaranoWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

	private final Log logger = LogFactory.getLog(QuaranoWebSecurityConfigurerAdapter.class);
	
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
        return new JwtAuthenticationTokenFilter();
    }
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		
		logger.debug("Configuring HTTP security, allowing public access to '/login' and '/public/**'");

		httpSecurity.authorizeRequests()
        .antMatchers("/login").permitAll()
        .antMatchers("/client/register").permitAll()
        .antMatchers("/public/**").permitAll()
        .antMatchers("/**").access("hasRole('" + RoleType.ROLE_USER + "')")
        .antMatchers("/hd/**").access("hasRole('" + RoleType.ROLE_HD_CASE_AGENT + "')")
        .antMatchers("/hd/**").access("hasRole('" + RoleType.ROLE_HD_ADMIN +"')")
        .and()
            .csrf().disable()              
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
        .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    httpSecurity.headers().cacheControl();
	}
}
