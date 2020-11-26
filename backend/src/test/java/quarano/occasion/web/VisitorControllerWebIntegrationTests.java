package quarano.occasion.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.occasion.OccasionDataInitializer;
import quarano.occasion.web.OccasionRepresentions.VisitorDto;
import quarano.occasion.web.OccasionRepresentions.VisitorGroupDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@QuaranoWebIntegrationTest
class VisitorControllerWebIntegrationTests extends AbstractDocumentation {

	private final ObjectMapper objectMapper;

	private final LocalDate today = LocalDate.now();
	private final LocalTime now = LocalTime.now();
	private final DocumentationFlow flow = DocumentationFlow.of("submit-visitors");

	@Test // CORE-631
	@WithQuaranoUser("admin")
	void submitsVisitorGroup() throws Exception {

		var occasionCode = OccasionDataInitializer.OCCASION_CODE_1;
		var visitor = new VisitorDto("Musterman", "Max", "Musterstraße", "1", "12346", "Musterstadt", "0123458796",
				"mail@mail.de", "Tisch 3");

		var payLoad = new VisitorGroupDto(Collections.emptyList(), occasionCode.getOccasionCode(), "Musterbar",
				"comment", today, now, today.plusDays(1), now).setVisitors(List.of(visitor));

		mvc.perform(post("/ext/occasions/{occasionCode}", occasionCode)
						.content(objectMapper.writeValueAsString(payLoad))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent())
				.andDo(flow.document("submit-visitors"))
				.andReturn().getResponse().getContentAsString();

		mvc.perform(get("/hd/occasions")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print());

	}
}
