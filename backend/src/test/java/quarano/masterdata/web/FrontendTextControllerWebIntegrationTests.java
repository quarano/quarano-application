package quarano.masterdata.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import net.minidev.json.JSONArray;
import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
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

		var result = mvc.perform(get("/frontendtexts")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(documentGetFrontendTexts())
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(result);
		var read = document.read("$._embedded.texts", JSONArray.class);

		assertThat(read).extracting("key")
				.contains("terms", "data-protection", "imprint", "welcome-index", "welcome-contact");
		assertThat(read).extracting("text").noneMatch(StringUtils::isEmpty);
	}

	private static ResultHandler documentGetFrontendTexts() {

		return DocumentationFlow.of("frontend-texts")
				.withResponsePreprocessor(
						Preprocessors.replacePattern(Pattern.compile("\"text\" : \".*\""),
								"\"text\" : \"<h1>Some HTML</h1><p>\u2026</p>\""))
				.document("get-frontend-texts");
	}
}
