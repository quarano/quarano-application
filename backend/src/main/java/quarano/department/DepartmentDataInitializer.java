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
package quarano.department;

import de.wevsvirushackathon.coronareport.healthdepartment.HealthDepartmentRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
@Order(550)
class DepartmentDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

	private final @NonNull DepartmentRepository departments;
	private final @NonNull HealthDepartmentRepository legacyDepartments;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		legacyDepartments.findAll().forEach(it -> {
			departments.save(new Department(it.getFullName(), it.getPassCode()));
		});
	}
}
