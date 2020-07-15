package quarano.security.web;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.account.AccountService;
import quarano.account.Password.UnencryptedPassword;
import quarano.department.TrackedCaseRepository;
import quarano.security.web.AuthenticationController.AuthenticationRequest;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.user.web.UserController;

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
	private final AccountService accounts;

	@Test
	void rejectsLoginForPersonWithCaseConcluded() throws Exception {

		var siggisCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_SEC1_ID_DEP1)
				.orElseThrow()
				.conclude();

		cases.save(siggisCase);

		mvc.perform(post("/api/login")
				.content(jackson.writeValueAsString(new AuthenticationRequest("secUser1", "secur1tyTest!")))
				.contentType(MediaType.APPLICATION_JSON))
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

	@Test // CORE-231
	@WithQuaranoUser("admin")
	void loggingInWithExpiredPasswordIndicatesPasswordChangeNeeded() throws Exception {

		// Given the password of agent4 changed by an admin
		var password = UnencryptedPassword.of("test");
		var account = accounts.changePassword(password, accounts.findByUsername("agent4").orElseThrow());

		// When logging in
		mvc.perform(post("/api/login")
				.content(jackson.writeValueAsString(new AuthenticationRequest(account.getUsername(), "test")))
				.contentType(MediaType.APPLICATION_JSON))

				// We expect the expiration of the password to be indicated by a links pointing to the
				// resource changing the password
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links[0].rel", is("next")))
				.andExpect(jsonPath("$._links[0].href", containsString("/user/me/password")))
				.andExpect(jsonPath("$._links[1].rel", is(UserController.CHANGE_PASSWORD.toString())))
				.andExpect(jsonPath("$._links[1].href", containsString("/user/me/password")));
	}

	private void assertSuccessfulLogin(String username, String password) throws Exception {

		mvc.perform(post("/api/login")
				.content(jackson.writeValueAsString(new AuthenticationRequest(username, password)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}
}
