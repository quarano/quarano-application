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
package quarano.department.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.core.EnumMessageSourceResolvable;
import quarano.department.CaseType;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.util.TestUtils;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class TrackedCaseRepresentationIntegrationTests {

	private final TrackedCaseRepresentations representations;
	private final MessageSourceAccessor messages;
	private final TrackedCaseRepository cases;

	private final WebApplicationContext context;

	@Test
	void exposesCaseTypeLabelOnSummary() {

		TestUtils.fakeRequest(HttpMethod.GET, "/api/hd/cases", context);

		var contactCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();

		assertThat(representations.toSummary(contactCase).getCaseTypeLabel())
				.isEqualTo(messages.getMessage(EnumMessageSourceResolvable.of(CaseType.CONTACT)));

		assertThat(representations.toSummary(contactCase.setType(CaseType.CONTACT_MEDICAL)).getCaseTypeLabel())
				.isEqualTo(messages.getMessage(EnumMessageSourceResolvable.of(CaseType.CONTACT_MEDICAL)));

		assertThat(representations.toSummary(contactCase.setType(CaseType.CONTACT_VULNERABLE)).getCaseTypeLabel())
				.isEqualTo(messages.getMessage(EnumMessageSourceResolvable.of(CaseType.CONTACT_VULNERABLE)));

		assertThat(representations.toSummary(contactCase.setType(CaseType.INDEX)).getCaseTypeLabel())
				.isEqualTo(messages.getMessage(EnumMessageSourceResolvable.of(CaseType.INDEX)));
	}
}
