package quarano.core.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;

/**
 * @author Oliver Drotbohm
 * @author Felix Schultze
 */
@ReportAsSingleViolation
@Pattern(regexp = Strings.LETTERS_AND_NUMBERS)
@Constraint(validatedBy = {})
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface AlphaNumeric {
	String ERROR_MSG = "{AlphaNumeric}";

	String message() default ERROR_MSG;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
