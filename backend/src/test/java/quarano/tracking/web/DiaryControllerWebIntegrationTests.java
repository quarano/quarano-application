package quarano.tracking.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.tracking.DiaryManagement;
import quarano.tracking.Slot;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.web.DiaryRepresentations.DiaryEntryInput;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tngtech.archunit.thirdparty.com.google.common.net.HttpHeaders;

/**
 * @author Oliver Drotbohm
 */
@QuaranoWebIntegrationTest
@RequiredArgsConstructor
class DiaryControllerWebIntegrationTests {

	private final MockMvc mvc;
	private final DiaryManagement entries;
	private final ObjectMapper jackson;

	@Test
	@WithQuaranoUser("test3")
	void createsDiaryEntry() throws Exception {

		var slot = Slot.now();
		var payload = new DiaryEntryInput(slot) //
				.setBodyTemperature(42.0f);

		var response = mvc.perform(post("/api/diary") //
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isCreated()) //
				.andReturn().getResponse();

		var location = response.getHeader(HttpHeaders.LOCATION);

		var document = JsonPath.parse(response.getContentAsString());

		assertThat(location).endsWith(document.read("$.id", String.class));
		assertThat(document.read("$.slot.timeOfDay", String.class)).isEqualToIgnoringCase(slot.getTimeOfDay().toString());
		assertThat(document.read("$.slot.date", String.class)).isEqualTo(slot.getDate().toString());
		assertThat(document.read("$.bodyTemperature", float.class)).isEqualTo(42.0f);

		mvc.perform(get(location)) //
				.andExpect(status().isOk());
	}

	@Test
	@WithQuaranoUser("test3")
	void updatesDiaryEntry() throws Exception {

		var entry = entries.findDiaryFor(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2) //
				.iterator().next();
		var slot = entry.getSlot();

		DiaryEntryInput payload = new DiaryEntryInput(slot) //
				.setBodyTemperature(42.0f);

		String response = mvc.perform(put("/api/diary/{identifier}", entry.getId()) //
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.id", String.class)).isEqualTo(entry.getId().toString());
		assertThat(document.read("$.slot.timeOfDay", String.class)).isEqualToIgnoringCase(slot.getTimeOfDay().toString());
		assertThat(document.read("$.slot.date", String.class)).isEqualTo(slot.getDate().toString());
		assertThat(document.read("$.bodyTemperature", float.class)).isEqualTo(42.0f);
	}
}
