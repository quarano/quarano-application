package quarano.reference;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.user.LocaleConfiguration;

import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

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
				.locale(Locale.GERMANY)
				.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.name", String.class)).isEqualTo(messages.getMessage("Alphabetic", Locale.GERMANY));
	}

	DocumentContext getSymptoms(Locale locale) throws Exception {

		var response = mvc.perform(get("/api/symptoms")
				.contentType(MediaType.APPLICATION_JSON)
				.locale(locale))
				.andReturn().getResponse().getContentAsString();

		return JsonPath.parse(response);
	}
}
