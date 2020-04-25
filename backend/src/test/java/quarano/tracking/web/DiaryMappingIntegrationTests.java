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
package quarano.tracking.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.tracking.Slot;
import quarano.tracking.Slot.TimeOfDay;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;
import quarano.util.TestUtils;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

/**
 * @author Oliver Drotbohm
 */
@QuaranoWebIntegrationTest
@RequiredArgsConstructor
class DiaryMappingIntegrationTests {

	private final DiaryRepresentations representations;
	private final TrackedPersonRepository people;
	private final ObjectMapper mapper;
	private final WebApplicationContext context;

	@Test
	void doesNotExposeCreationOfCurrentEveningsEntryInTheMorning() throws Exception {

		var person = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2).orElseThrow();
		var thisMorning = Slot.of(LocalDate.now(), TimeOfDay.MORNING);
		var summary = representations.toSummary(person.getDiary(), person.getAccountRegistrationDate(), thisMorning);

		TestUtils.fakeRequest(HttpMethod.GET, "/api", context);

		var result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(summary);
		var document = JsonPath.parse(result);

		assertThatExceptionOfType(PathNotFoundException.class).isThrownBy(() -> {
			document.read("$._embedded.entries[0].evening._links.create");
		});
	}
}
