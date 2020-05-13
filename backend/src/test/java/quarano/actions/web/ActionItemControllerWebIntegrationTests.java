package quarano.actions.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPersonDataInitializer;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import com.jayway.jsonpath.JsonPath;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoWebIntegrationTest
class ActionItemControllerWebIntegrationTests {

	private final MockMvc mvc;
	private final TrackedCaseRepository cases;

	@Test
	@WithQuaranoUser("agent3")
	void obtainsAnomalysDetails() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2)
				.orElseThrow();

		var response = mvc.perform(get("/api/hd/actions/{id}", trackedCase.getId())) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.anomalies.process[0].date", String.class)) //
				.isEqualTo(LocalDate.now().toString());
	}
}
