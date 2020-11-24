package quarano.event.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import quarano.QuaranoWebIntegrationTest;
import quarano.event.EventCode;
import quarano.event.EventDataInitializer;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@QuaranoWebIntegrationTest
public class VisitorControllerWebIntegrationTests {

	private final MockMvc mockMvc;
	private final ObjectMapper objectMapper;

	@Test
	public void test() throws Exception {
		EventCode eventCode = EventDataInitializer.EVENT_CODE_1;

		VisitorDto visitorDto = new VisitorDto("firstname", "lastname", "street", "2", "12346", "city", "0123458796",
				"mail@mail.de", "Quali");

		VisitorTransmissionDto payLoad = new VisitorTransmissionDto(null, eventCode.getEventCode(), "locationame",
				"comment", LocalDate.now(), LocalTime.now(), LocalDate.now(), LocalTime.now());

		var content = mockMvc
				.perform(post("/ext/event/{eventCode}", eventCode)
						.content(objectMapper.writeValueAsString(payLoad))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
	}
}
