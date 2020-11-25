package quarano.occasion.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
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
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@QuaranoWebIntegrationTest
class VisitorControllerWebIntegrationTests {

	private final MockMvc mockMvc;
	private final ObjectMapper objectMapper;

	private final LocalDate today = LocalDate.now();
	private final LocalTime now = LocalTime.now();

	@Test // CORE-631
	void submitsVisitorGroup() throws Exception {

		var occasionCode = OccasionDataInitializer.OCCASION_CODE_1;
		var visitor = new VisitorDto("firstname", "lastname", "street", "2", "12346", "city", "0123458796",
				"mail@mail.de", "Quali");

		var payLoad = new VisitorGroupDto(Collections.emptyList(), occasionCode.getOccasionCode(), "locationame",
				"comment", today, now, today.plusDays(1), now).setVisitors(List.of(visitor));

		mockMvc
				.perform(post("/ext/occasions/{occasionCode}", occasionCode)
						.content(objectMapper.writeValueAsString(payLoad))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent())
				.andReturn().getResponse().getContentAsString();
	}
}
