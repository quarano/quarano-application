package quarano.masterdata.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.client.LinkDiscoverer;
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
@RequiredArgsConstructor
class EmailTextControllerWebIntegrationTests extends AbstractDocumentation {

	private final LinkDiscoverer discoverer;

	@Test
	void getAllTexts() throws Exception {

		var result = mvc.perform(get("/emailtexts")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(documentGetEmailTexts())
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Ihr Gesundheitsamt Mannheim", "diary-reminder", "new-contact-case",
				"registration-index", "registration-contact");
		assertThatResultHasEditLink(result);
	}

	@Test
	void getAllTextsEng() throws Exception {

		var result = mvc.perform(get("/emailtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.locale(Locale.UK))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Your Gesundheitsamt Mannheim", "diary-reminder", "new-contact-case",
				"registration-index", "registration-contact");
		assertThatResultHasEditLink(result);
	}

	@Test
	void getOneText() throws Exception {

		var result = mvc.perform(get("/emailtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.param("key", "diary-reminder"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Ihr Gesundheitsamt Mannheim", "diary-reminder");
		assertThatResultHasEditLink(result);
	}

	@Test
	void getOneTextEn() throws Exception {

		var result = mvc.perform(get("/emailtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.locale(Locale.UK)
				.param("key", "diary-reminder"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Your Gesundheitsamt Mannheim", "diary-reminder");

		var document = JsonPath.parse(result);
		var read = document.read("$._embedded.texts", JSONArray.class);
		assertThatResultHasEditLink(result);
	}

	@Test
	@WithQuaranoUser("agent1")
	void putOnlyAsAdmin() throws Exception {

		mvc.perform(get("/admin/emailtexts/diary-reminder")
				.content("Text"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithQuaranoUser("admin")
	void putText() throws Exception {

		mvc.perform(put("/admin/emailtexts/diary-reminder")
				.header(HttpHeaders.CONTENT_LANGUAGE, "de")
				.content("Text"))
				.andDo(documentPutEmailTexts())
				.andExpect(status().is2xxSuccessful());

		var result = mvc.perform(get("/emailtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.param("key", "diary-reminder"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Text", "diary-reminder");
	}

	@Test
	@WithQuaranoUser("admin")
	void putTextEn() throws Exception {

		mvc.perform(put("/admin/emailtexts/diary-reminder")
				.header(HttpHeaders.CONTENT_LANGUAGE, "en")
				.content("Text"))
				.andExpect(status().is2xxSuccessful());

		var result = mvc.perform(get("/emailtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.param("key", "diary-reminder")
				.param("lang", "en"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Text", "diary-reminder");
	}

	@Test
	@WithQuaranoUser("admin")
	void putTextWithWrongKey() throws Exception {

		mvc.perform(put("/admin/emailtexts/abc")
				.header(HttpHeaders.CONTENT_LANGUAGE, "de")
				.content("Text"))
				.andExpect(status().isNotFound());
	}

	void assertThatResultHasEditLink(String result) {
		var firstText = JsonPath.parse(result).read("$._embedded.texts[0]", Map.class);
		assertThat(discoverer.findLinkWithRel(IanaLinkRelations.EDIT, JsonPath.parse(firstText).jsonString())).isPresent();
	}

	static void assertThatResultContains(String result, String excampleString, Object... keys) {

		var document = JsonPath.parse(result);
		var read = document.read("$._embedded.texts", JSONArray.class);

		assertThat(read).extracting("key").contains(keys);
		assertThat(read).extracting("text", String.class)
				.noneMatch(ObjectUtils::isEmpty)
				.anyMatch(it -> it.contains(excampleString));
	}

	static ResultHandler documentGetEmailTexts() {

		return DocumentationFlow.of("email-texts")
				.withResponsePreprocessor(
						Preprocessors.replacePattern(Pattern.compile("\"text\" : \".*\""),
								"\"text\" : \"Some Text\""))
				.document("get-texts");
	}

	static ResultHandler documentPutEmailTexts() {
		return DocumentationFlow.of("email-texts").document("put-texts");
	}
}
