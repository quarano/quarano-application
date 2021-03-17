package quarano.tracking.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
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
		payload.setName("Sportplatz 01");
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
	void getLocationsForUser() throws Exception {
		LocationDto.LocationContactDto contactDto = new LocationDto.LocationContactDto();
		contactDto.setContactPersonEmail("praesident@sportvereinnulleins.de");
		contactDto.setContactPersonPhone("0918272711");
		contactDto.setContactPersonName("Michael Mustermann");
		var payload = new LocationDto();
		payload.setContactPerson(contactDto);
		payload.setName("Sportplatz 01");
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

		LocationDto.LocationContactDto contactDto2 = new LocationDto.LocationContactDto();
		contactDto2.setContactPersonEmail("praesident@sgturnen.de");
		contactDto2.setContactPersonPhone("0918272712");
		contactDto2.setContactPersonName("Michaela Mustermann");
		var payload2 = new LocationDto();
		payload2.setName("Turnhalle");
		payload2.setContactPerson(contactDto2);
		payload2.setCity("Musterstadt");
		payload2.setStreet("An der Turnhalle");
		payload2.setZipCode("12345");
		payload2.setHouseNumber("1");
		payload2.setComment("In der Halle");

		mvc.perform(post("/locations")
				.content(mapper.writeValueAsString(payload2))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		String responseOfTest = mvc.perform(get("/locations")
				.content(mapper.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var documentContext = JsonPath.parse(responseOfTest);
		assertThat(documentContext.read("$", JSONArray.class)).hasSize(2);
	}
}
