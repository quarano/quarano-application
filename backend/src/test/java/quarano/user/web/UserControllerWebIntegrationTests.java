package quarano.user.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;
import quarano.core.EmailAddress;
import quarano.core.web.QuaranoHttpHeaders;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;
import quarano.user.web.UserRepresentations.PasswordChangeRequest;
import quarano.user.web.UserRepresentations.PasswordResetInput;
import quarano.user.web.UserRepresentations.PasswordResetRequest;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@QuaranoWebIntegrationTest
@RequiredArgsConstructor
class UserControllerWebIntegrationTests extends AbstractDocumentation {

	private final String USERNAME = "DemoAccount";
	private final String PASSWORD = "DemoPassword";
	private final String AGENT_USERNAME = "agent1";
	private final String AGENT_PASSWORD = "agent1";

	private final ObjectMapper mapper;
	private final LinkDiscoverer links;
	private final TrackedPersonRepository people;
	private final DocumentationFlow flow = DocumentationFlow.of("password-reset");

	@Test
	void testLoginWithValidCredentialsForTrackedPerson() throws Exception {

		// when
		var token = login(USERNAME, PASSWORD);

		String resultDtoStr = performGet(token);
		DocumentContext document = JsonPath.parse(resultDtoStr);

		assertThat(document.read("$.username", String.class)).isEqualTo("DemoAccount");
		assertThat(document.read("$.firstName", String.class)).isEqualTo("Markus");
		assertThat(document.read("$.lastName", String.class)).isEqualTo("Hanser");
		assertThat(document.read("$.client.locale", String.class)).isEqualTo("en");
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
		String resultDtoStr = mvc.perform(get("/api/user/me")
				.header("Origin", "*")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
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
		mvc.perform(options("/client/me")
				.header("Origin", "*")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.OPTIONS))
				.andExpect(status().isOk());
	}

