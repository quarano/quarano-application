package quarano.security.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.department.TrackedCaseRepository;
import quarano.security.web.AuthenticationController.AuthenticationRequest;
import quarano.tracking.TrackedPersonDataInitializer;

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
				.andExpect(status().is2xxSuccessful());
	}
}
