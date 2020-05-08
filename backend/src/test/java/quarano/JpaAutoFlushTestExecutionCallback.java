/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

		applicationContext.getBeanProvider(EntityManager.class) //
				.stream() //
				.forEach(em -> em.flush());
	}
}
