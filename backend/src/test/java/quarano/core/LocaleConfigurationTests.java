package quarano.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_LANGUAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static quarano.core.web.QuaranoHttpHeaders.AUTH_TOKEN;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@QuaranoWebIntegrationTest
@RequiredArgsConstructor
public class LocaleConfigurationTests {

	static final String ME = "/api/user/me";

	static final String DEFAULT_LOCALE = "de-DE";

	final String USERNAME_WITH_LOCALE = "DemoAccount";
	final String PASSWORD_WITH_LOCALE = "DemoPassword";
	final String USERNAME_WITHOUT_LOCALE = "test3";
	final String PASSWORD_WITHOUT_LOCALE = "test123";

	final MockMvc mvc;
	final ObjectMapper mapper;

	@Test
	void testLocaleHandlingWithDefault() throws Exception {

		var response = login(USERNAME_WITHOUT_LOCALE, PASSWORD_WITHOUT_LOCALE, null);

		assertThat(response.getHeader(CONTENT_LANGUAGE)).isEqualTo(DEFAULT_LOCALE);

		var token = response.getHeader(AUTH_TOKEN);

		var responseGet = performGet(ME, token, Locale.KOREA); // unsupported language
		var document = JsonPath.parse(responseGet.getContentAsString());

		assertThat(responseGet.getHeader(CONTENT_LANGUAGE)).isEqualTo(DEFAULT_LOCALE);
		assertThat(document.read("$.client.locale", String.class)).isNull();

		var responsePut = performWrongPasswordChange(token, Locale.KOREA);
		document = JsonPath.parse(responsePut.getContentAsString());

		assertThat(responsePut.getHeader(CONTENT_LANGUAGE)).isEqualTo(DEFAULT_LOCALE);
		assertThat(document.read("$.current", String.class)).isEqualTo("Ungültiges aktuelles Passwort!");
	}

	@Test
	void testLocaleHandlingWithAcceptLanguageHeader() throws Exception {

		var response = login(USERNAME_WITHOUT_LOCALE, PASSWORD_WITHOUT_LOCALE, Locale.forLanguageTag("tr"));

		assertThat(response.getHeader(CONTENT_LANGUAGE)).isEqualTo("tr");

		var token = response.getHeader(AUTH_TOKEN);

		var locale = Locale.forLanguageTag("en");

		var responseGet = performGet(ME, token, locale);
		var document = JsonPath.parse(responseGet.getContentAsString());

		assertThat(responseGet.getHeader(CONTENT_LANGUAGE)).isEqualTo("en");
		assertThat(document.read("$.client.locale", String.class)).isNull();

		var responsePut = performWrongPasswordChange(token, locale);
		document = JsonPath.parse(responsePut.getContentAsString());

		assertThat(responsePut.getHeader(CONTENT_LANGUAGE)).isEqualTo("en");
		assertThat(document.read("$.current", String.class)).isEqualTo("Invalid current password!");
	}

	@Test
	void testLocaleHandlingWithUserSetting() throws Exception {

		var response = login(USERNAME_WITH_LOCALE, PASSWORD_WITH_LOCALE, null);

		assertThat(response.getHeader(CONTENT_LANGUAGE)).isEqualTo("en-GB");

		var token = response.getHeader(AUTH_TOKEN);

		var locale = Locale.forLanguageTag("tr");

		var responseGet = performGet(ME, token, locale);
		var document = JsonPath.parse(responseGet.getContentAsString());

		assertThat(responseGet.getHeader(CONTENT_LANGUAGE)).isEqualTo("en-GB");
		assertThat(document.read("$.client.locale", String.class)).isEqualTo("en_GB");

		var responsePut = performWrongPasswordChange(token, locale);
		document = JsonPath.parse(responsePut.getContentAsString());

		assertThat(responsePut.getHeader(CONTENT_LANGUAGE)).isEqualTo("en-GB");
		assertThat(document.read("$.current", String.class)).isEqualTo("Invalid current password!");
	}

	private HttpServletResponse login(String username, String password, Locale locale) throws Exception {

		return mvc.perform(post("/login")
				.header("Origin", "*")
				.locale(locale)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createRequestBody(username, password)))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse();
	}

	private String createRequestBody(String username, String password) throws Exception {
		return mapper.writeValueAsString(Map.of("username", username, "password", password));
	}

	private MockHttpServletResponse performGet(String urlTemplate, String token, Locale locale) throws Exception {

		return mvc.perform(get(urlTemplate)
				.header("Origin", "*")
				.header("Authorization", "Bearer " + token)
				.locale(locale)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
				.andReturn().getResponse();
	}

	private MockHttpServletResponse performWrongPasswordChange(String token, Locale locale) throws Exception {

		return mvc.perform(put("/api/user/me/password")
				.header("Origin", "*")
				.header("Authorization", "Bearer " + token)
				.locale(locale)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createRequestBody("xxxx#", "xxxx#", "xxxx#")))
				.andExpect(status().is4xxClientError())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
	}

	private String createRequestBody(String current, String password, String passwordConfirm) throws Exception {
		return mapper
				.writeValueAsString(Map.of("current", current, "password", password, "passwordConfirm", passwordConfirm));
	}
}
