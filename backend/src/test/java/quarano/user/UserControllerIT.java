package quarano.user;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import quarano.util.TokenResponse;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public class UserControllerIT {

	@Autowired MockMvc mvc;
	@Autowired ObjectMapper mapper;

	@Test
	void testLoginWithValidCredentials() throws Exception {

		// Accounts and password created by dummy data input beans given
		var username = "DemoAccount";
		var password = "DemoPassword";

		// when
		String resultLogin = mvc.perform(post("/login") //
				.header("Origin", "*") //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(createLoginRequestBody(username, password))) //
				.andExpect(status().isOk()) //
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) //
				.andReturn().getResponse().getContentAsString();

		TokenResponse response = mapper.readValue(resultLogin, TokenResponse.class);

		// check if token is valid for authentication
		String resultDtoStr = mvc.perform(get("/api/user/me") //
				.header("Origin", "*") //
				.header("Authorization", "Bearer " + response.getToken()) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) //
				.andReturn().getResponse().getContentAsString();

		DocumentContext document = JsonPath.parse(resultDtoStr);

		assertThat(document.read("$.username", String.class)).isEqualTo("DemoAccount");
		assertThat(document.read("$.firstName", String.class)).isEqualTo("Markus");
		assertThat(document.read("$.lastName", String.class)).isEqualTo("Hanser");
	}

	@Test
	void testLoginWithInvalidCredentials() throws Exception {

		// Accounts and password created by dummy data input beans given
		var username = "DemoAccount";
		var password = "My-Wrong-Password";

		// when
		mvc.perform(post("/login") //
				.header("Origin", "*") //
				.contentType(MediaType.APPLICATION_JSON) //
				.content(createLoginRequestBody(username, password))) //
				.andExpect(status().isUnauthorized());
	}

	@Test
	void testThatPreflightRequestsGetNotBlocked() throws Exception {

		// check if token is valid for authentication
		mvc.perform(options("/client/me") //
				.header("Origin", "*") //
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.OPTIONS)) //
				.andExpect(status().isOk());
	}

	public String createLoginRequestBody(String username, String password) throws Exception {
		return mapper.writeValueAsString(Map.of("username", username, "password", password));
	}
}
