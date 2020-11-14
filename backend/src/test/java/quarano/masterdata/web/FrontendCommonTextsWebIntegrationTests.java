package quarano.masterdata.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import quarano.AbstractDocumentation;
import quarano.QuaranoWebIntegrationTest;

import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;

import com.jayway.jsonpath.JsonPath;

@QuaranoWebIntegrationTest
@SpringBootTest(properties = { "quarano.department.default-department.rkiCode=xx" })
@RequiredArgsConstructor
class FrontendCommonTextsWebIntegrationTests extends AbstractDocumentation {

	private final LinkDiscoverer discoverer;

	@Test
	void getOneText() throws Exception {

		var result = mvc.perform(get("/frontendtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.param("key", "welcome-index"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Sie haben vom GA Mannheim", "welcome-index");
		assertThatResultHasntEditLink(result);
	}

	@Test
	void getOneTextEn() throws Exception {

		var result = mvc.perform(get("/frontendtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.locale(Locale.UK)
				.param("key", "welcome-index"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Your GA Mannheim", "welcome-index");
		assertThatResultHasntEditLink(result);
	}

	@Test
	void getOneTextWithExplicitEnAndRawtext() throws Exception {

		var result = mvc.perform(get("/frontendtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.param("key", "welcome-index")
				.param("lang", Locale.ENGLISH.toLanguageTag())
				.param("rawtext", "true"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "Your {departmentName}", "welcome-index");
		assertThatResultHasEditLink(result);
	}

	void assertThatResultContains(String result, String excampleString, Object... keys) {

		var document = JsonPath.parse(result);
		var read = document.read("$._embedded.texts", JSONArray.class);

		assertThat(read).extracting("key").contains(keys);
		assertThat(read).extracting("text", String.class)
				.noneMatch(ObjectUtils::isEmpty)
				.anyMatch(it -> it.contains(excampleString));
	}

	void assertThatResultHasEditLink(String result) {
		var firstText = JsonPath.parse(result).read("$._embedded.texts[0]", Map.class);
		assertThat(discoverer.findLinkWithRel(IanaLinkRelations.EDIT, JsonPath.parse(firstText).jsonString())).isPresent();
	}

	void assertThatResultHasntEditLink(String result) {
		var firstText = JsonPath.parse(result).read("$._embedded.texts[0]", Map.class);
		assertThat(discoverer.findLinkWithRel(IanaLinkRelations.EDIT, JsonPath.parse(firstText).jsonString()))
				.isNotPresent();
	}
}
