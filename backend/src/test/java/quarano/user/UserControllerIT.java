package quarano.user;

import static de.wevsvirushackathon.coronareport.TestUtil.fromJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import de.wevsvirushackathon.coronareport.CoronareportBackendApplication;
import de.wevsvirushackathon.coronareport.client.ClientDto;
import de.wevsvirushackathon.coronareport.client.TokenResponse;
import quarano.Quarano;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.web.TrackedPersonDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {CoronareportBackendApplication.class, Quarano.class})
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class UserControllerIT {

	@Autowired
	private MockMvc mvc;


	@Test
	public void testLoginWithValidCredentials() throws Exception {

		// Accounts and password created by dummy data input beans
		// given
		final String username = "DemoAccount";
		final String password = "DemoPassword";
		
		final TrackedPerson person = TrackedPersonDataInitializer.INDEX_PERSON2_IN_ENROLLMENT;
		final TrackedPersonDto expectedPersonDto = TrackedPersonDto.builder()
				.firstName(person.getFirstName())
				.lastName(person.getLastName())
				.infected(true)
				.email(person.getEmailAddress().toString())
				.phone(person.getPhoneNumber().toString())
				.build();
		

		String requestbody = createLoginRequestBody(username, password);

		// when
		String resultLogin = mvc
				.perform(post("/login").header("Origin", "*").contentType(MediaType.APPLICATION_JSON)
						.content(requestbody))

				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		TokenResponse response = fromJson(resultLogin, TokenResponse.class);

		// check if token is valid for authentication
		String resultDtoStr = mvc
				.perform(
						get("/user/me").header("Origin", "*").header("Authorization", "Bearer " + response.getToken())
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		UserDto resultDto = fromJson(resultDtoStr, UserDto.class);

		// then
		Assertions.assertEquals(expectedPersonDto, resultDto.getClient());

	}

	@Test
	public void testLoginWithInvalidCredentials() throws Exception {

		// Accounts and password created by dummy data input beans
		// given
		final String username = "DemoAccount";
		final String password = "My-Wrong-Password";

		String requestbody = createLoginRequestBody(username, password);

		// when
		mvc.perform(post("/login")
					.header("Origin", "*")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestbody))

			.andExpect(status().isUnauthorized())
			.andReturn().getResponse()
			.getContentAsString();

	}

	@Test
	public void testThatPreflightRequestsGetNotBlocked() throws Exception {

		// check if token is valid for authentication
		mvc.perform(options("/client/me")
				.header("Origin", "*")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.OPTIONS))
				.andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

	}

	public static String createLoginRequestBody(String username, String password) {
		return "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";
	}



}