	@Test // CORE-206
	void changesPasswordOfUser() throws Exception {

		var token = login(USERNAME, PASSWORD);
		var newPassword = "newPassword";
		var payload = new PasswordChangeRequest(PASSWORD, newPassword, newPassword);

		mvc.perform(put("/api/user/me/password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(payload))
				.header("Authorization", "Bearer " + token))
				.andExpect(status().is2xxSuccessful());

		expectLoginRejectedFor(USERNAME, PASSWORD);
		assertThat(login(USERNAME, newPassword)).isNotNull();
	}

	@Test // CORE-206
	void changesPasswordOfAdmin() throws Exception {

		var username = "agent1";
		var password = "agent1";

		var token = login(username, password);
		var newPassword = "newPassword";
		var payload = new PasswordChangeRequest(password, newPassword, newPassword);

		mvc.perform(put("/api/user/me/password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(payload))
				.header("Authorization", "Bearer " + token))
				.andExpect(status().is2xxSuccessful());

		expectLoginRejectedFor(username, password);
		assertThat(login(username, newPassword)).isNotNull();
	}

	@Test // CORE-206
	void rejectsPasswordChangeIfCurrentPasswordDoesntMatch() throws Exception {

		var newPassword = "newPassword";
		var payload = new PasswordChangeRequest("invalid", newPassword, newPassword);

		String result = issuePasswordChange(payload)
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(result);

		assertThat(document.read("$.current", String.class)).isNotNull();
	}

	@Test // CORE-206
	void rejectsPasswordChangeIfNewPasswordsDontMatch() throws Exception {

		var newPassword = "newPassword";
		var payload = new PasswordChangeRequest(PASSWORD, newPassword, newPassword + "!");

		String result = issuePasswordChange(payload)
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(result);

		assertThat(document.read("$.password", String.class)).isNotNull();
		assertThat(document.read("$.passwordConfirm", String.class)).isNotNull();
	}

	@Test // CORE-436
	void rejectsPasswordChangeIfNewPasswordIdenticalTheExistingOne() throws Exception {

		var payload = new PasswordChangeRequest(PASSWORD, PASSWORD, PASSWORD);

		String result = issuePasswordChange(payload)
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(result);

		assertThat(document.read("$.password", String.class)).isNotBlank();
	}

	@Test
	void changesPassword() throws Exception {

		var newPassword = PASSWORD + "!";

		issuePasswordChange(new PasswordChangeRequest(PASSWORD, newPassword, newPassword))
				.andDo(documentPasswordChange())
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	void exposesPasswortResetLinkOnApiRoot() {

	}

	@Test // CORE-92
	void requestsPasswordReset() throws Exception {

		requestPasswordReset();

		assertThat(people.findByEmailAddress(EmailAddress.of("markus.hanser@testtest.de")))
				.flatMap(TrackedPerson::getAccount)
				.hasValueSatisfying(it -> {
					assertThat(it.wasPasswordResetRequested()).isTrue();
				});
	}

	@TestFactory // CORE-92
	Stream<DynamicTest> rejectsInvalidPasswordResetRequests() {

		var validUsername = "DemoAccount";
		var validEmail = "markus.hanser@testtest.de";

		var invalidEmails = Stream.of(null, "daniel@dilemma.de", "noEmailAtAll", "foo@bar.com")
				.map(it -> new PasswordResetRequest().setUsername(validUsername).setEmail(it));
		var invalidUserNames = Stream.of(null, "someUser")
				.map(it -> new PasswordResetRequest().setUsername(it).setEmail(validEmail));

		return DynamicTest.stream(Stream.concat(invalidEmails, invalidUserNames),
				it -> String.format("… for %s and %s.", it.getUsername(), it.getEmail()),
				it -> {
					var response = mvc.perform(post("/password/reset")
							.content(mapper.writeValueAsString(it)))
							.andExpect(status().isBadRequest())
							.andReturn().getResponse();

					assertThat(response.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.TEXT_PLAIN_VALUE);
					assertThat(response.getContentAsString()).isNotEmpty();
				});

	}

	@Test // CORE-92
	void resetsPasswordSuccessfully() throws Exception {

		var resetPassword = UserLinkRelations.RESET_PASSWORD;

		var root = mvc.perform(get("/"))
				.andDo(flow.document("access-root-resource", relaxedLinks(linkWithRel(resetPassword.value()))))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		links.findRequiredLinkWithRel(resetPassword, root);

		var response = requestPasswordReset()
				.andDo(flow.document("request-reset", relaxedLinks(linkWithRel(resetPassword.value()))))
				.andReturn().getResponse().getContentAsString();

		var resetUrl = links.findRequiredLinkWithRel(resetPassword, response).getHref();
		var markus = people.findByEmailAddress(EmailAddress.of("markus.hanser@testtest.de")).orElseThrow();

		var reset = new PasswordResetInput()
				.setUsername("DemoAccount")
				.setDateOfBirth(markus.getDateOfBirth())
				.setPassword("newPassword")
				.setPasswordConfirm("newPassword");

		mvc.perform(put(resetUrl)
				.content(mapper.writeValueAsString(reset)))
				.andExpect(status().is2xxSuccessful())
				.andDo(flow.document("perform-reset"));
	}

	private ResultActions requestPasswordReset() throws Exception {

		var request = new PasswordResetRequest()
				.setUsername("DemoAccount")
				.setEmail("markus.hanser@testtest.de");

		return mvc.perform(post("/password/reset")
				.content(mapper.writeValueAsString(request)))
				.andExpect(status().is2xxSuccessful());
	}

	@SuppressWarnings("null")
	private String login(String username, String password) throws Exception {

		var token = mvc.perform(post("/api/login")
				.header("Origin", "*")
				.contentType(MediaType.APPLICATION_JSON)
				.content(createLoginRequestBody(username, password)))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse()
				.getHeader(QuaranoHttpHeaders.AUTH_TOKEN);

		assertThat(token).isNotNull();

		return token;
	}

	private void expectLoginRejectedFor(String username, String password) throws Exception {

		mvc.perform(post("/api/login")
				.header("Origin", "*")
				.contentType(MediaType.APPLICATION_JSON)
				.content(createLoginRequestBody(username, password)))
				.andExpect(status().isUnauthorized());
	}

	private ResultActions issuePasswordChange(PasswordChangeRequest payload) throws Exception {

		var token = login(USERNAME, PASSWORD);

		return mvc.perform(put("/api/user/me/password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(payload))
				.header("Authorization", "Bearer " + token));
	}

	private String createLoginRequestBody(String username, String password) throws Exception {
		return mapper.writeValueAsString(Map.of("username", username, "password", password));
	}

	private static ResultHandler documentPasswordChange() {
		return DocumentationFlow.of("password-expired").document("change-password");
	}
}
