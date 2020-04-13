package de.wevsvirushackathon.coronareport.client;

import static de.wevsvirushackathon.coronareport.TestUtil.fromJson;
import static de.wevsvirushackathon.coronareport.TestUtil.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import com.fasterxml.jackson.core.JsonProcessingException;

import de.wevsvirushackathon.coronareport.TestDataProvider;
import de.wevsvirushackathon.coronareport.user.UserControllerIT;
import de.wevsvirushackathon.coronareport.user.UserDto;
import quarano.registration.web.AccountRegistrationDto;

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
		
		// Given
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

		// when
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
		
		
		// then check if login works with new accoutn
		String token = loginAndCheckSuccess(password, username);

		// and check if token is valid for authentication
		UserDto resultDto = callUserMeAndCheckSuccess(token);

		// check if changes to client object happened as expected
		//Assertions.assertTrue(resultDto.getClient().getRegistrationTimestamp().after(Timestamp.from(Instant.now().minusSeconds(100))));
	}
	
	
	@Test
	public void registerNewAccountWithInvalidActivationCodeFails() throws Exception {
		
		// Given
		Client client = testData.clientWithNoAccount();
		final String password = "myPassword";
		final String username = "testusername";
		final Date dateOfBirth = Date.from(Instant.parse("1980-10-01T00:00:00.00Z"));

		final AccountRegistrationDto registrationDto = AccountRegistrationDto.builder()
				.username(username)
				.dateOfBirth(dateOfBirth)
				.email("mytestmail@test.com")
				.password(password)
				.clientCode(TestDataProvider.invalidClientCode())
				.build();

		// when
		mvc	.perform(
				post("/client/" + client.getClientId() +  "/register")
					.header("Origin", "*")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(registrationDto)))
			.andExpect(
					status().
					isBadRequest()).
			andExpect(
					content().
					contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
			.andReturn().getResponse().getContentAsString();
		
		// then check that login does not work with new account
		checkLoginFails(password, username);

	}
	
	@Test
	public void registerAccountWithExistingUsernameFails() throws Exception {
		
		// Given
		Client client = testData.clientWithNoAccount();
		final String passwordUser1 = "myPassword";
		final String passwordUser2 = "anotherPassword";
		final String username = "testusername";
		final Date dateOfBirthUser1 = Date.from(Instant.parse("1980-10-01T00:00:00.00Z"));
		final Date dateOfBirthUser2 = Date.from(Instant.parse("1995-03-01T00:00:00.00Z"));
		final String emailUser1 = "mytestmail@test.com";
		final String emailUser2 = "anotheremail@test.com";

		final AccountRegistrationDto registrationDto = AccountRegistrationDto.builder()
				.username(username)
				.dateOfBirth(dateOfBirthUser1)
				.email(emailUser1)
				.password(passwordUser1)
				.clientCode(TestDataProvider.invalidClientCode())
				.build();
		
		final AccountRegistrationDto registrationDto2WithSameUsername = AccountRegistrationDto.builder()
				.username(username)
				.dateOfBirth(dateOfBirthUser2)
				.email(emailUser2)
				.password(passwordUser2)
				.clientCode(TestDataProvider.invalidClientCode())
				.build();		

		registerClientAndCheckSuccess(client, registrationDto);
		
		// when
		mvc	.perform(
				post("/client/" + client.getClientId() +  "/register")
					.header("Origin", "*")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(registrationDto2WithSameUsername)))
			.andExpect(
					status().
					isBadRequest()).
			andExpect(
					content().
					contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
			.andReturn().getResponse().getContentAsString();
		
		
		// then check that login does not work with new account
		loginAndCheckSuccess(passwordUser1, username);
		checkLoginFails(passwordUser2, username);

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


	private void registerClientAndCheckSuccess(Client client, final AccountRegistrationDto registrationDto)
			throws UnsupportedEncodingException, Exception, JsonProcessingException {
		mvc	.perform(
				post("/client/" + client.getClientId() +  "/register")
					.header("Origin", "*")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(registrationDto)))
			.andExpect(
					status().
					isBadRequest()).
			andExpect(
					content().
					contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
			.andReturn().getResponse().getContentAsString();
	}	
	

	private UserDto callUserMeAndCheckSuccess(String token)
			throws UnsupportedEncodingException, Exception, IOException {
		String resultDtoStr = mvc
				.perform(
						get("/user/me").header("Origin", "*").header("Authorization", "Bearer " + token)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		UserDto resultDto = fromJson(resultDtoStr, UserDto.class);
		return resultDto;
	}

	private String loginAndCheckSuccess(final String password, final String username)
			throws UnsupportedEncodingException, Exception, IOException {
		// login with new account
		String requestbody = UserControllerIT.createLoginRequestBody(username, password);
		String jwtToken = mvc
				.perform(post("/login").header("Origin", "*").contentType(MediaType.APPLICATION_JSON)
						.content(requestbody))

				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		TokenResponse response = fromJson(jwtToken, TokenResponse.class);
		return response.getToken();
	}
	
	private void checkLoginFails(final String password, final String username)
			throws UnsupportedEncodingException, Exception, IOException {
		// login with new account
		String requestbody = UserControllerIT.createLoginRequestBody(username, password);
		mvc
			.perform(post("/login").header("Origin", "*").contentType(MediaType.APPLICATION_JSON)
					.content(requestbody))
			.andExpect(status().isUnauthorized());
	}



}