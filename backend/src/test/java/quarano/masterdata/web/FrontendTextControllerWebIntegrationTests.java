package quarano.masterdata.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import net.minidev.json.JSONArray;
import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;

import java.util.Locale;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.util.StringUtils;

import com.jayway.jsonpath.JsonPath;

/**
 * @author Jens Kutzsche
 */
@QuaranoWebIntegrationTest
class FrontendTextControllerWebIntegrationTests extends AbstractDocumentation {

	@Test
	void getAllTexts() throws Exception {

		var result = mvc.perform(get("/api/frontendtexts")
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

		var result = mvc.perform(get("/api/frontendtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.locale(Locale.UK))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "You have received", "terms", "data-protection", "imprint", "welcome-index",
				"welcome-contact");
	}

	@Test
	void getOneText() throws Exception {

		var result = mvc.perform(get("/api/frontendtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.param("key", "welcome-index"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Sie haben vom Gesundheitsamt Mannheim", "welcome-index");
	}

	@Test
	void getOneTextEn() throws Exception {

		var result = mvc.perform(get("/api/frontendtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.locale(Locale.UK)
				.param("key", "welcome-index"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Your health agency Mannheim", "welcome-index");
	}

	static void assertThatResultContains(String result, String excampleString, Object... keys) {

		var document = JsonPath.parse(result);
		var read = document.read("$._embedded.texts", JSONArray.class);

		assertThat(read).extracting("key").contains(keys);
		assertThat(read).extracting("text", String.class)
				.noneMatch(StringUtils::isEmpty)
				.anyMatch(it -> it.contains(excampleString));
	}

	static ResultHandler documentGetFrontendTexts() {

		return DocumentationFlow.of("frontend-texts")
				.withResponsePreprocessor(
						Preprocessors.replacePattern(Pattern.compile("\"text\" : \".*\""),
								"\"text\" : \"<h1>Some HTML</h1><p>\u2026</p>\""))
				.document("get-frontend-texts");
	}

	@SpringBootTest(properties = { "quarano.department.default-department.rkiCode=xx" })
	static class WithoutCommonTextsTests extends AbstractDocumentation {

		@Test
		void getOneText() throws Exception {

			var result = mvc.perform(get("/api/frontendtexts")
					.contentType(MediaType.APPLICATION_JSON)
					.param("key", "welcome-index"))
					.andExpect(status().is2xxSuccessful())
					.andReturn().getResponse().getContentAsString();

			assertThatResultContains(result, "Sie haben vom GA Mannheim", "welcome-index");
		}

		@Test
		void getOneTextEn() throws Exception {

			var result = mvc.perform(get("/api/frontendtexts")
					.contentType(MediaType.APPLICATION_JSON)
					.locale(Locale.UK)
					.param("key", "welcome-index"))
					.andExpect(status().is2xxSuccessful())
					.andReturn().getResponse().getContentAsString();

			assertThatResultContains(result, "Your GA Mannheim", "welcome-index");
		}
	}
}
