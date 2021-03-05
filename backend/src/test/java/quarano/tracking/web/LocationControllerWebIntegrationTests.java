package quarano.tracking.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

/**
 * @author David Bauknecht
 */
@QuaranoWebIntegrationTest
@WithQuaranoUser("test3")
@RequiredArgsConstructor
class LocationControllerWebIntegrationTests {

	private final MockMvc mvc;
	private final ObjectMapper mapper;
	private final MessageSourceAccessor messages;

	@Test
	void addLocationSuccess() throws Exception {
		LocationDto.LocationContactDto contactDto = new LocationDto.LocationContactDto();
		contactDto.setContactPersonEmail("praesident@sportvereinnulleins.de");
		contactDto.setContactPersonPhone("0918272711");
		contactDto.setContactPersonName("Michael Mustermann");
		var payload = new LocationDto();
		payload.setContactPerson(contactDto);
		payload.setCity("Musterstadt");
		payload.setStreet("Am Sportplatz");
		payload.setZipCode("12345");
		payload.setHouseNumber("1");
		payload.setComment("Auf dem Fussballplatz");

		String response = mvc.perform(post("/locations")
				.content(mapper.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.contactPerson.contactPersonName", String.class)).isEqualTo("Michael Mustermann");
		assertThat(document.read("$.contactPerson.contactPersonEmail", String.class)).isEqualTo("praesident@sportvereinnulleins.de");
		assertThat(document.read("$.contactPerson.contactPersonPhone", String.class)).isEqualTo("0918272711");
		assertThat(document.read("$.city", String.class)).isEqualTo("Musterstadt");
		assertThat(document.read("$.comment", String.class)).isEqualTo("Auf dem Fussballplatz");
	}

	@Test
	@WithQuaranoUser("admin")
	void getAllLocations() throws Exception {

		String response = mvc.perform(get("/hd/locations"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

	}
}
