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
package quarano.security.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.department.TrackedCaseRepository;
import quarano.security.web.AuthenticationController.AuthenticationRequest;
import quarano.tracking.TrackedPersonDataInitializer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoWebIntegrationTest
class AuthenticationControllerWebIntegrationTests {

	private final MockMvc mvc;
	private final TrackedCaseRepository cases;
	private final ObjectMapper jackson;

	@Test
	@Disabled
	void rejectsLoginForPersonWithCaseConcluded() throws Exception {

		var siggisCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_SEC1_ID_DEP1).orElseThrow();

		cases.save(siggisCase.conclude());

		mvc.perform(post("/api/login") //
				.content(jackson.writeValueAsString(new AuthenticationRequest("secUser1", "secur1tyTest!"))) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isUnauthorized());
	}

	@Test
	void logsInTrackedUserWithOpenCase() throws Exception {
		assertSuccessfulLogin("secUser1", "secur1tyTest!");
	}

	@Test
	void logsInStaffMember() throws Exception {
		assertSuccessfulLogin("agent1", "agent1");
	}

	@Test
	void logsInAdmin() throws Exception {
		assertSuccessfulLogin("admin", "admin");
	}

	private void assertSuccessfulLogin(String username, String password) throws Exception {

		mvc.perform(post("/api/login") //
				.content(jackson.writeValueAsString(new AuthenticationRequest(username, password))) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk());
	}
}
