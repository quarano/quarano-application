package quarano.masterdata.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import net.minidev.json.JSONArray;
import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;

import java.util.Locale;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.util.ObjectUtils;

import com.jayway.jsonpath.JsonPath;

/**
 * @author Jens Kutzsche
 */
@QuaranoWebIntegrationTest
class FrontendTextControllerWebIntegrationTests extends AbstractDocumentation {

	@Test
	void getAllTexts() throws Exception {

		var result = mvc.perform(get("/frontendtexts")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(documentGetFrontendTexts())
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Sie haben vom Gesundheitsamt Mannheim", "terms", "data-protection", "imprint",
				"welcome-index",
				"welcome-contact");
	}

	@Test
	void getAllTextsEng() throws Exception {

		var result = mvc.perform(get("/frontendtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.locale(Locale.UK))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "You have received", "terms", "data-protection", "imprint", "welcome-index",
				"welcome-contact");
	}

	@Test
	void getOneText() throws Exception {

		var result = mvc.perform(get("/frontendtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.param("key", "welcome-index"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Sie haben vom Gesundheitsamt Mannheim", "welcome-index");
	}

	@Test
	void getOneTextEn() throws Exception {

		var result = mvc.perform(get("/frontendtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.locale(Locale.UK)
				.param("key", "welcome-index"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Your health agency Mannheim", "welcome-index");
	}

	@Test
	@WithQuaranoUser("agent1")
	void putOnlyAsAdmin() throws Exception {

		mvc.perform(get("/frontendtexts/terms")
				.content("Text"))
				.andExpect(status().isForbidden());

		mvc.perform(get("/admin/frontendtexts/terms")
				.content("Text"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithQuaranoUser("admin")
	void putText() throws Exception {

		mvc.perform(put("/admin/frontendtexts/terms")
				.header(HttpHeaders.CONTENT_LANGUAGE, "de")
				.content("Text"))
				.andDo(documentPutFrontendTexts())
				.andExpect(status().is2xxSuccessful());

		var result = mvc.perform(get("/frontendtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.param("key", "terms"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Text", "terms");
	}

	@Test
	@WithQuaranoUser("admin")
	void putTextEn() throws Exception {

		mvc.perform(put("/admin/frontendtexts/terms")
				.header(HttpHeaders.CONTENT_LANGUAGE, "en")
				.content("Text"))
				.andExpect(status().is2xxSuccessful());

		var result = mvc.perform(get("/frontendtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.param("key", "terms")
				.param("lang", "en"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Text", "terms");
	}

	@Test
	@WithQuaranoUser("admin")
	void putTextWithWrongKey() throws Exception {

		mvc.perform(put("/admin/frontendtexts/abc")
				.header(HttpHeaders.CONTENT_LANGUAGE, "de")
				.content("Text"))
				.andExpect(status().isNotFound());
	}

	static void assertThatResultContains(String result, String excampleString, Object... keys) {

		var document = JsonPath.parse(result);
		var read = document.read("$._embedded.texts", JSONArray.class);

		assertThat(read).extracting("key").contains(keys);
		assertThat(read).extracting("text", String.class)
				.noneMatch(ObjectUtils::isEmpty)
				.anyMatch(it -> it.contains(excampleString));
	}

	static ResultHandler documentGetFrontendTexts() {

		return DocumentationFlow.of("frontend-texts")
				.withResponsePreprocessor(
						Preprocessors.replacePattern(Pattern.compile("\"text\" : \".*\""),
								"\"text\" : \"<h1>Some HTML</h1><p>\u2026</p>\""))
				.document("get-texts");
	}

	static ResultHandler documentPutFrontendTexts() {
		return DocumentationFlow.of("frontend-texts").document("put-texts");
	}
}
