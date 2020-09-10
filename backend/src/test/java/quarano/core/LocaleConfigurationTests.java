package quarano.core;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static quarano.core.web.QuaranoHttpHeaders.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import quarano.user.LocaleConfiguration;

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
	final TrackedPersonRepository persons;

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

		var response = login(USERNAME_WITHOUT_LOCALE, PASSWORD_WITHOUT_LOCALE, LocaleConfiguration.TURKISH);

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

		var locale = LocaleConfiguration.TURKISH;

		var responseGet = performGet(ME, token, locale);
		var document = JsonPath.parse(responseGet.getContentAsString());

		assertThat(responseGet.getHeader(CONTENT_LANGUAGE)).isEqualTo("en-GB");
		assertThat(document.read("$.client.locale", String.class)).isEqualTo("en_GB");

		var responsePut = performWrongPasswordChange(token, locale);
		document = JsonPath.parse(responsePut.getContentAsString());

		assertThat(responsePut.getHeader(CONTENT_LANGUAGE)).isEqualTo("en-GB");
		assertThat(document.read("$.current", String.class)).isEqualTo("Invalid current password!");
	}

	@WithQuaranoUser(USERNAME_WITHOUT_LOCALE)
	@ParameterizedTest
	@ValueSource(strings = { "de", "de_DE", "en", "en_GB", "tr", "tr_TR", "en_CA" })
	void processCorrectLocalesSucceeds(String locale) throws Exception {

		mvc.perform(get("/api/user/me")
				.header("Origin", "*")
				.locale(Locale.forLanguageTag("tr"))
				.param("locale", locale))
				.andExpect(status().is2xxSuccessful());

		assertThat(persons.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2))
				.isPresent()
				.map(TrackedPerson::getLocale)
				.map(Locale::toString)
				.hasValue(locale);
	}

	@WithQuaranoUser(USERNAME_WITHOUT_LOCALE)
	@ParameterizedTest
	@ValueSource(strings = { "en,de", "en_US,tr_TR" })
	void processInvalidLocalesRejects(String locale) throws Exception {

		var exception = assertThrows(NestedServletException.class, () -> {
			mvc.perform(post("/login")
					.header("Origin", "*")
					.param("locale", locale)
					.contentType(MediaType.APPLICATION_JSON)
					.content(createRequestBody(USERNAME_WITHOUT_LOCALE, PASSWORD_WITHOUT_LOCALE)))
					.andExpect(status().is4xxClientError());
		});

		assertThat(exception.getCause()).isInstanceOf(IllegalArgumentException.class);
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
