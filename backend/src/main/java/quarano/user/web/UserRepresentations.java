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
package quarano.user.web;

import lombok.RequiredArgsConstructor;
import quarano.account.Department;
import quarano.core.web.MapperWrapper;
import quarano.department.Enrollment;
import quarano.department.web.TrackedCaseRepresentations;
import quarano.department.web.TrackedCaseRepresentations.EnrollmentDto;
import quarano.tracking.TrackedPerson;
import quarano.tracking.web.TrackedPersonDto;

import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class UserRepresentations {

	private final MapperWrapper mapper;
	private final TrackedCaseRepresentations delegate;

	EnrollmentDto toRepresentation(Enrollment enrollment) {
		return delegate.toRepresentation(enrollment);
	}

	DepartmentDto toRepresentation(Department department) {
		return mapper.map(department, DepartmentDto.class);
	}

	TrackedPersonDto toRepresentation(TrackedPerson person) {
		return mapper.map(person, TrackedPersonDto.class);
	}
}
