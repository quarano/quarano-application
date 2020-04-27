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

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

/**
 * @author Oliver Drotbohm
 * @author Felix Schultze
 */
@QuaranoWebIntegrationTest
@WithQuaranoUser("test3")
@RequiredArgsConstructor
class ContactPersonControllerWebIntegrationTests {

	private final MockMvc mvc;
	private final ObjectMapper mapper;

	
	@Test
	void addContactPersonSuccess() throws Exception {

		var payload = new ContactPersonDto();
		payload.setFirstName("TestNameFirst");
		payload.setLastName("TestName");
		payload.setIsHealthStaff(true);
		payload.setEmail("test@testtest.de");
		payload.setMobilePhone("0123910");

		String response = mvc.perform(post("/api/contacts") //
				.content(mapper.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isCreated()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.firstName", String.class)).isEqualTo("TestNameFirst");
		assertThat(document.read("$.lastName", String.class)).isEqualTo("TestName");
		assertThat(document.read("$.isHealthStaff", String.class)).isEqualTo("true");
		assertThat(document.read("$.email", String.class)).isEqualTo("test@testtest.de");
		assertThat(document.read("$.mobilePhone", String.class)).isEqualTo("0123910");
	}
	
	@Test
	void rejectsMissingContactWays() throws Exception {

		var payload = new ContactPersonDto();

		String response = mvc.perform(post("/api/contacts") //
				.content(mapper.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isBadRequest()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.phone", String.class)).isNotNull();
		assertThat(document.read("$.mobilePhone", String.class)).isNotNull();
		assertThat(document.read("$.email", String.class)).isNotNull();
		assertThat(document.read("$.identificationHint", String.class)).isNotNull();
	}

	@Test
	void rejectsFirstAndLastNameContainingNumbers() throws Exception {

		var payload = new ContactPersonDto();
		payload.setFirstName("Test121231 ");
		payload.setLastName("TestN121231 ");
		payload.setMobilePhone("0123910");

		String response = mvc.perform(post("/api/contacts") //
				.content(mapper.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isBadRequest()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.firstName", String.class)).isEqualTo("Dieses Feld darf nur Buchstaben enthalten!");
		assertThat(document.read("$.lastName", String.class)).isEqualTo("Dieses Feld darf nur Buchstaben enthalten!");
	}
	

	@TestFactory
	Stream<DynamicTest> acceptsRequestIfOneContactWayIsGiven() {

		var fields = Map.of("phone", "123456789", //
				"mobilePhone", "123456789", //
				"email", "michael@mustermann.de", //
				"identificationHint", "Fleischereifachverkäufer");

		return DynamicTest.stream(fields.entrySet().iterator(), //
				entry -> {
					return String.format("Accepts new contact if only %s is given", entry.getKey());
				}, //
				entry -> {

					var payload = JsonPath.parse(mapper.writeValueAsString(new ContactPersonDto())).set("$." + entry.getKey(),
							entry.getValue());

					String response = mvc.perform(post("/api/contacts") //
							.content(payload.jsonString()) //
							.contentType(MediaType.APPLICATION_JSON)) //
							.andExpect(status().isCreated()) //
							.andReturn().getResponse().getContentAsString();

					var document = JsonPath.parse(response);

					assertThat(document.read("$." + entry.getKey(), String.class)).isNotNull();
				});
	}
}
