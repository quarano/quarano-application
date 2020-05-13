package quarano.department.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.core.web.MapperWrapper;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;
import quarano.tracking.web.TrackedPersonDto;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

/**
 * @author Oliver Drotbohm
 */
@QuaranoWebIntegrationTest
@RequiredArgsConstructor
class EnrollmentWebIntegrationTests {

	private final MockMvc mvc;
	private final ObjectMapper jackson;
	private final TrackedPersonRepository repository;
	private final MapperWrapper mapper;	
	private final MessageSourceAccessor messages;

	@Test
	@WithQuaranoUser("DemoAccount")
	public void completeEnrollmentDetailsSubmissionMarksStepComplete() throws Exception {

		var person = repository.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON2_ID_DEP1).orElseThrow();
		var source = mapper.map(person, TrackedPersonDto.class);

		source.setStreet("Street");
		source.setZipCode("68199");
		source.setCity("Mannheim");

		// When all enrollment details were submitted
		mvc.perform(put("/api/enrollment/details") //
				.content(jackson.writeValueAsString(source)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		// The first enrollment step is completed
		String result = mvc.perform(get("/api/enrollment")) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		assertThat(JsonPath.parse(result).read("$.completedPersonalData", boolean.class)).isTrue();
	}
	
	@Test
	@WithQuaranoUser("DemoAccount")	
	void rejectsInvalidCharactersForStringFields() throws Exception {

		var person = repository.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON2_ID_DEP1).orElseThrow();
		var source = mapper.map(person, TrackedPersonDto.class);

		source.setFirstName("Michael 123") //
		.setLastName("Mustermann 123") //
		.setPhone("0123456789") //
		.setCity("city 123") //
		.setStreet("\\") //
		.setHouseNumber("-");

		// When all enrollment details were submitted
		var responseBody = mvc.perform(put("/api/enrollment/details") //
				.content(jackson.writeValueAsString(source)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isBadRequest()) //
				.andReturn().getResponse().getContentAsString();


		var document = JsonPath.parse(responseBody);

		var alphaNumeric = messages.getMessage("AlphaNumeric");
		var firstName = messages.getMessage("Pattern.firstName");
		var lastName = messages.getMessage("Pattern.lastName");

		assertThat(document.read("$.firstName", String.class)).isEqualTo(firstName);
		assertThat(document.read("$.lastName", String.class)).isEqualTo(lastName);
		assertThat(document.read("$.city", String.class)).contains("gültige Stadt");
		assertThat(document.read("$.street", String.class)).contains("gültige Straße");
		assertThat(document.read("$.houseNumber", String.class)).isEqualTo(alphaNumeric);
	}	
}
