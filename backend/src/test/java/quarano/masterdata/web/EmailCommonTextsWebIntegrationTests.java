package quarano.masterdata.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import net.minidev.json.JSONArray;
import quarano.AbstractDocumentation;
import quarano.QuaranoWebIntegrationTest;

import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;

import com.jayway.jsonpath.JsonPath;

@QuaranoWebIntegrationTest
@SpringBootTest(properties = { "quarano.department.default-department.rkiCode=xx" })
class EmailCommonTextsWebIntegrationTests extends AbstractDocumentation {

	@Test
	void getOneText() throws Exception {

		var result = mvc.perform(get("/emailtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.param("key", "diary-reminder"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "bitte denken Sie an Ihren regelmäßigen Tagebucheintrag", "Ihr {departmentName}",
				"diary-reminder");
	}

	@Test
	void getOneTextEn() throws Exception {

		var result = mvc.perform(get("/emailtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.locale(Locale.UK)
				.param("key", "diary-reminder"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "please remember to record your regular diary entry", "Your {departmentName}",
				"diary-reminder");
	}

	@Test
	void getOneTextWithExplicitEnAndRawtext() throws Exception {

		var result = mvc.perform(get("/emailtexts")
				.contentType(MediaType.APPLICATION_JSON)
				.param("key", "diary-reminder")
				.param("lang", Locale.ENGLISH.toLanguageTag()))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		assertThatResultContains(result, "please remember to record your regular diary entry", "Your {departmentName}",
				"diary-reminder");
	}

	void assertThatResultContains(String result, String excampleString, String excampleString2, Object... keys) {

		var document = JsonPath.parse(result);
		var read = document.read("$._embedded.texts", JSONArray.class);

		assertThat(read).extracting("key").contains(keys);
		assertThat(read).extracting("text", String.class)
				.noneMatch(ObjectUtils::isEmpty)
				.anyMatch(it -> it.contains(excampleString))
				.anyMatch(it -> Optional.ofNullable(excampleString2).map(it::contains).orElse(Boolean.TRUE));
	}
}
