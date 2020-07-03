package quarano.user.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.core.web.QuaranoHttpHeaders;
import quarano.user.web.UserController.NewPassword;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@QuaranoWebIntegrationTest
@RequiredArgsConstructor
class UserControllerWebIntegrationTests {

	private final String USERNAME = "DemoAccount";
	private final String PASSWORD = "DemoPassword";
	private final String AGENT_USERNAME = "agent1";
	private final String AGENT_PASSWORD = "agent1";

	private final MockMvc mvc;
	private final ObjectMapper mapper;

	@Test
	void testLoginWithValidCredentialsForTrackedPerson() throws Exception {

		// when
		var token = login(USERNAME, PASSWORD);

		String resultDtoStr = performGet(token);
		DocumentContext document = JsonPath.parse(resultDtoStr);

		assertThat(document.read("$.username", String.class)).isEqualTo("DemoAccount");
		assertThat(document.read("$.firstName", String.class)).isEqualTo("Markus");
		assertThat(document.read("$.lastName", String.class)).isEqualTo("Hanser");
		assertThat(document.read("$.healthDepartment.name", String.class)).isEqualTo("GA Mannheim");
		assertThat(document.read("$.healthDepartment.email", String.class)).isEqualTo("index-email@gesundheitsamt.de");
		assertThat(document.read("$.healthDepartment.phone", String.class)).isEqualTo("0123456789");
	}

	@Test
	void testLoginWithValidCredentialsForAgent() throws Exception {

		// when
		var token = login(AGENT_USERNAME, AGENT_PASSWORD);

		String resultDtoStr = performGet(token);
		DocumentContext document = JsonPath.parse(resultDtoStr);

		assertThat(document.read("$.username", String.class)).isEqualTo("agent1");
		assertThat(document.read("$.firstName", String.class)).isEqualTo("Horst");
		assertThat(document.read("$.lastName", String.class)).isEqualTo("Hallig");
		assertThat(document.read("$.healthDepartment.name", String.class)).isEqualTo("GA Mannheim");
		assertThat(document.read("$.healthDepartment.email", String.class)).isNull();
		assertThat(document.read("$.healthDepartment.phone", String.class)).isNull();
	}

	private String performGet(String token) throws Exception {
		// check if token is valid for authentication
		String resultDtoStr = mvc.perform(get("/api/user/me") //
				.header("Origin", "*") //
				.header("Authorization", "Bearer " + token) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) //
				.andReturn().getResponse().getContentAsString();
		return resultDtoStr;
	}

	@Test
	void testLoginWithInvalidCredentials() throws Exception {
		expectLoginRejectedFor("DemoAccount", "My-Wrong-Password");
	}

	@Test
	void loginWithInvalidUsernameIsRejected() throws Exception {
		expectLoginRejectedFor("InvalidUsername", "DemoPassword");
	}

	@Test
	void testThatPreflightRequestsGetNotBlocked() throws Exception {

		// check if token is valid for authentication
		mvc.perform(options("/client/me") //
				.header("Origin", "*") //
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.OPTIONS)) //
				.andExpect(status().isOk());
	}

	@Test // CORE-206
	void changesPasswordOfUser() throws Exception {

		var token = login(USERNAME, PASSWORD);
		var newPassword = "newPassword";
		var payload = new UserController.NewPassword(PASSWORD, newPassword, newPassword);

		mvc.perform(put("/api/user/me/password") //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(mapper.writeValueAsString(payload)) //
				.header("Authorization", "Bearer " + token)) //
				.andExpect(status().isOk());

		expectLoginRejectedFor(USERNAME, PASSWORD);
		assertThat(login(USERNAME, newPassword)).isNotNull();
	}

	@Test // CORE-206
	void changesPasswordOfAdmin() throws Exception {

		var username = "agent1";
		var password = "agent1";

		var token = login(username, password);
		var newPassword = "newPassword";
		var payload = new UserController.NewPassword(password, newPassword, newPassword);

		mvc.perform(put("/api/user/me/password") //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(mapper.writeValueAsString(payload)) //
				.header("Authorization", "Bearer " + token)) //
				.andExpect(status().isOk());

		expectLoginRejectedFor(username, password);
		assertThat(login(username, newPassword)).isNotNull();
	}

	@Test // CORE-206
	void rejectsPasswordChangeIfCurrentPasswordDoesntMatch() throws Exception {

		var newPassword = "newPassword";
		var payload = new UserController.NewPassword("invalid", newPassword, newPassword);

		String result = issuePasswordChange(payload) //
				.andExpect(status().isBadRequest()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(result);

		assertThat(document.read("$.current", String.class)).isNotNull();
	}

	@Test // CORE-206
	void rejectsPasswordChangeIfNewPasswordsDontMatch() throws Exception {

		var newPassword = "newPassword";
		var payload = new UserController.NewPassword(USERNAME, newPassword, newPassword + "!");

		String result = issuePasswordChange(payload) //
				.andExpect(status().isBadRequest()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(result);

		assertThat(document.read("$.password", String.class)).isNotNull();
		assertThat(document.read("$.passwordConfirm", String.class)).isNotNull();
	}

	private String login(String username, String password) throws Exception {

		return mvc.perform(post("/login") //
				.header("Origin", "*") //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(createLoginRequestBody(username, password))) //
				.andExpect(status().is2xxSuccessful()) //
				.andReturn().getResponse() //
				.getHeader(QuaranoHttpHeaders.AUTH_TOKEN);
	}

	private void expectLoginRejectedFor(String username, String password) throws Exception {

		mvc.perform(post("/login") //
				.header("Origin", "*") //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(createLoginRequestBody(username, password))) //
				.andExpect(status().isUnauthorized());
	}

	private ResultActions issuePasswordChange(NewPassword payload) throws Exception {

		var token = login(USERNAME, PASSWORD);

		return mvc.perform(put("/api/user/me/password") //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(mapper.writeValueAsString(payload)) //
				.header("Authorization", "Bearer " + token));
	}

	private String createLoginRequestBody(String username, String password) throws Exception {
		return mapper.writeValueAsString(Map.of("username", username, "password", password));
	}
}
