package quarano.department.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.core.web.MapperWrapper;
import quarano.reference.Symptom;
import quarano.reference.SymptomRepository;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;
import quarano.tracking.web.TrackedPersonDto;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
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
	private final @NonNull SymptomRepository symptoms;

	@Test
	@WithQuaranoUser("DemoAccount")
	public void completeEnrollmentDetailsSubmissionMarksStepComplete() throws Exception {

		var source = createValidDetailsInput();

		submitDetailsSuccessfully(source);

		String result = performRequestToGetEnrollementState();

		assertThat(JsonPath.parse(result).read("$.completedPersonalData", boolean.class)).isTrue();
	}

	@Test // CORE-115
	@WithQuaranoUser("DemoAccount")
	public void completeQuestionaireSubmissionMarksStepComplete() throws Exception {

		String result = performRequestToGetEnrollementState();

		var document = JsonPath.parse(result);

		assertThat(document.read("$.completedPersonalData", boolean.class)).isFalse();
		assertThat(document.read("$.completedQuestionnaire", boolean.class)).isFalse();

		var source = createValidDetailsInput();
		submitDetailsSuccessfully(source);

		var questionnaireSource = createValidQuestionnaireInput();
		submitQuestionnaireSuccessfully(questionnaireSource);

		// The first enrollment step is completed
		result = performRequestToGetEnrollementState();
		document = JsonPath.parse(result);

		assertThat(document.read("$.completedPersonalData", boolean.class)).isTrue();
		assertThat(document.read("$.completedQuestionnaire", boolean.class)).isTrue();

		// check if questionnaire data has been stored
		result = performRequestToGetQuestionnaire();
		document = JsonPath.parse(result);

		assertThat(document.read("$.belongToMedicalStaff", Boolean.class)).isTrue();
		assertThat(document.read("$.belongToMedicalStaffDescription", String.class)
				.equals(questionnaireSource.getBelongToMedicalStaffDescription()));
		assertThat(document.read("$.hasSymptoms", Boolean.class)).isTrue();
		assertThat(document.read("$.dayOfFirstSymptoms", String.class)
				.equals(questionnaireSource.getDayOfFirstSymptoms().toString()));
		assertThat(document.read("$.hasPreExistingConditions", Boolean.class)).isFalse();

		var symptoms = document.read("$.symptoms", JSONArray.class);

		assertThat(symptoms).containsExactlyElementsOf(
				List.of("e5cea3b0-c8f4-4e03-a24e-89213f3f6637", "571a03cd-173c-4499-995c-d6a003e8c032"));
	}

	@Test // CORE-115
	@WithQuaranoUser("DemoAccount")
	public void completeQuestionaireWithSymptomsOfToday() throws Exception {

		var source = createValidDetailsInput();
		submitDetailsSuccessfully(source);

		var questionnaireSource = createValidQuestionnaireInput();
		questionnaireSource.setDayOfFirstSymptoms(LocalDate.now());
		submitQuestionnaireSuccessfully(questionnaireSource);
	}

	@Test // CORE-115
	@WithQuaranoUser("DemoAccount")
	public void incompleteQuestionnaireIsRefused() throws Exception {

		var source = createValidDetailsInput();
		submitDetailsSuccessfully(source);

		var questionnaireSource = createValidQuestionnaireInput();

		// make input-data incomplete
		questionnaireSource.setBelongToMedicalStaff(null);
		questionnaireSource.setHasPreExistingConditions(null);
		questionnaireSource.setHasSymptoms(null);

		String result = submitQuestionnaireExpectBadRequest(questionnaireSource);
		var document = JsonPath.parse(result);

		assertThat(document.read("$.belongToMedicalStaff", String.class)).isNotNull();
		assertThat(document.read("$.hasPreExistingConditions", String.class)).isNotNull();
		assertThat(document.read("$.hasSymptoms", String.class)).isNotNull();

		// The first enrollment step is completed
		result = performRequestToGetEnrollementState();
		document = JsonPath.parse(result);

		assertThat(document.read("$.completedPersonalData", boolean.class)).isTrue();
		assertThat(document.read("$.completedQuestionnaire", boolean.class)).isFalse();

		// check if questionnaire data is still empty, because it should not have been saved
		result = performRequestToGetQuestionnaire();

		expectResponseCarriesEmptyQuestionnaire(result);

		document = JsonPath.parse(result);

		assertThat(document.read("$.belongToMedicalStaff", Boolean.class)).isNull();
		assertThat(document.read("$.hasPreExistingConditions", Boolean.class)).isNull();
		assertThat(document.read("$.hasSymptoms", Boolean.class)).isNull();

	}

	@Test
	@WithQuaranoUser("DemoAccount")
	public void getInitialEmptyQuestionnaireSuccessfully() throws Exception {
		var result = performRequestToGetQuestionnaire();
		expectResponseCarriesEmptyQuestionnaire(result);
	}

	private void expectResponseCarriesEmptyQuestionnaire(String result) {
		var document = JsonPath.parse(result);

		assertThat(document.read("$.belongToMedicalStaff", Boolean.class)).isNull();
		assertThat(document.read("$.belongToMedicalStaffDescription", String.class)).isNull();
		assertThat(document.read("$.hasPreExistingConditions", Boolean.class)).isNull();
		assertThat(document.read("$.hasPreExistingConditionsDescription", String.class)).isNull();
		assertThat(document.read("$.hasSymptoms", String.class)).isNull();
		assertThat(document.read("$.dayOfFirstSymptoms", String.class)).isNull();
		assertThat(document.read("$.symptoms", JSONArray.class)).isNull();
		assertThat(document.read("$.familyDoctor", String.class)).isNull();
		assertThat(document.read("$.guessedOriginOfInfection", String.class)).isNull();
		assertThat(document.read("$.hasContactToVulnerablePeople", Boolean.class)).isNull();
		assertThat(document.read("$.hasContactToVulnerablePeopleDescription", String.class)).isNull();
	}

	private QuestionnaireDto createValidQuestionnaireInput() {

		Symptom cough = symptoms.findById(UUID.fromString("e5cea3b0-c8f4-4e03-a24e-89213f3f6637")).get();
		Symptom neckProblems = symptoms.findById(UUID.fromString("571a03cd-173c-4499-995c-d6a003e8c032")).get();

		var questionnaire = new QuestionnaireDto();
		questionnaire.setBelongToMedicalStaff(Boolean.TRUE);
		questionnaire.setBelongToMedicalStaffDescription("Description1");
		questionnaire.setDayOfFirstSymptoms(LocalDate.now().minusDays(3));
		questionnaire.setHasSymptoms(Boolean.TRUE);
		questionnaire.setSymptoms(Arrays.asList(cough.getId(), neckProblems.getId()));
		questionnaire.setHasPreExistingConditions(Boolean.FALSE);

		return questionnaire;
	}

	private String performRequestToGetEnrollementState() throws UnsupportedEncodingException, Exception {
		String result = mvc.perform(get("/api/enrollment"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		return result;
	}

	private String performRequestToGetQuestionnaire() throws UnsupportedEncodingException, Exception {
		String result = mvc.perform(get("/api/enrollment/questionnaire"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		return result;
	}

	private void submitDetailsSuccessfully(TrackedPersonDto source)
			throws UnsupportedEncodingException, Exception, JsonProcessingException {
		mvc.perform(put("/api/enrollment/details")
				.content(jackson.writeValueAsString(source))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
	}

	private void submitQuestionnaireSuccessfully(QuestionnaireDto source)
			throws UnsupportedEncodingException, Exception, JsonProcessingException {
		mvc.perform(put("/api/enrollment/questionnaire")
				.content(jackson.writeValueAsString(source))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
	}

	private String submitQuestionnaireExpectBadRequest(QuestionnaireDto source)
			throws UnsupportedEncodingException, Exception, JsonProcessingException {
		return mvc.perform(put("/api/enrollment/questionnaire")
				.content(jackson.writeValueAsString(source))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
	}

	private TrackedPersonDto createValidDetailsInput() {
		var person = repository.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON2_ID_DEP1).orElseThrow();
		var source = mapper.map(person, TrackedPersonDto.class);

		source.setStreet("Street");
		source.setZipCode("68199");
		source.setCity("Mannheim");
		return source;
	}

	@Test
	@WithQuaranoUser("DemoAccount")
	void rejectsInvalidCharactersForStringFields() throws Exception {

		var person = repository.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON2_ID_DEP1).orElseThrow();
		var source = mapper.map(person, TrackedPersonDto.class);

		source.setFirstName("Michael 123")
				.setLastName("Mustermann 123")
				.setPhone("0123456789")
				.setCity("city 123")
				.setStreet("\\")
				.setHouseNumber("@");

		// When all enrollment details were submitted
		var responseBody = mvc.perform(put("/api/enrollment/details")
				.content(jackson.writeValueAsString(source))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
		var document = JsonPath.parse(responseBody);

		Locale locale = person.getLocale();
		var houseNumber = messages.getMessage("Pattern.houseNumber", locale);
		var firstName = messages.getMessage("Pattern.firstName", locale);
		var lastName = messages.getMessage("Pattern.lastName", locale);
		var city = messages.getMessage("Pattern.city", locale);
		var street = messages.getMessage("Pattern.street", locale);

		assertThat(document.read("$.firstName", String.class)).isEqualTo(firstName);
		assertThat(document.read("$.lastName", String.class)).isEqualTo(lastName);
		assertThat(document.read("$.city", String.class)).isEqualTo(city);
		assertThat(document.read("$.street", String.class)).isEqualTo(street);
		assertThat(document.read("$.houseNumber", String.class)).isEqualTo(houseNumber);
	}
}
