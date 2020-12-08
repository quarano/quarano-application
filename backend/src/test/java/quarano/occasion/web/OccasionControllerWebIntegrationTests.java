package quarano.occasion.web;

import static capital.scalable.restdocs.AutoDocumentation.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.department.TrackedCaseDataInitializer;
import quarano.occasion.web.OccasionRepresentions.OccasionsDto;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

/**
 * Integration tests for {@link OccasionController}.
 *
 * @author David Bauknecht
 * @author Oliver Drotbohm
 * @since 1.4
 */
@RequiredArgsConstructor
@QuaranoWebIntegrationTest
class OccasionControllerWebIntegrationTests extends AbstractDocumentation {

	private final ObjectMapper objectMapper;
	private final DocumentationFlow flow = DocumentationFlow.of("create-occasions");

	@Test // CORE-613
	@WithQuaranoUser("admin")
	void createOccasionTest() throws Exception {

		var now = LocalDateTime.now();
		var payload = new OccasionsDto("Omas 80. Geburtstag", now.minusDays(7), now.minusDays(6));

		var respones = mvc.perform(post("/hd/cases/{id}/occasions", TrackedCaseDataInitializer.TRACKED_CASE_MARKUS)
				.content(objectMapper.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(flow.document("create-occasion",
						responseFields().responseBodyAsType(OccasionRepresentions.OccasionSummary.class)))
				.andReturn().getResponse().getContentAsString();

		var parseDoc = JsonPath.parse(respones);

		assertThat(parseDoc.read("$.occasionCode", String.class)).isNotBlank();
	}
}
