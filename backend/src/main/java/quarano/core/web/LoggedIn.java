package quarano.core.web;

import quarano.tracking.TrackedPerson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.ServletRequestBindingException;

/**
 * Annotation to mark the method parameter with that shall get the {@link TrackedPerson} corresponding to the currently
 * logged in user injected. The method parameter needs to be of type {@code TrackedPerson} A
 * {@link ServletRequestBindingException} will be thrown if no {@link TrackedPerson} could have been obtained in the
 * first place.
 *
 * @author Oliver Drotbohm
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggedIn {
}
