/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.department.web;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static quarano.department.web.TrackedCaseLinkRelations.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.ValidationUtils;
import quarano.WithQuaranoUser;
import quarano.department.CaseType;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseDataInitializer;
import quarano.department.TrackedCaseProperties;
import quarano.department.TrackedCaseRepository;
import quarano.department.web.TrackedCaseRepresentations.CommentInput;
import quarano.department.web.TrackedCaseRepresentations.TrackedCaseDto;
import quarano.department.web.TrackedCaseRepresentations.ValidationGroups.Index;
import quarano.tracking.TrackedPersonDataInitializer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import javax.validation.groups.Default;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

/**
 * @author Oliver Drotbohm
 * @author Patrick Otto
 */
@WithQuaranoUser("agent1")
@QuaranoWebIntegrationTest
@RequiredArgsConstructor
class TrackedCaseControllerWebIntegrationTests {

	private final MockMvc mvc;
	private final ObjectMapper jackson;
	private final TrackedCaseProperties configuration;
	private final ValidationUtils validator;
	private final TrackedCaseRepository cases;
	private final TrackedCaseRepresentations representations;
	private final MessageSourceAccessor messages;
	private final LinkDiscoverer discoverer;
	private final ModelMapper modelMapper;

	@Test
	void successfullyCreatesNewTrackedIndexCase() throws Exception {

		var payload = createMinimalIndexPayload();

		var response = issueCaseCreation(payload, CaseType.INDEX).getContentAsString();
		var document = JsonPath.parse(response);

		assertMinimalFieldsSet(document, payload);
		assertThat(discoverer.findLinkWithRel(CONCLUDE, response)).isPresent();
	}

	@Test
	void indicateStartTrackingIfRequiredDataIsSet() throws Exception {

		var payload = createMinimalIndexPayload() //
				.setEmail("foo@bar.com") //
				.setDateOfBirth(LocalDate.now().minusYears(25));

		var response = issueCaseCreation(payload, CaseType.INDEX).getContentAsString();
		var document = JsonPath.parse(response);

		assertMinimalFieldsSet(document, payload);

		Stream.of(START_TRACKING, CONCLUDE).forEach(it -> {
			assertThat(discoverer.findLinkWithRel(it, response)).isPresent();
		});
	}

