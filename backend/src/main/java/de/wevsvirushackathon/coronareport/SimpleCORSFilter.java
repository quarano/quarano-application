package de.wevsvirushackathon.coronareport;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Allows requests from different domains as the one on which the api runs.
 * <p>
 * TODO This effectively disables Cors. It is a means for development during MVP, will be replaced before production.
 *
 * @author Patrick Otto
 */
@Component
public class SimpleCORSFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(SimpleCORSFilter.class);

    public SimpleCORSFilter() {
        log.info("SimpleCORSFilter init");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, HEAD, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, client-code, Origin,Accept, X-Requested-With, remember-me, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}