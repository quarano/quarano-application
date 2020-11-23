package quarano.core;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Annotation for {@link TransactionalEventListener}s that are supposed to be executed asynchronously and trigger a
 * transaction themselves.
 *
 * @author Oliver Drotbohm
 */
@Async
@Transactional
@TransactionalEventListener
@Retention(RUNTIME)
@Target({ METHOD, ANNOTATION_TYPE })
public @interface AsyncTransactionalEventListener {}
