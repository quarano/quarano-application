package quarano;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A JUnit 5 callback to make sure, JPA {@link EntityManager}s contained in the application are flushed for each test
 * method execution.
 *
 * @author Oliver Drotbohm
 */
class JpaAutoFlushTestExecutionCallback implements AfterTestExecutionCallback {

	/*
	 * (non-Javadoc)
	 * @see org.junit.jupiter.api.extension.AfterTestExecutionCallback#afterTestExecution(org.junit.jupiter.api.extension.ExtensionContext)
	 */
	@Override
	public void afterTestExecution(ExtensionContext context) throws Exception {

		ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);

		applicationContext.getBeanProvider(EntityManager.class)
				.stream()
				.forEach(em -> em.flush());
	}
}
