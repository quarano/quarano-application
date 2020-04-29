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
package quarano.auth;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.department.DepartmentDataInitializer;
import quarano.tracking.TrackedPersonDataInitializer;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class ActivationCodeServiceIntegrationTests {

	private final ActivationCodeService codes;

	@Test
	void createsActivationCode() {

		var personId = TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1;
		var departmentId = DepartmentDataInitializer.DEPARTMENT_ID_DEP1;

		var code = codes.createActivationCode(personId, departmentId).get();

		assertThat(code.isWaitingForActivation()).isTrue();
		assertThat(codes.getValidCode(code.getId()).isSuccess()).isTrue();
	}
}
