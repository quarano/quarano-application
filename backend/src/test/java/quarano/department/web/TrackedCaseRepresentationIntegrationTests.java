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

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.core.EnumMessageSourceResolvable;
import quarano.department.CaseType;
import quarano.department.TrackedCaseRepository;
import quarano.department.web.TrackedCaseRepresentations.TrackedCaseDto;
import quarano.diary.DiaryEntry;
import quarano.diary.Slot;
import quarano.tracking.BodyTemperature;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.util.TestUtils;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import java.util.UUID;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class TrackedCaseRepresentationIntegrationTests {

	private final TrackedCaseRepresentations representations;
	private final MessageSourceAccessor messages;
	private final TrackedCaseRepository cases;
	private final ObjectMapper jackson;

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

	@Test // CORE-252
	void deserializesOriginCases() throws Exception {

		var result = jackson.readValue("{ \"originCases\" : [ \"/cases/4711\" ] }", TrackedCaseDto.Input.class);

		assertThat(result.getOriginCases()).hasSize(1);
	}

	@Test // CORE-252
	void doesNotSerializeOriginCasesInMainBody() throws Exception {

		TestUtils.fakeRequest(HttpMethod.GET, "/api/hd/cases", context);

		var contactCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();

		var result = jackson.writeValueAsString(representations.toRepresentation(contactCase));
		var document = JsonPath.parse(result, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

		assertThat(document.read("$.originCases", String.class)).isNull();
	}

	@Test
	void mapsDiaryEntryToDiaryEntrySummary() {

		var source = DiaryEntry.of(Slot.now(), TrackedPerson.TrackedPersonIdentifier.of(UUID.randomUUID()))
				.setBodyTemperature(BodyTemperature.of(40.0f));

		var result = representations.toDiaryEntrySummary(source);

		assertThat(result.getId()).isEqualTo(source.getId().toString());
		assertThat(result.getBodyTemperature()).isEqualTo(source.getBodyTemperature().getValue());
		assertThat(result.getReportedAt()).isEqualTo(source.getDateTime());
		assertThat(result.getSlot()).containsEntry("date", source.getSlot().getDate());
		assertThat(result.getSlot()).containsEntry("timeOfDay", source.getSlot().getTimeOfDay().name().toLowerCase());
		assertThat(result.getSymptoms()).allSatisfy(it -> {
			assertThat(it.getId()).isNotNull();
			assertThat(it.getName()).isNotBlank();
			assertThat(it.isCharacteristic()).isNotNull();
		});
	}
}
