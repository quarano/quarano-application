package quarano.occasion.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;
import quarano.occasion.OccasionDataInitializer;
import quarano.occasion.web.OccasionRepresentions.VisitorDto;
import quarano.occasion.web.OccasionRepresentions.VisitorGroupDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@QuaranoWebIntegrationTest
class VisitorControllerWebIntegrationTests extends AbstractDocumentation {

	private final ObjectMapper objectMapper;

	private final LocalDate today = LocalDate.now();
	private final LocalTime now = LocalTime.now();
	private final DocumentationFlow flow = DocumentationFlow.of("submit-visitors");

	@Test // CORE-631
	void submitsVisitorGroup() throws Exception {

		var occasionCode = OccasionDataInitializer.OCCASION_CODE_1;
		var visitor = new VisitorDto()
				.setFirstName("Max")
				.setLastName("Mustermann")
				.setStreet("Musterstraße")
				.setHouseNumber("1")
				.setZipCode("12346")
				.setCity("Musterstadt")
				.setPhone("0123458796")
				.setEmail("mail@mail.de")
				.setQualifier("Tisch 3");

		var group = new VisitorGroupDto(Collections.emptyList(), occasionCode.getOccasionCode(), "Musterbar",
				"comment", today, now, today.plusDays(1), now).setVisitors(List.of(visitor));

		mvc.perform(post("/ext/occasions/{occasionCode}", occasionCode)
				.content(objectMapper.writeValueAsString(group))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent())
				.andDo(flow.document("submit-visitors"))
				.andReturn().getResponse().getContentAsString();
	}
}
