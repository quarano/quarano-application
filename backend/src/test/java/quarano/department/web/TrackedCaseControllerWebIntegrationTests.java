package quarano.department.web;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static quarano.department.web.TrackedCaseLinkRelations.*;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
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
import java.time.format.DateTimeFormatter;
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
	void createsNewTrackedIndexCase() throws Exception {

		var payload = createMinimalIndexPayload();

		var response = issueCaseCreation(payload, CaseType.INDEX).getContentAsString();
		var document = JsonPath.parse(response);

		assertMinimalIndexFieldsSet(document, payload);
		assertThat(discoverer.findLinkWithRel(CONCLUDE, response)).isPresent();
	}

	@Test
	void createNewTrackedIndexCaseWithExtReference() throws Exception {

		var payload = createMinimalIndexPayload();
		payload.setExtReferenceNumber("AGD-45465");

		var response = issueCaseCreation(payload, CaseType.INDEX).getContentAsString();
		var document = JsonPath.parse(response);

		assertThat(document.read("$.extReferenceNumber", String.class))
				.isEqualTo(payload.getExtReferenceNumber().toString());
	}

	@Test
	void indicatesStartTrackingIfRequiredDataIsSet() throws Exception {

		var payload = createMinimalIndexPayload() //
				.setEmail("foo@bar.com") //
				.setDateOfBirth(LocalDate.now().minusYears(25));

		var response = issueCaseCreation(payload, CaseType.INDEX).getContentAsString();
		var document = JsonPath.parse(response);

		assertMinimalIndexFieldsSet(document, payload);

		Stream.of(START_TRACKING, CONCLUDE).forEach(it -> {
			assertThat(discoverer.findLinkWithRel(it, response)).isPresent();
		});
	}

	@Test
	void receiveTrackedCaseSummaryForTrackedCaseWithEndedQuarantineEndDate() throws Exception {

		LocalDate today = LocalDate.now();

		// get the first case
		var response = mvc.perform(get("/api/hd/cases") //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		var trackedCaseId = document.read("$._embedded.cases[0].caseId", String.class);

		assertThat(trackedCaseId).isNotNull();

		// modify it to have a quarantine that ended in the past
		LocalDate quarantineStartDate = today.minusDays(5);
		LocalDate quarantineEndDate = today.minusDays(1);
		var payload = createMinimalIndexPayload().setQuarantineEndDate(quarantineEndDate)
				.setQuarantineStartDate(quarantineStartDate);

		mvc.perform(put("/api/hd/cases/{id}", trackedCaseId) //
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		// fetch cases again and check if quarantine start and end dates are shown correctly
		var readResponseAfterUpdate = mvc.perform(get("/api/hd/cases") //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		var documentAfterUpdate = JsonPath.parse(readResponseAfterUpdate);

		String trackedCaseSummaryQuarantineStartAfterUpdate = documentAfterUpdate
				.read("$..cases[?(@.caseId == '" + trackedCaseId + "')].quarantine.from", JSONArray.class).get(0).toString();

		String trackedCaseSummaryQuarantineEndAfterUpdate = documentAfterUpdate
				.read("$..cases[?(@.caseId == '" + trackedCaseId + "')].quarantine.to", JSONArray.class).get(0).toString();

		assertThat(trackedCaseSummaryQuarantineStartAfterUpdate)
				.isEqualTo(quarantineStartDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
		assertThat(trackedCaseSummaryQuarantineEndAfterUpdate)
				.isEqualTo(quarantineEndDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

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

		assertMinimalIndexFieldsSet(document, payload);
		assertThat(document.read("$.infected", boolean.class)).isTrue();
	}

	@Test
	// CORE-121
	void transformContactCaseToIndexCase() throws Exception {

		// get contact case Tanja and turn it into dto
		var contactCaseTanja = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1).get();
		var caseTanjaAsDto = representations.toInputRepresentation(contactCaseTanja);

		// set necessary fields to be a valid index case
		caseTanjaAsDto.setInfected(true);
		caseTanjaAsDto.setTestDate(LocalDate.now());
		caseTanjaAsDto.setPhone("4456465465");
		caseTanjaAsDto.setQuarantineStartDate(LocalDate.now());
		caseTanjaAsDto.setQuarantineEndDate(LocalDate.now().plusDays(14));

		// start the transformation
		var response = issueToIndexCaseTransformation(caseTanjaAsDto, contactCaseTanja.getId());

		// check response
		var document = JsonPath.parse(response.getContentAsString());
		assertMinimalIndexFieldsSet(document, caseTanjaAsDto);
		assertThat(document.read("$.infected", boolean.class)).isTrue();

		// check if case has been stored correctly
		var contactCaseTanjaAfterTransformation = cases.findById(contactCaseTanja.getId()).orElseThrow();
		assertThat(contactCaseTanjaAfterTransformation.isIndexCase()).isTrue();
	}

	@Test
	// CORE-121
	void rejectTransformContactCaseWhenInfoMissing() throws Exception {

		// get contact case Tanja and turn it into dto
		var contactCaseTanja = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1).get();
		TrackedCaseDto caseTanjaAsDto = new TrackedCaseDto();
		modelMapper.map(contactCaseTanja, caseTanjaAsDto);

		// test date is missing
		caseTanjaAsDto.setInfected(true);
		caseTanjaAsDto.setPhone("4456465465");
		caseTanjaAsDto.setQuarantineStartDate(LocalDate.now());
		caseTanjaAsDto.setQuarantineEndDate(LocalDate.now().plusDays(14));

		// start the transformation
		var response = expectBadRequestOnTransformationCall(caseTanjaAsDto, contactCaseTanja.getId());

		// response should contain error message for field testDate
		var document = JsonPath.parse(response.getContentAsString());

		assertThat(document.read("$.testDate", String.class)).isNotNull();
	}

	@Test
	void rejectCreationOfContactCaseWithPositiveTestResult() throws Exception {

		var payload = createMinimalContactPayload() //
				.setTestDate(LocalDate.now().minusDays(8)) //
				.setInfected(true);

		var response = expectBadRequest(HttpMethod.POST, "/api/hd/cases?type=contact", payload);

		assertThat(response.read("$.testDate", String.class)).isNotNull();
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
				.setExtReferenceNumber("ADF !").setHouseNumber("\\");

		var document = expectBadRequest(HttpMethod.POST, "/api/hd/cases", payload);

		var houseNumber = messages.getMessage("Pattern.houseNumber");
		var firstName = messages.getMessage("Pattern.firstName");
		var lastName = messages.getMessage("Pattern.lastName");
		var extReference = messages.getMessage("Pattern.extReferenceNumber");

		assertThat(document.read("$.firstName", String.class)).isEqualTo(firstName);
		assertThat(document.read("$.lastName", String.class)).isEqualTo(lastName);
		assertThat(document.read("$.city", String.class)).contains("gültige Stadt");
		assertThat(document.read("$.street", String.class)).contains("gültige Straße");
		assertThat(document.read("$.houseNumber", String.class)).isEqualTo(houseNumber);
		assertThat(document.read("$.extReferenceNumber", String.class)).isEqualTo(extReference);
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

		var lastNamesFromResponse = List.of( //
				document.read("$._embedded.cases[0].lastName", String.class), //
				document.read("$._embedded.cases[1].lastName", String.class), //
				document.read("$._embedded.cases[2].lastName", String.class));

		var expectedList = new ArrayList<>(lastNamesFromResponse);
		Collections.sort(expectedList);

		assertThat(lastNamesFromResponse).containsExactlyElementsOf(expectedList);
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

	@Test // CORE-121
	// Only firstname, and lastname is mandatory for contact cases
	void createsContactCaseWithMinimalInput() throws Exception {

		var payload = createMinimalContactPayload();

		var response = issueCaseCreation(payload, CaseType.CONTACT).getContentAsString();

		assertThat(JsonPath.parse(response).read("$.firstName", String.class).equals(payload.getFirstName()));
		assertThat(JsonPath.parse(response).read("$.lastName", String.class).equals(payload.getLastName()));
		assertThat(cases
				.findById(TrackedCaseIdentifier.of(UUID.fromString(JsonPath.parse(response).read("$.caseId", String.class)))));
	}

	@Test // CORE-121
	// Only firstname, and lastname is mandatory for contact cases
	void updatesContactCaseWithMinimalInput() throws Exception {

		var payload = createMinimalContactPayload();
		var response = issueCaseCreation(payload, CaseType.CONTACT);

		var document = JsonPath.parse(response.getContentAsString());
		var caseId = TrackedCaseIdentifier.of(UUID.fromString(document.read("$.caseId", String.class)));

		response = issueCaseUpdate(payload.setEmail("myemail@email.de"), caseId, CaseType.CONTACT);

		document = JsonPath.parse(response.getContentAsString());

		assertMinimalContactFieldsSet(document, payload);
		assertThat(document.read("$.email", String.class)).isEqualTo("myemail@email.de");
	}

	@Test // CORE-121
	void emptyEmailDoesNotTriggerValidation() throws Exception {

		var contactCase = createMinimalContactPayload();
		contactCase.setEmail("");

		mvc.perform(post("/api/hd/cases") //
				.content(jackson.writeValueAsString(contactCase)) //
				.contentType(MediaType.APPLICATION_JSON).param("type", "contact")) //
				.andExpect(status().isCreated());
	}

	@Test // CORE-185
	void updatingContactVulnerableCaseDoesNotRequireQuarantineData() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();
		issueCaseUpdate(createMinimalContactPayload(), trackedCase.getId(), CaseType.CONTACT_VULNERABLE);

	}

	@Test // CORE-121
	void updatingContactMedicalCaseDoesNotRequireQuarantineData() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();
		issueCaseUpdate(createMinimalContactPayload(), trackedCase.getId(), CaseType.CONTACT_MEDICAL);

	}

	@Test // CORE-121
	void updatingContactCaseDoesNotRequireQuarantineData() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();
		issueCaseUpdate(createMinimalContactPayload(), trackedCase.getId(), CaseType.CONTACT);

	}

	@Test
	void creatingContactCasesOnlyWithCorrectQuarantineDates() throws Exception {
		var payload = createMinimalContactPayload();
		creatingCasesOnlyWithCorrectQuarantineDates(payload, CaseType.CONTACT, CaseType.CONTACT_MEDICAL,
				CaseType.CONTACT_VULNERABLE);
	}

	@Test
	void creatingIndexCasesOnlyWithCorrectOrderOfQuarantineDates() throws Exception {
		var payload = createMinimalIndexPayload();
		creatingCasesOnlyWithCorrectQuarantineDates(payload, CaseType.INDEX);
	}

	private void creatingCasesOnlyWithCorrectQuarantineDates(TrackedCaseDto payload, CaseType... types) throws Exception {
		// only with start
		payload.setQuarantineStartDate(LocalDate.now());
		payload.setQuarantineEndDate(null);

		for (CaseType type : types) {
			expectBadRequestOnCreation(payload, type);
		}

		// only with end
		payload.setQuarantineStartDate(null);
		payload.setQuarantineEndDate(LocalDate.now());

		for (CaseType type : types) {
			expectBadRequestOnCreation(payload, type);
		}

		// with end before start
		payload.setQuarantineStartDate(LocalDate.now());
		payload.setQuarantineEndDate(LocalDate.now().minusDays(1));

		for (CaseType type : types) {
			expectBadRequestOnCreation(payload, type);
		}

		// correct with start before end
		payload.setQuarantineStartDate(LocalDate.now().minusDays(1));
		payload.setQuarantineEndDate(LocalDate.now());

		for (CaseType type : types) {
			issueCaseCreation(payload, type);
		}
	}

	@Test
	void updatingContactCasesOnlyWithCorrectQuarantineDates() throws Exception {
		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();
		var payload = createMinimalContactPayload();
		updatingCasesOnlyWithCorrectQuarantineDates(payload, trackedCase.getId(), CaseType.CONTACT, CaseType.CONTACT_MEDICAL,
				CaseType.CONTACT_VULNERABLE);
	}

	@Test
	void updatingIndexCasesOnlyWithCorrectOrderOfQuarantineDates() throws Exception {
		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();
		var payload = createMinimalIndexPayload();
		updatingCasesOnlyWithCorrectQuarantineDates(payload, trackedCase.getId(),CaseType.INDEX);
	}

	private void updatingCasesOnlyWithCorrectQuarantineDates(TrackedCaseDto payload, TrackedCaseIdentifier caseId,
			CaseType... types) throws Exception, JsonProcessingException {
		// only with start
		payload.setQuarantineStartDate(LocalDate.now());
		payload.setQuarantineEndDate(null);

		for (CaseType type : types) {
			expectBadRequestOnUpdate(payload, type, caseId);
		}

		// only with end
		payload.setQuarantineStartDate(null);
		payload.setQuarantineEndDate(LocalDate.now());

		for (CaseType type : types) {
			expectBadRequestOnUpdate(payload, type, caseId);
		}

		// with end before start
		payload.setQuarantineStartDate(LocalDate.now());
		payload.setQuarantineEndDate(LocalDate.now().minusDays(1));

		for (CaseType type : types) {
			expectBadRequestOnUpdate(payload, type, caseId);
		}

		// correct with start before end
		payload.setQuarantineStartDate(LocalDate.now().minusDays(1));
		payload.setQuarantineEndDate(LocalDate.now());

		for (CaseType type : types) {
			issueCaseUpdate(payload, caseId, type);
		}
	}

	@Test // CORE-121
	void updatesTrackedPersonDetails() throws Exception {

		var trackedCase = cases.findById(TrackedCaseDataInitializer.TRACKED_CASE_SANDRA).orElseThrow();

		var payload = representations.toInputRepresentation(trackedCase) //
				.setFirstName("Max") //
				.setLastName("Mustermann");

		var response = mvc.perform(put("/api/hd/cases/{id}", trackedCase.getId()) //
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.firstName", String.class)).isEqualTo(payload.getFirstName());
		assertThat(document.read("$.lastName", String.class)).isEqualTo(payload.getLastName());
	}

	@Test // CORE-115
	void exposesQuestionnaireForCaseAlreadyTracking() throws Exception {

		var trackingCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_SEC1_ID_DEP1).orElseThrow();

		var response = mvc.perform(get("/api/hd/cases/{id}", trackingCase.getId())) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		var link = discoverer.findRequiredLinkWithRel(TrackedCaseLinkRelations.QUESTIONNAIRE, response);
		assertThat(link).isNotNull();

		response = mvc.perform(get(link.getHref())) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.belongToMedicalStaffDescription", String.class)).isNotNull();
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
				.setPhone("0123456789") //
				.setTestDate(today) //
				.setQuarantineStartDate(today) //
				.setQuarantineEndDate(today.plus(configuration.getQuarantinePeriod()));
	}

	private MockHttpServletResponse issueCaseCreation(TrackedCaseDto payload, CaseType type) throws Exception {

		return mvc.perform(post("/api/hd/cases") //
				.param("type", type == CaseType.INDEX ? "index" : "contact") //
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isCreated()) //
				.andExpect(header().string(HttpHeaders.LOCATION, is(notNullValue()))) //
				.andReturn().getResponse();
	}

	private MockHttpServletResponse expectBadRequestOnCreation(TrackedCaseDto payload, CaseType type) throws Exception {

		return mvc.perform(post("/api/hd/cases") //
				.param("type", type == CaseType.INDEX ? "index" : "contact") //
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isBadRequest()) //
				.andReturn().getResponse();
	}

	private MockHttpServletResponse issueCaseUpdate(TrackedCaseDto payload, TrackedCaseIdentifier caseId, CaseType type)
			throws Exception {

		return mvc.perform(put("/api/hd/cases/{id}", caseId) //
				.param("type", type == CaseType.INDEX ? "index" : "contact") //
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse();
	}

	private MockHttpServletResponse issueToIndexCaseTransformation(TrackedCaseDto payload, TrackedCaseIdentifier id)
			throws Exception, JsonProcessingException {

		return mvc.perform(put("/api/hd/cases/{id}", id) //
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse();
	}

	private MockHttpServletResponse expectBadRequestOnTransformationCall(TrackedCaseDto payload,
			TrackedCaseIdentifier caseId) throws Exception, JsonProcessingException {

		return mvc.perform(put("/api/hd/cases/{id}", caseId) //
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isBadRequest()) //
				.andReturn().getResponse();
	}

	private MockHttpServletResponse expectBadRequestOnUpdate(TrackedCaseDto payload, CaseType type,
			TrackedCaseIdentifier caseId) throws Exception, JsonProcessingException {

		return mvc.perform(put("/api/hd/cases/{caseId}", caseId) //
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isBadRequest()) //
				.andReturn().getResponse();
	}

	private static void assertMinimalIndexFieldsSet(DocumentContext document, TrackedCaseDto payload) {

		assertMinimalContactFieldsSet(document, payload);
		assertThat(document.read("$.quarantineStartDate", String.class)) //
				.isNotNull() //
				.isEqualTo(payload.getQuarantineStartDate().toString());
		assertThat(document.read("$.quarantineEndDate", String.class)) //
				.isEqualTo(payload.getQuarantineEndDate().toString());
		assertThat(document.read("$.phone", String.class)).isEqualTo(payload.getPhone());
		assertThat(document.read("$.testDate", String.class)).isEqualTo(payload.getTestDate().toString());
	}

	private static void assertMinimalContactFieldsSet(DocumentContext document, TrackedCaseDto payload) {

		assertThat(document.read("$.firstName", String.class)).isEqualTo(payload.getFirstName());
		assertThat(document.read("$.lastName", String.class)).isEqualTo(payload.getLastName());
	}
}
