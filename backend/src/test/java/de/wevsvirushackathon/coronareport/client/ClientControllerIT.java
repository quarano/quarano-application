package de.wevsvirushackathon.coronareport.client;

import static de.wevsvirushackathon.coronareport.TestUtil.fromJson;
import static de.wevsvirushackathon.coronareport.TestUtil.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import de.wevsvirushackathon.coronareport.TestDataProvider;
import de.wevsvirushackathon.coronareport.user.UserControllerIT;
import de.wevsvirushackathon.coronareport.user.UserDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ClientControllerIT {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private TestDataProvider testData;

	@Test
	public void registerNewAccountForClientSuccess() throws Exception {
		
		// the client to 
		Client client = testData.clientWithNoAccount();
		final String password = "myPassword";
		final String username = "testusername";
		final Date dateOfBirth = Date.from(Instant.parse("1980-10-01T00:00:00.00Z"));

		final AccountRegistrationDto registrationDto = AccountRegistrationDto.builder()
				.username(username)
				.dateOfBirth(dateOfBirth)
				.email("mytestmail@test.com")
				.password(password)
				.clientCode(client.getClientCode())
				.build();

		mvc	.perform(
				post("/client/" + client.getClientId() +  "/register")
					.header("Origin", "*")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(registrationDto)))
			.andExpect(
					status().
					isOk()).
			andExpect(
					content().
					contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
			.andReturn().getResponse().getContentAsString();
		
		// login with new account
		String requestbody = UserControllerIT.createLoginRequestBody(username, password);
		String jwtToken = mvc
				.perform(post("/login").header("Origin", "*").contentType(MediaType.APPLICATION_JSON)
						.content(requestbody))

				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		TokenResponse response = fromJson(jwtToken, TokenResponse.class);

		// check if token is valid for authentication
		String resultDtoStr = mvc
				.perform(
						get("/user/me").header("Origin", "*").header("Authorization", "Bearer " + response.getToken())
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		UserDto resultDto = fromJson(resultDtoStr, UserDto.class);

		// then
		Assertions.assertTrue(resultDto.getClient().getClientId().equals(client.getClientId()));
		Assertions.assertTrue(resultDto.getClient().getRegistrationTimestamp().after(Timestamp.from(Instant.now().minusSeconds(20))));
	

	}

	@Test
	public void testCheckClientCodeWithValidCode() throws Exception {

		String resultAsString = mvc
				.perform(get("/client/checkcode/" + TestDataProvider.validClientCode()).header("Origin", "*")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		Assertions.assertTrue(Boolean.parseBoolean(resultAsString));

	}

	@Test
	public void testCheckClientCodeWithInvalidCodeFailsWith404() throws Exception {

		mvc.perform(get("/client/checkcode/" + TestDataProvider.invalidClientCode()).header("Origin", "*")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn().getResponse()
				.getContentAsString();
	}

	@Test
	public void testCheckClientCodeWithoutCodeFailsWith400() throws Exception {

		mvc.perform(get("/client/checkcode/").header("Origin", "*").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
	}

}