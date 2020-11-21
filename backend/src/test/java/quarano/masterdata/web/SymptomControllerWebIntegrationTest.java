package quarano.masterdata.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

/**
 * Integration tests for {@link SymptomController}.
 *
 * @author Oliver Drotbohm
 * @author Paul Guhl
 * @author Jens Kutzsche
 */
@QuaranoWebIntegrationTest
@WithQuaranoUser("test3")
@RequiredArgsConstructor
class SymptomControllerWebIntegrationTest {

	private final MockMvc mvc;

	@Test // CORE-419
	void readsSymptomsWithTranslations() throws Exception {

		var germanSymptoms = getSymptoms(Locale.GERMAN);
		var germanName = germanSymptoms.read("$.[0].name", String.class);

		var turkishSymptoms = getSymptoms(Locale.ENGLISH);
		var turkishName = turkishSymptoms.read("$.[0].name", String.class);

		assertThat(germanName.equals(turkishName)).isFalse();
	}

	private DocumentContext getSymptoms(Locale locale) throws Exception {

		var response = mvc.perform(get("/api/symptoms")
				.contentType(MediaType.APPLICATION_JSON)
				.locale(locale))
				.andReturn().getResponse().getContentAsString();

		return JsonPath.parse(response);
	}
}
