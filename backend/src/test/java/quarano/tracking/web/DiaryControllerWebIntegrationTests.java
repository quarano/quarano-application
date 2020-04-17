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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.core.web.MapperWrapper;
import quarano.tracking.DiaryEntry;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

/**
 * @author Oliver Drotbohm
 */
@QuaranoWebIntegrationTest
class DiaryControllerWebIntegrationTests {

	@Autowired MockMvc mvc;
	@Autowired TrackedPersonRepository repository;
	@Autowired MapperWrapper mapper;
	@Autowired ObjectMapper jackson;

	@Test
	@WithQuaranoUser("test3")
	void updatesDiaryEntry() throws Exception {

		var person = repository.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2).orElseThrow();
		DiaryEntry entry = person.getDiary().iterator().next();

		DiaryEntryDto payload = mapper.map(entry, DiaryEntryDto.class);
		payload.setBodyTemperature(42.0f);

		String response = mvc.perform(put("/api/diary/{identifier}", entry.getId()) //
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		DocumentContext document = JsonPath.parse(response);

		assertThat(document.read("$.id", String.class)).isNotNull();
		assertThat(document.read("$.bodyTemperature", float.class)).isEqualTo(42.0f);
	}
}
