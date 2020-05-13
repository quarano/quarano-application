package quarano.actions.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPersonDataInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

/**
 * @author Oliver Drotbohm
 */
@QuaranoWebIntegrationTest
@RequiredArgsConstructor
class ActionItemsControllerWebIntegrationTests {

	private final MockMvc mvc;
	private final TrackedCaseRepository cases;
	private final ObjectMapper jackson;

	@Test
	@WithQuaranoUser("agent3")
	void rendersAnomalies() throws Exception {

		var response = mvc.perform(get("/api/hd/actions")) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$[0].processSummary", JSONArray.class)).isNotEmpty();
		assertThat(document.read("$[0].healthSummary", JSONArray.class)).isNotEmpty();
	}

	@Test
	@WithQuaranoUser("agent3")
	void rendersAnomaly() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2)
				.orElseThrow();

		var response = mvc.perform(get("/api/hd/actions/{id}", trackedCase.getId())) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.comments", JSONArray.class)).isEmpty();
		assertThat(document.read("$.anomalies.resolved", JSONArray.class)).isEmpty();
	}

	@Test
	@WithQuaranoUser("agent3")
	void resolvesAnomalies() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2)
				.orElseThrow();

		ActionsReviewed reviewed = new ActionsReviewed();
		reviewed.setComment("Comment!");

		var response = mvc.perform(put("/api/hd/actions/{id}/resolve", trackedCase.getId()) //
				.content(jackson.writeValueAsString(reviewed)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.comments[0].comment", String.class)).isEqualTo(reviewed.getComment());
		assertThat(document.read("$.anomalies.resolved", JSONArray.class)).isNotEmpty();
		assertThat(document.read("$.anomalies.process", JSONArray.class)).isEmpty();
		assertThat(document.read("$.anomalies.health", JSONArray.class)).isEmpty();
	}
}
