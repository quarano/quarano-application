package quarano.masterdata.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.util.StringUtils;

import com.jayway.jsonpath.JsonPath;

/**
 * @author Jens Kutzsche
 */
@QuaranoWebIntegrationTest
public class FrontendTextControllerWebIntegrationTests extends AbstractDocumentation {

	@Test
	void getAllTexts() throws Exception {

		var result = mvc.perform(get("/api/frontendtexts")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(documentGetFrontendTexts())
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(result);

		JSONArray read = document.read("$._embedded.texts", JSONArray.class);
		assertThat(read).extracting("key")
				.contains("terms", "data-protection", "imprint", "welcome-index", "welcome-contact");
		assertThat(read).extracting("text").noneMatch(StringUtils::isEmpty);
	}

	private static ResultHandler documentGetFrontendTexts() {
		return DocumentationFlow.of("frontend-texts").document("get-frontend-texts");
	}
}
