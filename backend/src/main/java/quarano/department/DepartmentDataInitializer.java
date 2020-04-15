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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.department.Department.DepartmentIdentifier;

import java.util.UUID;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
@Order(510)
@Slf4j
public class DepartmentDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

	private final @NonNull DepartmentRepository departments;

	public final static DepartmentIdentifier DEPARTMENT_ID_DEP1 = DepartmentIdentifier
			.of(UUID.fromString("aba0ec65-6c1d-4b7b-91b4-c31ef16ad0a2"));
	public final static DepartmentIdentifier DEPARTMENT_ID_DEP2 = DepartmentIdentifier
			.of(UUID.fromString("ca3f3e9a-414a-4117-a623-59b109b269f1"));

	public final static Department DEPARTMENT_1 = new Department("GA Mannheim", DEPARTMENT_ID_DEP1);
	public final static Department DEPARTMENT_2 = new Department("GA Darmstadt", DEPARTMENT_ID_DEP2);

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		this.log.warn("Testdata: creating two healthdepartmens");

		final Department hd1 = this.departments.save(DEPARTMENT_1);
		final Department hd2 = this.departments.save(DEPARTMENT_2);
	}
}
