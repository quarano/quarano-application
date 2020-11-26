package quarano.occasion.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.department.TrackedCaseDataInitializer;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@QuaranoWebIntegrationTest
class OccasionControllerWebIntegrationTests extends AbstractDocumentation {

	private final ObjectMapper objectMapper;
	private final DocumentationFlow flow = DocumentationFlow.of("create-occasions");

	@Test // CORE-613
	@WithQuaranoUser("admin")
	void createOccasionTest() throws Exception {
		OccasionsDto payload = new OccasionsDto("Omas 80. Geburtstag", LocalDateTime.now().minusDays(7),
				LocalDateTime.now().minusDays(6));
		var respones = mvc.perform(post("/hd/cases/{id}/occasions", TrackedCaseDataInitializer.TRACKED_CASE_MARKUS)
				.content(objectMapper.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(flow.document("create-occasions"))
				.andReturn().getResponse().getContentAsString();

		DocumentContext parseDoc = JsonPath.parse(respones);
		assertThat(parseDoc.read("$.occasionCode", String.class)).isNotBlank();

		mvc.perform(get("/hd/occasions")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print());

	}

}
