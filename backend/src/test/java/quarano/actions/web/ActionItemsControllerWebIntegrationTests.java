package quarano.actions.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.actions.DescriptionCode;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPersonDataInitializer;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

/**
 * @author Oliver Drotbohm
 * @author Felix Schultze
 */
@QuaranoWebIntegrationTest
@RequiredArgsConstructor
class ActionItemsControllerWebIntegrationTests {

	private final MockMvc mvc;
	private final TrackedCaseRepository cases;
	private final ObjectMapper jackson;

	@Test
	@WithQuaranoUser("agent3")
	void obtainsAnomalysDetails() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2)
				.orElseThrow();

		var response = mvc.perform(get("/api/hd/actions/{id}", trackedCase.getId()))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.anomalies.process[0].date", String.class))
				.isEqualTo(LocalDate.now().toString());
	}

	@Test
	@WithQuaranoUser("agent3")
	void rendersAnomaly() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2)
				.orElseThrow();

		var response = mvc.perform(get("/api/hd/actions/{id}", trackedCase.getId()))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.comments", JSONArray.class)).isEmpty();
		assertThat(document.read("$.anomalies.health", JSONArray.class)).hasSize(1);
		assertThat(document.read("$.anomalies.resolved", JSONArray.class)).hasSize(1);
		assertThat(document.read("$.anomalies.resolved[0].items", JSONArray.class)).hasSize(2);
	}

	@Test
	@WithQuaranoUser("agent3")
	void resolvesAnomaliesManually() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2)
				.orElseThrow();

		ActionsReviewed reviewed = new ActionsReviewed();
		reviewed.setComment("Comment!");

		var response = mvc.perform(put("/api/hd/actions/{id}/resolve", trackedCase.getId())
				.content(jackson.writeValueAsString(reviewed))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.comments[0].comment", String.class)).isEqualTo(reviewed.getComment());
		assertThat(document.read("$.anomalies.resolved", JSONArray.class)).isNotEmpty();
		assertThat(document.read("$.anomalies.process", JSONArray.class)).isEmpty();
		assertThat(document.read("$.anomalies.health", JSONArray.class)).isEmpty();
	}

	@Test // CORE-102
	@WithQuaranoUser("agent3")
	void resolvesAnomaliesManuallyDoesNotWorkForSystemAnomalies() throws Exception {

		var actionsResponse = mvc.perform(get("/api/hd/actions"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var actionsDocument = JsonPath.parse(actionsResponse);
		var contactCaseId = actionsDocument.read("$[1].caseId", String.class);

		ActionsReviewed reviewed = new ActionsReviewed()
				.setComment("Comment!");

		var response = mvc.perform(put("/api/hd/actions/{id}/resolve", contactCaseId)
				.content(jackson.writeValueAsString(reviewed))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.comments[0].comment", String.class)).isEqualTo(reviewed.getComment());
		assertThat(document.read("$.anomalies.resolved", JSONArray.class)).isEmpty();
		assertThat(document.read("$.anomalies.process", JSONArray.class)).isNotEmpty();
		assertThat(document.read("$.anomalies.health", JSONArray.class)).isEmpty();

		var model = jackson.readValue(response, RepresentationModel.class);

		assertThat(model.getLinks()).isEmpty();
	}

	@Test
	@WithQuaranoUser("agent3")
	void obtainsUnresolvedActions() throws Exception {
		// resolv actions
		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2)
				.orElseThrow();

		ActionsReviewed reviewed = new ActionsReviewed();
		reviewed.setComment("Comment!");

		mvc.perform(put("/api/hd/actions/{id}/resolve", trackedCase.getId())
				.content(jackson.writeValueAsString(reviewed))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var response = mvc.perform(get("/api/hd/actions"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$", JSONArray.class)).hasSize(2);
		assertThat(document.read("$[0].healthSummary", JSONArray.class)).isEqualTo(List.of());
		assertThat(document.read("$[0].processSummary", JSONArray.class))
				.isEqualTo(List.of(DescriptionCode.MISSING_DETAILS_CONTACT.name()));
		assertThat(document.read("$[1].healthSummary", JSONArray.class)).isEqualTo(List.of());
		assertThat(document.read("$[1].processSummary", JSONArray.class))
				.isEqualTo(List.of(DescriptionCode.MISSING_DETAILS_CONTACT.name()));
	}

	@Test // CORE-224
	@WithQuaranoUser("agent1")
	void getActionsDoesNotReturnConcludedCases() throws Exception {

		// get an active case with open actions
		var actionsResponse = mvc.perform(get("/api/hd/actions"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var actionsDocument = JsonPath.parse(actionsResponse);
		var contactCaseId = actionsDocument.read("$[1].caseId", String.class);

		// conclude the case
		TrackedCaseIdentifier caseIdentifier = TrackedCaseIdentifier.of(UUID.fromString(contactCaseId));
		var trackedCase = cases.findById(caseIdentifier).get();

		trackedCase.conclude();
		cases.save(trackedCase);

		// request list again
		var actionsResponseAfterConclude = mvc.perform(get("/api/hd/actions"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var cases = JsonPath.parse(actionsResponseAfterConclude).read("$..caseId", JSONArray.class);

		// check that case is not cntained anymore
		assertThat(cases).doesNotContain(contactCaseId);


	}
}
