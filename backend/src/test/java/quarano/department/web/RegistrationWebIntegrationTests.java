package quarano.department.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static quarano.department.activation.ActivationCodeException.Status.*;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.core.I18nProperties;
import quarano.core.web.QuaranoHttpHeaders;
import quarano.department.RegistrationManagement;
import quarano.department.TrackedCaseRepository;
import quarano.department.activation.ActivationCode;
import quarano.department.activation.ActivationCodeDataInitializer;
import quarano.department.activation.ActivationCodeService;
import quarano.department.web.RegistrationRepresentations.RegistrationDto;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.mediatype.hal.HalLinkDiscoverer;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@QuaranoWebIntegrationTest
@RequiredArgsConstructor
class RegistrationWebIntegrationTests {

	private final MockMvc mvc;
	private final ObjectMapper mapper;
	private final TrackedPersonRepository people;
	private final TrackedCaseRepository cases;
	private final ActivationCodeService codes;
	private final HalLinkDiscoverer links;
	private final MessageSourceAccessor messages;
	private final RegistrationManagement registration;
	private final I18nProperties i18nProperties;

	@Test
	void registerNewAccountForClientSuccess() throws Exception {

		// Given
		var activation = createActivation();
		var person = activation.getPerson();

		var password = "myPassword";
		var username = "testusername";

		var registrationDto = new RegistrationDto()
				.setUsername(username)
				.setPassword(password)
				.setPasswordConfirm(password)
				.setDateOfBirth(person.getDateOfBirth())
				.setClientCode(activation.getCode());

		// when
		var response = mvc.perform(post("/registration")
				.header("Origin", "*")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(registrationDto)))
				.andExpect(status().is2xxSuccessful())
				.andReturn()
				.getResponse();

		// then check for token
		var token = response.getHeader(QuaranoHttpHeaders.AUTH_TOKEN);

		assertThat(token).isNotNull();

		// check login works with token
		callUserMeAndCheckSuccess(token, person);

		// check if login works with new account
		token = loginAndCheckSuccess(password, username);

