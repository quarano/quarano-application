package quarano;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

/**
 * Runs the annotated method with the given user logged in.
 *
 * @author Oliver Drotbohm
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = AccountSecurityContextFactory.class)
public @interface WithQuaranoUser {

	String value();
}
