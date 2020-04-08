package de.wevsvirushackathon.coronareport.authentication.validation;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A Request filter that checks if the Bearer Token header is set and starts and
 * authentication with the containing token
 * 
 * @author Patrick Otto
 *
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	@Value("${jwt.validation.header}")
	private String tokenHeader;
	
	 @Autowired
	private JwtAuthenticationProvider authProvider;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String requestHeader = request.getHeader(this.tokenHeader);

		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			String authToken = requestHeader.substring(7);
			
			Authentication filledAuth = this.authProvider.authenticate(new JwtAuthentication(authToken));
			SecurityContextHolder.getContext().setAuthentication(filledAuth);
		}
		chain.doFilter(request, response);
	}
}