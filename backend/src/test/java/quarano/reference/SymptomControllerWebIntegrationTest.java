package quarano.reference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import quarano.core.validation.Alphabetic;
import quarano.user.LocaleConfiguration;

@QuaranoWebIntegrationTest
@WithQuaranoUser("test3")
@RequiredArgsConstructor
class SymptomControllerWebIntegrationTest {

	private final MockMvc mvc;
	private final ObjectMapper mapper;
	private final MessageSourceAccessor messages;

	@Test
	void readsSymptomsWithTranslations() throws Exception {

		var germanSymptoms = getSymptoms(Locale.GERMAN);
		var germanName = germanSymptoms.read("$.[0].name", String.class);

		var turkishSymptoms = getSymptoms(LocaleConfiguration.TURKISH);
		var turkishName = turkishSymptoms.read("$.[0].name", String.class);

		assertThat(germanName.equals(turkishName)).isFalse();
	}

	@Test
	void rejectsSymptomWithNameContainingNumbersAndSpecialCharacters() throws Exception {

		var payload = new SymptomDto();
		payload.setId(UUID.randomUUID());
		payload.setName("some invalid name 1231 __\\");

		String response = mvc.perform(post("/api/symptoms")
				.content(mapper.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.name", String.class)).isEqualTo(messages.getMessage("Alphabetic"));
	}

	@Test
	void storesSymptomWithTranslations() throws Exception {

		var payload = new SymptomDto();
		payload.setId(UUID.randomUUID());
		payload.setName("Arterienverkalkung");
		var translations = new HashMap<Locale, String>();
		payload.setTranslations(translations);
		translations.put(Locale.GERMAN, "Arterienverkalkung");
		translations.put(Locale.ENGLISH, "Hardening of the arteries");
		translations.put(LocaleConfiguration.TURKISH, "Arterlerin sertleşmesi");

		var response = mvc.perform(post("/api/symptoms")
				.content(mapper.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.translations.tr", String.class)).isEqualTo("Arterlerin sertleşmesi");
	}

	DocumentContext getSymptoms(Locale locale) throws Exception {

		var response = mvc.perform(get("/api/symptoms")
				.contentType(MediaType.APPLICATION_JSON)
				.locale(locale))
				.andReturn().getResponse().getContentAsString();

		return JsonPath.parse(response);
	}
}
