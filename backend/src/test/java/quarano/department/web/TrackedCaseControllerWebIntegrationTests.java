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
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseDataInitializer;
import quarano.department.TrackedCaseProperties;
import quarano.department.TrackedCaseRepository;
import quarano.department.web.TrackedCaseRepresentations.CommentInput;
import quarano.department.web.TrackedCaseRepresentations.TrackedCaseDto;
import quarano.department.web.TrackedCaseRepresentations.ValidationGroups.Index;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.util.TestUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import javax.validation.groups.Default;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.comparator.Comparators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
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

		var payload = createMinimalIndexPayload()
				.setEmail("foo@bar.com")
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
		var response = mvc.perform(get("/hd/cases")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		var trackedCaseId = document.read("$._embedded.cases[0].caseId", String.class);

		assertThat(trackedCaseId).isNotNull();

		// modify it to have a quarantine that ended in the past
		LocalDate quarantineStartDate = today.minusDays(5);
		LocalDate quarantineEndDate = today.minusDays(1);
		var payload = createMinimalIndexPayload().setQuarantineEndDate(quarantineEndDate)
				.setQuarantineStartDate(quarantineStartDate);

		mvc.perform(put("/hd/cases/{id}", trackedCaseId)
				.content(jackson.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		// fetch cases again and check if quarantine start and end dates are shown correctly
		var readResponseAfterUpdate = mvc.perform(get("/hd/cases")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
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

		response = mvc.perform(put(location)
				.content(jackson.writeValueAsString(payload.setInfected(true)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse();

		var document = JsonPath.parse(response.getContentAsString());

		assertMinimalIndexFieldsSet(document, payload);
		assertThat(document.read("$.infected", boolean.class)).isTrue();
	}

	@Test // CORE-121
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

	@Test // CORE-121
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

		var payload = createMinimalContactPayload()
				.setTestDate(LocalDate.now().minusDays(8))
				.setInfected(true);

		var response = expectBadRequest(HttpMethod.POST, "/hd/cases?type=contact", payload);

		assertThat(response.read("$.testDate", String.class)).isNotNull();
	}

	@TestFactory
	Stream<DynamicTest> rejectsIndexCaseCreationWithoutRequiredFields() throws Exception {

		var source = Map.of("", Index.class,
				"type=index", Index.class,
				"type=contact", Default.class);

		return DynamicTest.stream(source.entrySet().iterator(),
				it -> String.format("POST to /api/hd/cases%s rejects missing properties",
						!it.getKey().isBlank() ? "?".concat(it.getKey()) : ""),
				test -> {

					var baseUri = "/hd/cases";
					var uri = baseUri.concat(!test.getKey().isBlank() ? "?".concat(test.getKey()) : "");
					var response = expectBadRequest(HttpMethod.POST, uri, new TrackedCaseDto());
					var group = test.getValue() == Default.class ? null : test.getValue();

					validator.getRequiredProperties(TrackedCaseDto.class, group)
							.forEach(it -> assertThat(response.read("$." + it, String.class)).isNotNull());
				});
	}

	@Test
	void rejectsInvalidCharactersForStringFields() throws Exception {

		var today = LocalDate.now();
		var payload = new TrackedCaseDto()
				.setFirstName("Michael 123")
				.setLastName("Mustermann 123")
				.setTestDate(today)
				.setQuarantineStartDate(today)
				.setQuarantineEndDate(today.plus(configuration.getQuarantinePeriod()))
				.setPhone("0123456789")
				.setCity("city 123")
				.setStreet("\\")
				.setExtReferenceNumber("ADF !").setHouseNumber("\\");

		var document = expectBadRequest(HttpMethod.POST, "/hd/cases", payload);

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
		var payload = representations.toInputRepresentation(trackedCase)
				.setEmail(null)
				.setDateOfBirth(null);

		var document = expectBadRequest(HttpMethod.PUT, "/hd/cases/" + trackedCase.getId(), payload);

		assertThat(document.read("$.email", String.class)).isNotNull();
	}

	@Test
	void getAllCasesOrderedCorrectly() throws Exception {

		var response = mvc.perform(get("/hd/cases")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		var lastNamesFromResponse = io.vavr.collection.List.of( // java.util.List.of does not accept a Null possible here
				document.read("$._embedded.cases[0].lastName", String.class),
				document.read("$._embedded.cases[1].lastName", String.class),
				document.read("$._embedded.cases[2].lastName", String.class),
				document.read("$._embedded.cases[3].lastName", String.class),
				document.read("$._embedded.cases[4].lastName", String.class),
				document.read("$._embedded.cases[5].lastName", String.class),
				document.read("$._embedded.cases[6].lastName", String.class),
				document.read("$._embedded.cases[7].lastName", String.class));

		var expectedList = io.vavr.collection.List.ofAll(lastNamesFromResponse).sorted(Comparators.nullsLow());

		assertThat(lastNamesFromResponse).containsExactlyElementsOf(expectedList);
	}

	@Test
	void addsComment() throws Exception {

		var trackedCase = cases.findById(TrackedCaseDataInitializer.TRACKED_CASE_GUSTAV).orElseThrow();

		var payload = new CommentInput();
		payload.setComment("Kommentar!");

		var response = mvc.perform(post("/hd/cases/{id}/comments", trackedCase.getId())
				.content(jackson.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
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

		// CORE-511
		assertThat(JsonPath.parse(response).read("$.createdAt", String.class)
				.equals(LocalDate.now().format(DateTimeFormatter.ISO_DATE)));
	}

	@Test // CORE-121
	// Only firstname, and lastname is mandatory for contact cases
	void updatesContactCaseWithMinimalInput() throws Exception {

		var payload = createMinimalContactPayload();
		var response = issueCaseCreation(payload, CaseType.CONTACT);

		var document = JsonPath.parse(response.getContentAsString());
		var caseId = TrackedCaseIdentifier.of(UUID.fromString(document.read("$.caseId", String.class)));

		response = issueCaseUpdate(payload.setEmail("myemail@email.de"), CaseType.CONTACT, caseId);

		document = JsonPath.parse(response.getContentAsString());

		assertMinimalContactFieldsSet(document, payload);
		assertThat(document.read("$.email", String.class)).isEqualTo("myemail@email.de");
	}

	@Test // CORE-121
	void emptyEmailDoesNotTriggerValidation() throws Exception {

		var contactCase = createMinimalContactPayload();
		contactCase.setEmail("");

		mvc.perform(post("/hd/cases")
				.content(jackson.writeValueAsString(contactCase))
				.contentType(MediaType.APPLICATION_JSON)
				.param("type", "contact"))
				.andExpect(status().isCreated());
	}

	@Test // CORE-185
	void updatingContactVulnerableCaseDoesNotRequireQuarantineData() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();
		issueCaseUpdate(createMinimalContactPayload(), CaseType.CONTACT_VULNERABLE, trackedCase);

	}

	@Test // CORE-121
	void updatingContactMedicalCaseDoesNotRequireQuarantineData() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();
		issueCaseUpdate(createMinimalContactPayload(), CaseType.CONTACT_MEDICAL, trackedCase);

	}

	@Test // CORE-121
	void updatingContactCaseDoesNotRequireQuarantineData() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();
		issueCaseUpdate(createMinimalContactPayload(), CaseType.CONTACT, trackedCase);

	}

	@Test // CORE-582
	void zipCodeNotFoundInRKIData() throws Exception {

		var contactCase = createMinimalContactPayload();
		contactCase.setZipCode("12345");

		var response = mvc.perform(post("/hd/cases")
				.content(jackson.writeValueAsString(contactCase))
				.contentType(MediaType.APPLICATION_JSON)
				.param("type", "contact"))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse();

		var document = JsonPath.parse(response.getContentAsString());

		Object[] placeHolderForZipCode = { "12345" };
		assertThat(document.read("zipCode", String.class))
				.isEqualTo(messages.getMessage("wrong.zipCode", placeHolderForZipCode, "Message not loaded"));
	}

	@lombok.Value(staticConstructor = "of")
	static class Fixture {
		LocalDate start, end;
		CaseType type;
		BiConsumer<TrackedCaseDto, CaseType> consumer;
	}

	@TestFactory
	Stream<DynamicTest> creatingContactCasesOnlyWithCorrectQuarantineDates() {

		return createQuarantineTests((payload, type) -> issueCaseCreation(payload, type),
				(payload, type) -> expectBadRequestOnCreation(payload, type));
	}

	@TestFactory
	Stream<DynamicTest> updatingContactCasesOnlyWithCorrectQuarantineDates() throws Exception {

		var contactCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();
		var indexCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON2_ID_DEP1)
				.orElseThrow();
		var caseMap = Map.of(CaseType.INDEX, indexCase, CaseType.CONTACT, contactCase, CaseType.CONTACT_MEDICAL,
				contactCase, CaseType.CONTACT_VULNERABLE, contactCase);

		return createQuarantineTests(
				(payload, type) -> issueCaseUpdate(payload, type, caseMap.getOrDefault(type, contactCase)),
				(payload, type) -> expectBadRequestOnUpdate(payload, type, caseMap.getOrDefault(type, contactCase)));
	}

	@SuppressWarnings("null")
	private Stream<DynamicTest> createQuarantineTests(BiConsumer<TrackedCaseDto, CaseType> success,
			BiConsumer<TrackedCaseDto, CaseType> failure) {

		var fixture = Arrays.stream(CaseType.values()).flatMap(it -> {

			var now = LocalDate.now();

			var base = Stream.of(Fixture.of(now, null, it, failure),
					Fixture.of(null, now, it, failure),
					Fixture.of(now, now.minusDays(1), it, failure),
					Fixture.of(now.minusDays(1), now, it, success));

			return it.equals(CaseType.INDEX)
					? Stream.concat(base, Stream.of(Fixture.of(null, null, it, failure)))
					: base;

		}).iterator();

		return DynamicTest.stream(fixture,
				it -> {
					return String.format("Case creation/update with quarantine (%s, %s) for type %s %s.", it.start, it.end,
							it.type, it.consumer == success ? "succeeds" : "is rejected");
				}, it -> {

					var payload = it.type == CaseType.INDEX
							? createMinimalIndexPayload()
							: createMinimalContactPayload();

					payload.setQuarantineStartDate(it.start);
					payload.setQuarantineEndDate(it.end);

					it.consumer.accept(payload, it.type);
				});
	}

	@Test // CORE-121
	void updatesTrackedPersonDetails() throws Exception {

		var trackedCase = cases.findById(TrackedCaseDataInitializer.TRACKED_CASE_SANDRA).orElseThrow();

		var payload = representations.toInputRepresentation(trackedCase)
				.setFirstName("Max")
				.setLastName("Mustermann");

		var response = mvc.perform(put("/hd/cases/{id}", trackedCase.getId())
				.content(jackson.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.firstName", String.class)).isEqualTo(payload.getFirstName());
		assertThat(document.read("$.lastName", String.class)).isEqualTo(payload.getLastName());
	}

	@Test // CORE-115
	void exposesQuestionnaireForCaseAlreadyTracking() throws Exception {

		var trackingCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_SEC1_ID_DEP1).orElseThrow();

		var response = mvc.perform(get("/hd/cases/{id}", trackingCase.getId()))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var link = discoverer.findRequiredLinkWithRel(TrackedCaseLinkRelations.QUESTIONNAIRE, response);
		assertThat(link).isNotNull();

		response = mvc.perform(get(link.getHref()))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.belongToMedicalStaffDescription", String.class)).isNotNull();
	}

	@Test // CORE-119
	@SuppressWarnings("unchecked")
	void exposesContactsForIndexCase() throws Exception {

		var trackingCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_SEC1_ID_DEP1).orElseThrow();

		var response = mvc.perform(get("/hd/cases/{id}", trackingCase.getId()))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);
		assertThat(document.read("$.contactCount", Integer.class)).isEqualTo(2);

		var link = discoverer.findRequiredLinkWithRel(TrackedCaseLinkRelations.CONTACTS, response);
		assertThat(link).isNotNull();

		response = mvc.perform(get(link.getHref()))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		document = JsonPath.parse(response);

		assertThat(document.read("$._embedded.contacts.length()", Integer.class)).isEqualTo(2);

		List<Map<String, Object>> contacts = document.read("$._embedded.contacts[?(@.lastName=='Drogler')]");

		assertThat(contacts).hasSize(1);

		DocumentContext contact = JsonPath.parse(contacts.get(0));

		assertThat(contact.read("$.firstName", String.class)).isEqualTo("Dorothea");
		assertThat(contact.read("$.isHealthStaff", boolean.class)).isTrue();
		assertThat(contact.read("$.isSenior", Boolean.class)).isNull();
		assertThat(contact.read("$.hasPreExistingConditions", Boolean.class)).isNull();
		assertThat(contact.read("$.caseStatusLabel", String.class)).isEqualTo("angelegt");
		assertThat(contact.read("$.caseType", String.class)).isEqualTo("contact");
		assertThat(contact.read("$.contactDates", List.class)).contains(LocalDate.now().minusDays(1).toString());

		assertThat(discoverer.findLinkWithRel(TrackedCaseContactSummary.TRACKED_CASE, contact.jsonString())).isPresent();
	}

	@Test // CORE-643
	@SuppressWarnings("unchecked")
	void dontExposesContactsForContactCase() throws Exception {

		var trackingCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();

		var response = mvc.perform(get("/hd/cases/{id}", trackingCase.getId()))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);
		assertThat(document.read("$.contactCount", Integer.class)).isNull();

		assertThat(discoverer.findLinkWithRel(TrackedCaseLinkRelations.CONTACTS, response)).isNotPresent();

		mvc.perform(get("/hd/cases/{id}/contacts", trackingCase.getId()))
				.andExpect(status().isForbidden());
	}

	@Test // CORE-120
	void exposesDiaryForCase() throws Exception {

		var trackingCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_SEC1_ID_DEP1).orElseThrow();

		var response = mvc.perform(get("/hd/cases/{id}", trackingCase.getId()))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var link = discoverer.findRequiredLinkWithRel(TrackedCaseLinkRelations.DIARY, response);

		assertThat(link).isNotNull();

		response = mvc.perform(get(link.getHref()))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$._embedded.trackedCaseDiaryEntrySummaryList.length()", Integer.class)).isEqualTo(3);

		List<Map<String, Object>> diaryEntries = document
				.read("$._embedded.trackedCaseDiaryEntrySummaryList[?(@.bodyTemperature=='37.8')]");

		assertThat(diaryEntries).hasSize(1);

		DocumentContext diaryEntry = JsonPath.parse(diaryEntries.get(0));

		assertThat(diaryEntry.read("$.contacts", Object[].class)).isEmpty();
		assertThat(diaryEntry.read("$.bodyTemperature", Float.class)).isEqualTo(37.8f);
		assertThat(diaryEntry.read("$.reportedAt", String.class)).isNotNull();
		assertThat((List<Map<String, Object>>) diaryEntry.read("$.symptoms", List.class))
				.hasSize(2)
				.allSatisfy(symptom -> {
					assertThat(symptom.get("id")).isNotNull();
					assertThat(symptom.get("name")).isNotNull();
					assertThat(symptom.get("characteristic")).isNotNull();
				});
	}

	@Test // CORE-252
	void filtersCasesIfQueryGiven() throws Exception {

		String response = mvc.perform(get("/hd/cases?q={query}", "ert"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		assertThat(JsonPath.parse(response).read("$._embedded.cases[*].lastName", String[].class))
				.containsExactly("Baum", "Ebert", "Mertens", "Seufert");
	}

	@Test // CORE-252
	void projectsCasesIfProjectionGiven() throws Exception {

		String response = mvc.perform(get("/hd/cases?q={query}&projection={projection}", "ert", "select"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var firstCase = JsonPath.parse(response).read("$._embedded.cases[0]", Map.class);

		// Contains required fields
		assertThat(firstCase.get("firstName")).isEqualTo("Bert");
		assertThat(firstCase.get("lastName")).isEqualTo("Baum");
		assertThat(firstCase.get("dateOfBirth")).isNull();

		// Contains self link
		assertThat(discoverer.findLinkWithRel(IanaLinkRelations.SELF, JsonPath.parse(firstCase).jsonString())).isPresent();
	}

	@Test // CORE-252
	void updatesOriginCasesCorrectly() throws Exception {

		var originCase = cases.findById(TrackedCaseDataInitializer.TRACKED_CASE_SANDRA).orElseThrow();
		var contactCase = cases.findAll()
				.filter(it -> !it.getType().equals(CaseType.INDEX))
				.stream().findFirst().orElseThrow();

		contactCase.addOriginCase(originCase);

		TestUtils.fakeRequest(HttpMethod.GET, "/hd/cases", mvc.getDispatcherServlet().getWebApplicationContext());

		var response = mvc.perform(put("/hd/cases/{id}", contactCase.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(jackson.writeValueAsString(representations.toInputRepresentation(contactCase))))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThatExceptionOfType(PathNotFoundException.class).isThrownBy(() -> document.read("$.originCases"));
		assertThat(document.read("$._embedded.originCases[0].firstName", String.class)).isEqualTo("Sandra");
		assertThat(document.read("$._embedded.originCases[0].lastName", String.class)).isEqualTo("Schubert");
		assertThat(document.read("$._embedded.originCases[0]._links.self.href", String.class)).isNotNull();

		assertThat(cases.findById(contactCase.getId())).hasValueSatisfying(it -> {
			assertThat(it.originatesFrom(originCase)).isTrue();
		});
	}

	@Test // CORE-346
	void returnsOnlyIndexCasesIfFiltered() throws Exception {

		var result = mvc.perform(get("/hd/cases")
				.contentType(MediaType.APPLICATION_JSON)
				.param("type", "index"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(result);

		assertThat(document.read("$._embedded.cases[*].caseType", JSONArray.class))
				.allMatch("index"::equals);
	}

	@Test // CORE-357
	@WithQuaranoUser("admin")
	void exposesNumberOfOpenAnomalies() throws Exception {

		var result = mvc.perform(get("/hd/cases/{id}", TrackedCaseDataInitializer.TRACKED_CASE_GUSTAV)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(result);

		assertThat(document.read("$._links.anomalies.href", String.class)).isNotBlank();
		assertThat(document.read("$.openAnomaliesCount", long.class)).isEqualTo(1);
	}

	@Test // CORE-331
	void getOriginCasesOfContactsCorrectly() throws Exception {

		var originCase = cases.findById(TrackedCaseDataInitializer.TRACKED_CASE_SIGGI).orElseThrow();
		var contactCase = cases.findAll()
				.filter(it -> !it.getType().equals(CaseType.INDEX))
				.filter(it -> it.originatesFrom(originCase))
				.stream().findFirst().orElseThrow();

		var response = mvc.perform(get("/hd/cases")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThatExceptionOfType(PathNotFoundException.class).isThrownBy(() -> document.read("$.originCases"));

		List<Map<String, Object>> contact = document
				.read("$._embedded.cases[?(@.caseId == '" + contactCase.getId() + "')]");
		var contactDoc = JsonPath.parse(contact.get(0));

		assertThat(contactDoc.read("$._embedded.originCases[0].firstName", String.class))
				.isEqualTo(originCase.getTrackedPerson().getFirstName());
		assertThat(contactDoc.read("$._embedded.originCases[0].lastName", String.class))
				.isEqualTo(originCase.getTrackedPerson().getLastName());
		String embeddedOriginCaseLink = contactDoc.read("$._embedded.originCases[0]._links.self.href", String.class);
		assertThat(embeddedOriginCaseLink).contains(originCase.getId().toString());

		String originCaseLink = contactDoc.read("$._links.originCases.href", String.class);
		assertThat(originCaseLink).contains(originCase.getId().toString());
		assertThat(originCaseLink).isEqualTo(embeddedOriginCaseLink);
	}

	private ReadContext expectBadRequest(HttpMethod method, String uri, Object payload) throws Exception {

		return JsonPath.parse(mvc.perform(request(method, uri)
				.content(jackson.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString());
	}

	private TrackedCaseDto createMinimalContactPayload() {

		return new TrackedCaseDto()
				.setFirstName("Michael")
				.setLastName("Mustermann");
	}

	private TrackedCaseDto createMinimalIndexPayload() {

		var today = LocalDate.now();

		return createMinimalContactPayload()
				.setPhone("0123456789")
				.setTestDate(today)
				.setQuarantineStartDate(today)
				.setQuarantineEndDate(today.plus(configuration.getQuarantinePeriod()));
	}

	private MockHttpServletResponse issueCaseCreation(TrackedCaseDto payload, CaseType type) {

		try {

			return mvc.perform(post("/hd/cases")
					.param("type", type == CaseType.INDEX ? "index" : "contact")
					.content(jackson.writeValueAsString(payload))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated())
					.andExpect(header().string(HttpHeaders.LOCATION, is(notNullValue())))
					.andReturn().getResponse();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private MockHttpServletResponse expectBadRequestOnCreation(TrackedCaseDto payload, CaseType type) {

		try {

			return mvc.perform(post("/hd/cases")
					.param("type", type == CaseType.INDEX ? "index" : "contact")
					.content(jackson.writeValueAsString(payload))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					.andReturn().getResponse();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private MockHttpServletResponse issueCaseUpdate(TrackedCaseDto payload, CaseType type, TrackedCase trackedCase) {

		payload.setLocale(trackedCase.getTrackedPerson().getLocale());
		return issueCaseUpdate(payload, type, trackedCase.getId());
	}

	private MockHttpServletResponse issueCaseUpdate(TrackedCaseDto payload, CaseType type, TrackedCaseIdentifier caseId) {

		try {

			return mvc.perform(put("/hd/cases/{id}", caseId)
					.param("type", type == CaseType.INDEX ? "index" : "contact")
					.content(jackson.writeValueAsString(payload))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn().getResponse();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private MockHttpServletResponse issueToIndexCaseTransformation(TrackedCaseDto payload, TrackedCaseIdentifier id)
			throws Exception, JsonProcessingException {

		return mvc.perform(put("/hd/cases/{id}", id)
				.content(jackson.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse();
	}

	private MockHttpServletResponse expectBadRequestOnTransformationCall(TrackedCaseDto payload,
			TrackedCaseIdentifier caseId) throws Exception, JsonProcessingException {

		return mvc.perform(put("/hd/cases/{id}", caseId)
				.content(jackson.writeValueAsString(payload))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse();
	}

	private MockHttpServletResponse expectBadRequestOnUpdate(TrackedCaseDto payload, CaseType type,
			TrackedCase trackedCase) {

		payload.setLocale(trackedCase.getTrackedPerson().getLocale());

		try {

			return mvc.perform(put("/hd/cases/{caseId}", trackedCase.getId())
					.content(jackson.writeValueAsString(payload))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					.andReturn().getResponse();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void assertMinimalIndexFieldsSet(DocumentContext document, TrackedCaseDto payload) {

		assertMinimalContactFieldsSet(document, payload);
		assertThat(document.read("$.quarantineStartDate", String.class))
				.isNotNull()
				.isEqualTo(payload.getQuarantineStartDate().toString());
		assertThat(document.read("$.quarantineEndDate", String.class))
				.isEqualTo(payload.getQuarantineEndDate().toString());
		assertThat(document.read("$.phone", String.class)).isEqualTo(payload.getPhone());
		assertThat(document.read("$.testDate", String.class)).isEqualTo(payload.getTestDate().toString());
	}

	private static void assertMinimalContactFieldsSet(DocumentContext document, TrackedCaseDto payload) {

		assertThat(document.read("$.firstName", String.class)).isEqualTo(payload.getFirstName());
		assertThat(document.read("$.lastName", String.class)).isEqualTo(payload.getLastName());
		assertThat(document.read("$._links.self.href", String.class)).isNotBlank();
	}
}
