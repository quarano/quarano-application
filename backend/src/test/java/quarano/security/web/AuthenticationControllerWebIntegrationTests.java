package quarano.security.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.account.AccountService;
import quarano.account.Password.UnencryptedPassword;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.security.web.AuthenticationController.AuthenticationRequest;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;
import quarano.user.web.UserLinkRelations;

import java.util.Locale;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoWebIntegrationTest
class AuthenticationControllerWebIntegrationTests extends AbstractDocumentation {

	private final TrackedCaseRepository cases;
	private final ObjectMapper jackson;
	private final AccountService accounts;
	private final LinkDiscoverers discoverers;
	final TrackedPersonRepository people;

	@Test
	void rejectsLoginForPersonWithCaseConcluded() throws Exception {

		var siggisCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_SEC1_ID_DEP1)
				.orElseThrow()
				.conclude();

		cases.save(siggisCase);

		mvc.perform(post("/login")
				.content(jackson.writeValueAsString(new AuthenticationRequest("secUser1", "secur1tyTest!")))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	@Test
	void logsInTrackedUserWithOpenCase() throws Exception {
		assertSuccessfulLogin("secUser1", "secur1tyTest!", null);
	}

	@Test
	void logsInStaffMember() throws Exception {
		assertSuccessfulLogin("agent1", "agent1", null);
	}

	@Test
	void logsInAdmin() throws Exception {
		assertSuccessfulLogin("admin", "admin", null);
	}

	@Test // CORE-231
	@WithQuaranoUser("admin")
	void loggingInWithExpiredPasswordIndicatesPasswordChangeNeeded() throws Exception {

		// Given the password of agent4 changed by an admin
		var password = UnencryptedPassword.of("test");
		var account = accounts.changePassword(password, accounts.findByUsername("agent4").orElseThrow());

		// When logging in
		var result = mvc.perform(post("/login")
				.content(jackson.writeValueAsString(new AuthenticationRequest(account.getUsername(), "test")))
				.contentType(MediaType.APPLICATION_JSON))

				// We expect the expiration of the password to be indicated by a links pointing to the
				// resource changing the password
				.andExpect(status().isOk())
				.andDo(documentExpiredPassword())
				.andReturn().getResponse().getContentAsString();

		LinkDiscoverer links = discoverers.getRequiredLinkDiscovererFor(MediaTypes.HAL_JSON);

		assertThat(links.findRequiredLinkWithRel(IanaLinkRelations.NEXT, result).getHref()).contains("/user/me/password");
		assertThat(links.findRequiredLinkWithRel(UserLinkRelations.CHANGE_PASSWORD, result).getHref())
				.contains("/user/me/password");
	}

	@Test // CORE-355
	void adoptionOfLanguage() throws Exception {

		var newLocale = Locale.CANADA_FRENCH;

		var person = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2);
		assertThat(person).isPresent()
				.map(TrackedPerson::getLocale)
				.isEmpty();

		assertSuccessfulLogin("test3", "test123", newLocale);

		person = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2);
		assertThat(person).isPresent()
				.map(TrackedPerson::getLocale)
				.hasValue(newLocale);
	}

	@Test // CORE-355
	void noAdoptionOfLanguage() throws Exception {

		var newLocale = Locale.CANADA_FRENCH;

		var person = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON2_ID_DEP1);
		assertThat(person).isPresent()
				.map(TrackedPerson::getLocale)
				.hasValueSatisfying(it -> ObjectUtils.notEqual(it, newLocale));

		var locale = person.get().getLocale();

		assertSuccessfulLogin("DemoAccount", "DemoPassword", null);

		person = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON2_ID_DEP1);
		assertThat(person).isPresent()
				.map(TrackedPerson::getLocale)
				.hasValueSatisfying(it -> ObjectUtils.notEqual(it, newLocale))
				.hasValue(locale);
	}

	@Test // CORE-477
	void rejectsLoginForExternalZip() throws Exception {

		var trackedCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON2_ID_DEP1)
				.map(TrackedCase::markAsExternalZip)
				.map(cases::save)
				.orElseThrow();

		mvc.perform(post("/login")
				.locale(trackedCase.getTrackedPerson().getLocale())
				.content(jackson.writeValueAsString(new AuthenticationRequest("DemoAccount", "DemoPassword"))))
				.andExpect(status().isForbidden());
	}

	private static ResultHandler documentExpiredPassword() {

		var links = relaxedLinks(
				linkWithRel("next").description("Pointing to the next step to gather information. "
						+ "Usually the user dashboard but can also be the resource to <<authentication.change-password, change the password>> if the change-password link is present (see below)"),
				linkWithRel("change-password").description(
						"Indicates the given password is expired. Follow up guiding the user to <<authentication.change-password, change her password>>."));

		return DocumentationFlow.of("password-expired").document("login", links);
	}

	private void assertSuccessfulLogin(String username, String password, Locale locale) throws Exception {

		mvc.perform(post("/login")
				.locale(locale)
				.content(jackson.writeValueAsString(new AuthenticationRequest(username, password)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}
}
