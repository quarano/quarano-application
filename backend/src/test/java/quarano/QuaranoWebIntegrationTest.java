package quarano;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

/**
 * @author Oliver Drotbohm
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@QuaranoIntegrationTest
@AutoConfigureMockMvc
public @interface QuaranoWebIntegrationTest {

}