		// and check if token is valid for authentication
		callUserMeAndCheckSuccess(token, person);
	}

	@Test
	void registerNewAccountWithInvalidActivationCodeFails() throws Exception {

		var activation = createActivation();
		var password = "myPassword";
		var username = "testusername";

		var payload = new RegistrationDto()
				.setUsername(username)
				.setPassword(password)
				.setPasswordConfirm(password)
				.setDateOfBirth(activation.getPerson().getDateOfBirth())
				.setClientCode(UUID.randomUUID());

		// when
		var result = expectFailedRegistration(payload);

		var document = JsonPath.parse(result);

		assertThat(document.read("$.clientCode", String.class)).isNotNull();

		// then check that login does not work with new account
		checkLoginFails(username, password);
	}

	@Test
	void registerAccountWithExistingUsernameFails() throws Exception {

		var activation = createActivation();
		var person = activation.getPerson();
		var password = "myPassword";

		var registrationDto = new RegistrationDto()
				.setUsername("DemoAccount")
				.setPassword(password)
				.setPasswordConfirm(password)
				.setDateOfBirth(person.getDateOfBirth())
				.setClientCode(activation.getCode());

		// when
		var payload = expectFailedRegistration(registrationDto);
		var document = JsonPath.parse(payload);

		var invalidUsernameMessage = messages.getMessage("Invalid.accountRegistration.username");

		assertThat(document.read("$.username", String.class)).isEqualTo(invalidUsernameMessage);

		checkLoginFails("DemoAccount", password);
	}

	@Test
	@WithQuaranoUser("agent2")
	void noPendingRegistrationExposesLinkToEnableTracking() throws Exception {

		var harrietteCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON7_ID_DEP1)
				.orElseThrow();

		var response = mvc.perform(get("/hd/cases/{id}/registration", harrietteCase.getId()))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$._links.start-tracking.href", String.class)).isNotBlank();
	}

	@Test
	@WithQuaranoUser("agent2")
	void startsTracking() throws Exception {

		var harryCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON6_ID_DEP1)
				.orElseThrow();

		var response = mvc.perform(put("/hd/cases/{id}/registration", harryCase.getId()))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		var document = JsonPath.parse(response);
		var harry = harryCase.getTrackedPerson();

		assertThat(document.read("$.email", String.class)).contains(harry.getLastName());
		assertThat(document.read("$.email", String.class)).contains("\r\n\r\n==========\r\n\r\n");
		assertThat(document.read("$.email", String.class)).startsWith("Dear Mrs/Mr " + harry.getLastName() + ",");
		assertThat(document.read("$.expirationDate", String.class)).isNotBlank();
		assertThat(document.read("$.activationCode", String.class)).isNotBlank();

		assertThat(links.findLinkWithRel(TrackedCaseLinkRelations.RENEW, response)).isPresent();
		assertThat(links.findLinkWithRel(TrackedCaseLinkRelations.CONCLUDE, response)).isPresent();

		assertThat(codes.getPendingActivationCode(harry.getId())).isPresent();
	}

	@Test // CORE-375
	@WithQuaranoUser("agent2")
	void multiLanguageRegistrationMail() throws Exception {

		// with saved language
		var harryCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON6_ID_DEP1)
				.orElseThrow();

		var response = mvc.perform(put("/hd/cases/{id}/registration", harryCase.getId()))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		var document = JsonPath.parse(response);
		var harry = harryCase.getTrackedPerson();

		assertThat(document.read("$.email", String.class)).contains("\r\n\r\n==========\r\n\r\n");
		assertThat(document.read("$.email", String.class)).startsWith("Dear Mrs/Mr " + harry.getLastName() + ",");
		assertThat(document.read("$.email", String.class)).contains("Sehr geehrte/geehrter Frau/Herr");

		assertThat(codes.getPendingActivationCode(harry.getId())).isPresent();

		// without saved language
		var harrietteCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON7_ID_DEP1)
				.orElseThrow();

		response = mvc.perform(put("/hd/cases/{id}/registration", harrietteCase.getId()))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		document = JsonPath.parse(response);
		var harriette = harrietteCase.getTrackedPerson();

		assertThat(document.read("$.email", String.class)).doesNotContain("==========");
		assertThat(document.read("$.email", String.class))
				.contains("Sehr geehrte/geehrter Frau/Herr " + harriette.getLastName() + ",");

		assertThat(codes.getPendingActivationCode(harriette.getId())).isPresent();

		// with default languge as saved language
		var tanjaCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();

		response = mvc.perform(put("/hd/cases/{id}/registration", tanjaCase.getId()))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		document = JsonPath.parse(response);
		var tanja = tanjaCase.getTrackedPerson();

		assertThat(document.read("$.email", String.class)).doesNotContain("==========");
		assertThat(document.read("$.email", String.class))
				.contains("Sehr geehrte/geehrter Frau/Herr " + tanja.getLastName() + ",");

		assertThat(codes.getPendingActivationCode(tanja.getId())).isPresent();
	}

	@Test
	void rejectsInvalidCharactersForStringFields() throws Exception {

		var person = people.findRequiredById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1);
		var password = "myPassword";

		var registrationDto = new RegistrationDto()
				.setUsername("Demo ! A/Ccount")
				.setPassword(password)
				.setPasswordConfirm(password)
				.setDateOfBirth(person.getDateOfBirth())
				.setClientCode(UUID.fromString(ActivationCodeDataInitializer.ACTIVATIONCODE_PERSON1.getId().toString()));

		// when
		var responseBody = mvc.perform(post("/registration")
				.header("Origin", "*")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(registrationDto)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn()
				.getResponse()
				.getContentAsString();

		var document = JsonPath.parse(responseBody);

		var usernameMessage = messages.getMessage("UserName");

		assertThat(document.read("$.username", String.class)).isEqualTo(usernameMessage);
	}

	@Test
	@Disabled
	void testCheckClientCodeWithInvalidCodeFailsWith404() throws Exception {

		var code = ActivationCodeDataInitializer.ACTIVATIONCODE_PERSON3_CANCELED;

		mvc.perform(get("/client/checkcode/" + code).header("Origin", "*")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn()
				.getResponse()
				.getContentAsString();
	}

	@Test
	@Disabled
	void testCheckClientCodeWithoutCodeFailsWith400() throws Exception {

		mvc.perform(get("/client/checkcode/").header("Origin", "*")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn()
				.getResponse()
				.getContentAsString();
	}

	@Test // CORE-220
	public void registerAccountWithInvalidBirthDateFails() throws Exception {

		var person = people.findRequiredById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1);
		var password = "myPassword";

		var payload = new RegistrationDto()
				.setUsername("DemoAccount4711")
				.setPassword(password)
				.setPasswordConfirm(password)
				.setDateOfBirth(person.getDateOfBirth().plusDays(1))
				.setClientCode(UUID.fromString(ActivationCodeDataInitializer.ACTIVATIONCODE_PERSON1.getId()
						.toString()));

		// when
		var responseBody = expectFailedRegistration(payload);
		var document = JsonPath.parse(responseBody);

		var wrongDateMessage = messages.getMessage("Invalid.accountRegistration.wrongBirthDate");

		assertThat(document.read("$.dateOfBirth", String.class)).isEqualTo(wrongDateMessage);

		checkLoginFails("DemoAccount", password);
	}

	@Test // CORE-355
	void adoptionOfLanguage() throws Exception {

		var newLocale = i18nProperties.getNonDefaultLocale();

		// Given
		var activation = createActivation();
		var person = activation.getPerson();

		var password = "myPassword";
		var username = "testusername";

		var registrationDto = new RegistrationDto()
				.setUsername(username)
				.setPassword(password)
				.setPasswordConfirm(password)
				.setDateOfBirth(person.getDateOfBirth())
				.setClientCode(activation.getCode());

		assertThat(person.getLocale()).isNotNull().isNotEqualTo(newLocale);

		// when
		mvc.perform(post("/registration")
				.header("Origin", "*")
				.locale(newLocale)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(registrationDto)))
				.andExpect(status().is2xxSuccessful())
				.andReturn()
				.getResponse();

		// then check for token
		assertThat(people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1))
				.isPresent()
				.map(TrackedPerson::getLocale)
				.hasValue(newLocale);
	}

	@Test // CORE-474
	void errorMessagesForActivationCodesGetI18Ned() throws Exception {

		var activation = createActivation();
		var person = activation.getPerson();

		var password = "myPassword";
		var username = "testusername";

		var registrationDto = new RegistrationDto()
				.setUsername(username)
				.setPassword(password)
				.setPasswordConfirm(password)
				.setDateOfBirth(person.getDateOfBirth())
				.setClientCode(activation.getCode());

		// Initiate registration

		mvc.perform(post("/registration")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(registrationDto)))
				.andExpect(status().is2xxSuccessful())
				.andReturn()
				.getResponse();

		// Retry registration with different name

		registrationDto.setUsername("someother");

		var response = mvc.perform(post("/registration")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(registrationDto)))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse();

		var document = JsonPath.parse(response.getContentAsString());

		assertThat(document.read("$.clientCode", String.class))
				.isEqualTo(messages.getMessage(USED_OR_CANCELED.getMessageKey()));
	}

	@Test // CORE-469
	@WithQuaranoUser("agent2")
	void startingTrackingCanBeRepeated() throws Exception {

		var personId = TrackedPersonDataInitializer.VALID_TRACKED_PERSON6_ID_DEP1;
		var harryCase = cases.findByTrackedPerson(personId)
				.orElseThrow();

		mvc.perform(put("/hd/cases/{id}/registration", harryCase.getId()))
				.andExpect(status().isOk());

		// Initial activation code
		var code = codes.getPendingActivationCode(personId).orElseThrow();

		assertThatCode(() -> {

			// Re-start registration
			var response = mvc.perform(put("/hd/cases/{id}/registration", harryCase.getId()))
					.andExpect(status().isOk())
					.andReturn().getResponse().getContentAsString();

			// New activation code
			assertThat(codes.getPendingActivationCode(personId)).hasValueSatisfying(it -> {

				// … is different from old
				assertThat(it).isNotEqualTo(code);
				// … got returned from the API call
				assertThat(JsonPath.parse(response).read("$.activationCode", String.class))
						.isEqualTo(it.getId().toString());
			});

		}).doesNotThrowAnyException();
	}

	private void callUserMeAndCheckSuccess(String token, TrackedPerson person) throws Exception {

		String resultDtoStr = mvc.perform(get("/user/me")
				.header("Origin", "*")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.parseMediaType("application/*+json")))
				.andReturn()
				.getResponse()
				.getContentAsString();

		var document = JsonPath.parse(resultDtoStr);

		assertThat(document.read("$.client.firstName", String.class)).isEqualTo(person.getFirstName());
		assertThat(document.read("$.client.lastName", String.class)).isEqualTo(person.getLastName());
	}

	@SuppressWarnings("null")
	private String loginAndCheckSuccess(final String password, final String username) throws Exception {

		var response = mvc.perform(post("/login")
				.header("Origin", "*")
				.contentType(MediaType.APPLICATION_JSON)
				.content(createLoginRequestBody(username, password)))
				.andExpect(status().is2xxSuccessful())
				.andReturn()
				.getResponse();

		var token = response.getHeader(QuaranoHttpHeaders.AUTH_TOKEN);

		assertThat(token).isNotBlank();

		return token;
	}

	private void checkLoginFails(String username, String password) throws Exception {

		// login with new account
		var requestbody = createLoginRequestBody(username, password);

		mvc.perform(post("/login")
				.header("Origin", "*")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestbody))
				.andExpect(status().isUnauthorized());
	}

	private String createLoginRequestBody(String username, String password) throws Exception {
		return mapper.writeValueAsString(Map.of("username", username, "password", password));
	}

	private String expectFailedRegistration(RegistrationDto payload) throws Exception {

		return mvc.perform(post("/registration")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(payload)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn()
				.getResponse()
				.getContentAsString();
	}

	private PersonAndCode createActivation() {
		return createActivation(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1);
	}

	private PersonAndCode createActivation(TrackedPersonIdentifier identifier) {

		var person = people.findRequiredById(identifier);
		var activationCode = registration.initiateRegistration(cases.findByTrackedPerson(person)
				.orElseThrow())
				.get();

		return new PersonAndCode(person, activationCode);
	}

	@Value
	private static class PersonAndCode {

		TrackedPerson person;
		ActivationCode code;

		@SuppressWarnings("null")
		UUID getCode() {
			return (UUID) ReflectionTestUtils.getField(code.getId(), "activationCodeId");
		}
	}
}