	@Test
	void successfullyUpdatesIndexCaseWithMinimalPayload() throws Exception {

		var payload = createMinimalIndexPayload();
		var response = issueCaseCreation(payload, CaseType.INDEX);

		String location = response.getHeader(HttpHeaders.LOCATION);

		response = mvc.perform(put(location) //
				.content(jackson.writeValueAsString(payload.setInfected(true))) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse();

		var document = JsonPath.parse(response.getContentAsString());

		assertMinimalFieldsSet(document, payload);
		assertThat(document.read("$.infected", boolean.class)).isTrue();
	}
	
	@Test
	void successfullyTransformContactCaseToIndexCase() throws Exception {

		// get contact case Tanja and turn it into dto
		var contactCaseTanja = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1).get();
		TrackedCaseDto caseTanjaAsDto =  new TrackedCaseDto();
		modelMapper.map(contactCaseTanja, caseTanjaAsDto);
		
		// set necessary fields to be a valid index case
		caseTanjaAsDto.setInfected(true);
		caseTanjaAsDto.setTestDate(LocalDate.now());
		caseTanjaAsDto.setPhone("4456465465");
		caseTanjaAsDto.setQuarantineStartDate(LocalDate.now());
		caseTanjaAsDto.setQuarantineEndDate(LocalDate.now().plusDays(14));
		
		// start the transofrmation
		var response = issueToIndexCaseTransformation(caseTanjaAsDto);
		
		// check response
		var document = JsonPath.parse(response.getContentAsString());
		assertMinimalFieldsSet(document, caseTanjaAsDto);
		assertThat(document.read("$.infected", boolean.class)).isTrue();
		
		// check if case has been stored correctly
		var contactCaseTanjaAfterTransformation = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1).get();
		assertThat(contactCaseTanjaAfterTransformation.getType() == CaseType.INDEX);
	}
	
	@Test
	void rejectTransformContactCaseWhenInfoMissing() throws Exception {

		// get contact case Tanja and turn it into dto
		var contactCaseTanja = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1).get();
		TrackedCaseDto caseTanjaAsDto =  new TrackedCaseDto();
		modelMapper.map(contactCaseTanja, caseTanjaAsDto);
		
		// test date is missing
		caseTanjaAsDto.setInfected(true);
		caseTanjaAsDto.setPhone("4456465465");
		caseTanjaAsDto.setQuarantineStartDate(LocalDate.now());
		caseTanjaAsDto.setQuarantineEndDate(LocalDate.now().plusDays(14));
		
		// start the transoformation
		var response = expectBadRequestOnTransformationCall(caseTanjaAsDto, contactCaseTanja.getId());
		
		// response should contain  error message for field testDate
		var document = JsonPath.parse(response.getContentAsString());
		assertThat(document.read("$.testDate", String.class)).isNotNull();
	}
	
	@Test
	void rejectCreationOfContactCaseWithPositiveTestResult() throws Exception {

		var payload = createMinimalContactPayload();
		payload.setTestDate(LocalDate.now().minusDays(8));
		payload.setInfected(true);
		
		var response = expectBadRequest(HttpMethod.POST, "/api/hd/cases", payload);
		
		var document = JsonPath.parse(response);
		assertThat(document.read("$.testDate", String.class)).isNotNull();

	}
	
	@Test
	void rejectUpdateOfContactCaseWithPositiveTestResult() throws Exception {

		var payload = createMinimalContactPayload();
		
		var response = issueCaseCreation(payload, CaseType.CONTACT);
		
		var document = JsonPath.parse(response.getContentAsString());
		var caseId = document.read("$.caseId", String.class);
		
		payload.setTestDate(LocalDate.now().minusDays(8));
		payload.setInfected(true);

		response = expectBadRequestOnUpdate(payload, CaseType.CONTACT, TrackedCaseIdentifier.of(UUID.fromString(caseId)));

		document = JsonPath.parse(response.getContentAsString());
		assertThat(document.read("$.testDate", String.class)).isNotNull();

	}	


	@TestFactory
	Stream<DynamicTest> rejectsIndexCaseCreationWithoutRequiredFields() throws Exception {

		var source = Map.of("", Index.class, //
				"type=index", Index.class, //
				"type=contact", Default.class);

		return DynamicTest.stream(source.entrySet().iterator(), //
				it -> String.format("POST to /api/hd/cases%s rejects missing properties",
						!it.getKey().isBlank() ? "?".concat(it.getKey()) : ""), //
				test -> {

					var baseUri = "/api/hd/cases";
					var uri = baseUri.concat(!test.getKey().isBlank() ? "?".concat(test.getKey()) : "");
					var response = expectBadRequest(HttpMethod.POST, uri, new TrackedCaseDto());
					var group = test.getValue() == Default.class ? null : test.getValue();

					validator.getRequiredProperties(TrackedCaseDto.class, group) //
							.forEach(it -> assertThat(response.read("$." + it, String.class)).isNotNull());
				});
	}

	@Test
	void rejectsInvalidCharactersForStringFields() throws Exception {

		var today = LocalDate.now();
		var payload = new TrackedCaseDto() //
				.setFirstName("Michael 123") //
				.setLastName("Mustermann 123") //
				.setTestDate(today) //
				.setQuarantineStartDate(today) //
				.setQuarantineEndDate(today.plus(configuration.getQuarantinePeriod())) //
				.setPhone("0123456789") //
				.setCity("city 123") //
				.setStreet("\\") //
				.setHouseNumber("-");

		var document = expectBadRequest(HttpMethod.POST, "/api/hd/cases", payload);

		var alphabetic = messages.getMessage("Alphabetic");
		var alphaNumeric = messages.getMessage("AlphaNumeric");

		assertThat(document.read("$.firstName", String.class)).isEqualTo(alphabetic);
		assertThat(document.read("$.lastName", String.class)).isEqualTo(alphabetic);
		assertThat(document.read("$.city", String.class)).contains("gültige Stadt");
		assertThat(document.read("$.street", String.class)).contains("gültige Straße");
		assertThat(document.read("$.houseNumber", String.class)).isEqualTo(alphaNumeric);
	}

	@Test
	void rejectsEmptyTrackedPersonDetailsIfEnrollmentDone() throws Exception {

		var trackedCase = cases.findById(TrackedCaseDataInitializer.TRACKED_CASE_SANDRA).orElseThrow();

		@SuppressWarnings("null")
		var payload = representations.toInputRepresentation(trackedCase) //
				.setEmail(null) //
				.setDateOfBirth(null);

		var document = expectBadRequest(HttpMethod.PUT, "/api/hd/cases/" + trackedCase.getId(), payload);

		assertThat(document.read("$.email", String.class)).isNotNull();
	}

	@Test
	void getAllCasesOrderedCorrectly() throws Exception {

		var response = mvc.perform(get("/api/hd/cases") //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		var lastnamesFromResponse = List.of( //
				document.read("$._embedded.cases[0].lastName", String.class), //
				document.read("$._embedded.cases[1].lastName", String.class), //
				document.read("$._embedded.cases[2].lastName", String.class));

		var expectedList = new ArrayList<>(lastnamesFromResponse);
		Collections.sort(expectedList);

		assertThat(lastnamesFromResponse).containsExactlyElementsOf(expectedList);
	}

	@Test
	void addsComment() throws Exception {

		var trackedCase = cases.findById(TrackedCaseDataInitializer.TRACKED_CASE_GUSTAV).orElseThrow();

		var payload = new CommentInput();
		payload.setComment("Kommentar!");

		var response = mvc.perform(post("/api/hd/cases/{id}/comments", trackedCase.getId()) //
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.comments[0].comment", String.class)).isEqualTo(payload.getComment());
		assertThat(document.read("$.comments[0].date", String.class)).isNotBlank();
		assertThat(document.read("$.comments[0].author", String.class)).isNotBlank();
	}

	@Test
	// Only firstname, and lastname is mandatory for contact cases
	void successfullyCreateContactCaseWithMinimalInput() throws Exception {
		
		var payload = createMinimalContactPayload();

		var response = issueCaseCreation(payload, CaseType.CONTACT);
				
		assertThat(JsonPath.parse(response).read("$.firstName", String.class).equals(payload.getFirstName()));
		assertThat(JsonPath.parse(response).read("$.lastName", String.class).equals(payload.getLastName()));
		assertThat(cases.findById(TrackedCaseIdentifier.of(UUID.fromString(JsonPath.parse(response).read("$.caseId", String.class)))));
	}
	
	@Test
	// Only firstname, and lastname is mandatory for contact cases
	void successfullyUpdateContactCaseWithMinimalInput() throws Exception {
		
		var payload = createMinimalContactPayload();
		var response = issueCaseCreation(payload, CaseType.CONTACT);

		var document = JsonPath.parse(response.getContentAsString());
		var caseId = TrackedCaseIdentifier.of(UUID.fromString(document.read("$.caseId", String.class)));

		response = issueCaseUpdate(payload.setEmail("myemail@email.de"), caseId, CaseType.CONTACT);

		document = JsonPath.parse(response.getContentAsString());

		assertMinimalFieldsSet(document, payload);
		assertThat(document.read("$.email", String.class)).equals("myemail@email.de");
	}
	

	@Test
	void emptyEmailDoesNotTriggerValidation() throws Exception {

		var contactCase = createMinimalContactPayload();
		contactCase.setEmail("");

		mvc.perform(post("/api/hd/cases") //
				.content(jackson.writeValueAsString(contactCase)) //
				.contentType(MediaType.APPLICATION_JSON).param("type", "contact")) //
				.andExpect(status().isOk());
	}

	@Test
	void updatingContactMedicalCaseDoesNotRequireQuarantineData() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();
		issueCaseUpdate(createMinimalContactPayload(), trackedCase.getId(), CaseType.CONTACT_MEDICAL );

	}
	
	
	@Test
	void updatingContactCaseDoesNotRequireQuarantineData() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();
		issueCaseUpdate(createMinimalContactPayload(), trackedCase.getId(), CaseType.CONTACT );

	}

	private ReadContext expectBadRequest(HttpMethod method, String uri, Object payload) throws Exception {

		return JsonPath.parse(mvc.perform(request(method, uri) //
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isBadRequest()) //
				.andReturn().getResponse().getContentAsString());
	}

	private TrackedCaseDto createMinimalContactPayload() {

		return new TrackedCaseDto() //
				.setFirstName("Michael") //
				.setLastName("Mustermann");
	}

	private TrackedCaseDto createMinimalIndexPayload() {

		var today = LocalDate.now();

		return createMinimalContactPayload() //
				.setTestDate(today) //
				.setQuarantineStartDate(today) //
				.setQuarantineEndDate(today.plus(configuration.getQuarantinePeriod()));
	}

	private MockHttpServletResponse issueCaseCreation(TrackedCaseDto payload, CaseType type) throws Exception {

		return mvc.perform(post("/api/hd/cases") //
				.param("type", (type == CaseType.INDEX) ? "index" : "contact")
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isCreated()) //
				.andExpect(header().string(HttpHeaders.LOCATION, is(notNullValue()))) //
				.andReturn().getResponse();
	}
	
	private MockHttpServletResponse issueCaseUpdate(TrackedCaseDto payload,  TrackedCaseIdentifier caseId, CaseType type) throws Exception {

		return mvc.perform(put("/api/hd/cases/{id}", caseId) //
				.param("type", (type == CaseType.INDEX) ? "index" : "contact")
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andExpect(header().string(HttpHeaders.LOCATION, is(notNullValue()))) //
				.andReturn().getResponse();
	}	

	private static void assertMinimalFieldsSet(DocumentContext document, TrackedCaseDto payload) {

		assertThat(document.read("$.firstName", String.class)).isEqualTo(payload.getFirstName());
		assertThat(document.read("$.lastName", String.class)).isEqualTo(payload.getLastName());
		assertThat(document.read("$.quarantineStartDate", String.class)) //
				.isEqualTo(payload.getQuarantineStartDate().toString());
		assertThat(document.read("$.quarantineEndDate", String.class)) //
				.isEqualTo(payload.getQuarantineEndDate().toString());
		assertThat(document.read("$.phone", String.class)).isEqualTo(payload.getPhone());
		assertThat(document.read("$.testDate", String.class)).isEqualTo(payload.getTestDate().toString());
	}
	
	private MockHttpServletResponse issueToIndexCaseTransformation(TrackedCaseDto payload)
			throws Exception, JsonProcessingException {
		var response = mvc.perform(put("/api/hd/cases") //
				.param("toIndex", "true")
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse();
		return response;
	}	

	private MockHttpServletResponse expectBadRequestOnTransformationCall(TrackedCaseDto payload, TrackedCaseIdentifier caseId)
			throws Exception, JsonProcessingException {
		var response = mvc.perform(put("/api/hd/cases/{id}", caseId) //
				.param("toIndex", "true")
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isBadRequest()) //
				.andReturn().getResponse();
		return response;
	}		
	
	private MockHttpServletResponse expectBadRequestOnUpdate(TrackedCaseDto payload, CaseType type, TrackedCaseIdentifier caseId)
			throws Exception, JsonProcessingException {
		var response = mvc.perform(put("/api/hd/cases/{caseId}", caseId) //
				.param("type", (type == CaseType.INDEX) ? "index" : "contact")
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isBadRequest()) //
				.andReturn().getResponse();
		return response;
	}			
}
