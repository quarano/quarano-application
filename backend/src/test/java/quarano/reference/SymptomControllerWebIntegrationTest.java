package quarano.reference;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.DocumentContext;
import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;

import java.util.EnumMap;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import quarano.core.support.Language;

@QuaranoWebIntegrationTest
@WithQuaranoUser("test3")
@RequiredArgsConstructor
class SymptomControllerWebIntegrationTest {

	private final MockMvc mvc;
	private final ObjectMapper mapper;

	@Test
	void readsSymptomsWithTranslations() throws Exception {

		var germanSymptoms = getSymptoms(Locale.GERMAN);
		var germanName = germanSymptoms.read("$.[0].name", String.class);

		var turkishSymptoms = getSymptoms(new Locale("tr"));
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

		assertThat(document.read("$.name", String.class)).isEqualTo("Dieses Feld darf nur Buchstaben enthalten!");
	}

	@Test
	void storesSymptomWithTranslations() throws Exception {
		var payload = new SymptomDto();
		payload.setId(UUID.randomUUID());
		payload.setName("Arterienverkalkung ");
		var translations = new EnumMap<Language, String>(Language.class);
		payload.setTranslations(translations);
		translations.put(Language.DE, "Arterienverkalkung");
		translations.put(Language.EN, "Hardening of the arteries");
		translations.put(Language.TR, "Arterlerin sertleşmesi");


		String response = mvc.perform(post("/api/symptoms")
				.content(mapper.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.translations.TR", String.class)).isEqualTo("Arterlerin sertleşmesi");
	}

	DocumentContext getSymptoms(Locale locale) throws Exception {

		var response = mvc.perform(get("/api/symptoms")
				.contentType(MediaType.APPLICATION_JSON)
				.locale(locale))
				.andReturn().getResponse().getContentAsString();
		return JsonPath.parse(response);
	}
}
