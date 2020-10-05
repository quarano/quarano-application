package quarano.tracking.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@QuaranoWebIntegrationTest
@WithQuaranoUser("test3")
@RequiredArgsConstructor
class LocationWebIntegrationTest {

	private final MockMvc mvc;
	private final ObjectMapper mapper;

	private static final String streetName = "TestStr.";
	private static final String houseNumber = "123";
	private static final String cityName = "TestCity";
	private static final String zipCode = "98765";

	@Test
	void addLocationSuccess() throws Exception {
		var testLocation = new LocationDto(streetName, houseNumber, cityName, zipCode);

		var responseString = mvc.perform(post("/api/locations")
				.content(mapper.writeValueAsString(testLocation))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();

		var response = JsonPath.parse(responseString);
		assertThat(response.read("$.street", String.class)).isEqualTo(streetName);
		assertThat(response.read("$.houseNumber", String.class)).isEqualTo(houseNumber);
		assertThat(response.read("$.city", String.class)).isEqualTo(cityName);
		assertThat(response.read("$.zipCode", String.class)).isEqualTo(zipCode);
	}

	@Test
	void rejectLocationMissingAddress() throws Exception {
		var testLocation = new LocationDto("", "", "", "");

		mvc.perform(post("/api/locations")
				.content(mapper.writeValueAsString(testLocation))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse();
	}
}
