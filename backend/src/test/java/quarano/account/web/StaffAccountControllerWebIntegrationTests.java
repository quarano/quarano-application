package quarano.account.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.account.AccountService;
import quarano.account.RoleType;
import quarano.account.web.StaffAccountRepresentations.StaffAccountCreateInputDto;
import quarano.account.web.StaffAccountRepresentations.StaffAccountUpdateInputDto;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

/**
 * @author Patrick Otto
 * @author Jan Stamer
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoWebIntegrationTest
class StaffAccountControllerWebIntegrationTests {

	private final MockMvc mvc;
	private final AccountService accounts;
	private final MessageSourceAccessor messages;
	private final ObjectMapper jackson;

	@Test
	@WithQuaranoUser("admin")
	void getAccountsSuccessfully() throws Exception {

		// get reference accounts
		var referenceAdminAccount = accounts.findByUsername("admin");
		var referenceAgentAccount = accounts.findByUsername("agent1");
		var referenceNonDepartmentAccount = accounts.findByUsername("DemoAccount");

		var response = mvc.perform(get("/api/hd/accounts"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		// check if user are delivered and sorted by lastname
		var accountEntries = document.read("$..lastName", JSONArray.class);
		var usernameEntries = document.read("$..username", JSONArray.class);

		assertThat(accountEntries).isNotEmpty();
		assertThat(accountEntries).isSorted();
		assertThat(!document.read("$._embedded.accounts[0].username", String.class).isBlank());

		// check if reference hd accounts are present while tracked user accounts are filtered
		assertThat(usernameEntries).containsOnlyOnce(referenceAdminAccount.get().getUsername());
		assertThat(usernameEntries).containsOnlyOnce(referenceAgentAccount.get().getUsername());
		assertThat(usernameEntries).doesNotContain(referenceNonDepartmentAccount.get().getUsername());

	}

	@Test
	@WithQuaranoUser("agent1")
	void getAccountServicesForbiddenForNonAdminHDUser() throws Exception {

		mvc.perform(get("/api/hd/accounts"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithQuaranoUser("DemoAccount")
	void getAccountsForbiddenForTrackedUser() throws Exception {

		mvc.perform(get("/api/hd/accounts"))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithQuaranoUser("admin")
	void createAccountSuccessfully() throws Exception {

		var source = createTestUserInput(RoleType.ROLE_HD_CASE_AGENT, RoleType.ROLE_HD_ADMIN);

		// send request
		var result = mvc.perform(post("/api/hd/accounts")
				.content(jackson.writeValueAsString(source))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();

		// assert user is stored
		assertThat(accounts.findByUsername(source.getUsername())).hasValueSatisfying(account -> {

			// assert response format
			assertThat(JsonPath.parse(result).read("$.username", String.class).equals(source.getUsername()));
			assertThat(JsonPath.parse(result).read("$.firstName", String.class).equals(source.getFirstName()));
			assertThat(JsonPath.parse(result).read("$.lastName", String.class).equals(source.getLastName()));
			assertThat(JsonPath.parse(result).read("$.accountId", String.class).equals(account.getId().toString()));
			assertThat(JsonPath.parse(result).read("$.roles", JSONArray.class).size() == 2);
			assertThat(JsonPath.parse(result).read("$.roles", JSONArray.class).contains("ROLE_HD_CASE_AGENT"));
			assertThat(JsonPath.parse(result).read("$.roles", JSONArray.class).contains("ROLE_HD_ADMIN"));
			assertThatExceptionOfType(PathNotFoundException.class)
					.isThrownBy(() -> JsonPath.parse(result).read("$.password", String.class));
		});
	}

	@Test
	@WithQuaranoUser("admin")
	void rejectsInvalidCharactersForStringFieldsOnCreate() throws Exception {

		// set invalid data
		var source = createTestUserInput(RoleType.ROLE_HD_CASE_AGENT, RoleType.ROLE_HD_ADMIN)
				.setUsername("Demo ! A/Ccount")
				.setEmail("myT@estmaIl@test.com")
				.setFirstName("Hans123")
				.setLastName("Huber123");

		// send request
		var responseBody = mvc.perform(post("/api/hd/accounts")
				.content(jackson.writeValueAsString(source))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(responseBody);

		var usernameMessage = messages.getMessage("UserName");
		var emailMessage = messages.getMessage("Email");
		var firstName = messages.getMessage("Pattern.firstName");
		var lastName = messages.getMessage("Pattern.lastName");

		assertThat(document.read("$.firstName", String.class)).isEqualTo(firstName);
		assertThat(document.read("$.lastName", String.class)).isEqualTo(lastName);
		assertThat(document.read("$.username", String.class)).isEqualTo(usernameMessage);
		assertThat(document.read("$.email", String.class)).isEqualTo(emailMessage);
	}

	@Test
	@WithQuaranoUser("admin")
	void rejectsInvalidCharactersForStringFieldsOnUpdate() throws Exception {

		// get reference accounts
		var agent1 = accounts.findByUsername("agent1");
		var dto = new StaffAccountUpdateInputDto()
				.setUsername("Demo ! A/Ccount")
				.setEmail("myT@estmaIl@test.com")
				.setFirstName("Hans123")
				.setLastName("Huber123");

		var response = mvc.perform(put("/api/hd/accounts/" + agent1.get().getId())
				.content(jackson.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON)) // )
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		var usernameMessage = messages.getMessage("UserName");
		var emailMessage = messages.getMessage("Email");
		var firstName = messages.getMessage("Pattern.firstName");
		var lastName = messages.getMessage("Pattern.lastName");

		assertThat(document.read("$.firstName", String.class)).isEqualTo(firstName);
		assertThat(document.read("$.lastName", String.class)).isEqualTo(lastName);
		assertThat(document.read("$.username", String.class)).isEqualTo(usernameMessage);
		assertThat(document.read("$.email", String.class)).isEqualTo(emailMessage);
	}

	@Test
	@WithQuaranoUser("admin")
	void resetPasswordOfStaffAccountFromSameDepartment() throws Exception {

		var agent1 = accounts.findByUsername("agent1").orElseThrow();
		var password = "MyN3wAgentPassw0rD";
		var newPassword = Map.of("password", password, "passwordConfirm", password);

		mvc.perform(put("/api/hd/accounts/{id}/password", agent1.getId())
				.content(jackson.writeValueAsString(newPassword))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	@WithQuaranoUser("admin")
	void resetPasswordOfStaffAccountFromDifferentDepartment() throws Exception {

		var agent3 = accounts.findByUsername("agent3").orElseThrow();
		var password = "MyN3wAgentPassw0rD";
		var newPassword = Map.of("password", password, "passwordConfirm", password);

		mvc.perform(put("/api/hd/accounts/{id}/password", agent3.getId())
				.content(jackson.writeValueAsString(newPassword))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithQuaranoUser("admin")
	void resetPasswordOfStaffAccountWithNonMatchingPasswords() throws Exception {

		var agent1 = accounts.findByUsername("agent1").orElseThrow();
		var password = "MyN3wAgentPassw0rD";
		var newPassword = Map.of("password", password, "passwordConfirm", "-not matching-");

		var responseBody = mvc.perform(put("/api/hd/accounts/{id}/password", agent1.getId())
				.content(jackson.writeValueAsString(newPassword))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(responseBody);

		var nonMatchingPassword = messages.getMessage("NonMatching.password");

		assertThat(document.read("$.password", String.class)).isEqualTo(nonMatchingPassword);
		assertThat(document.read("$.passwordConfirm", String.class)).isEqualTo(nonMatchingPassword);
	}

	@Test
	@WithQuaranoUser("admin")
	void resetPasswordOfUnknownAccount() throws Exception {

		var password = "MyN3wAgentPassw0rD";
		var newPassword = Map.of("password", password, "passwordConfirm", password);

		mvc.perform(put("/api/hd/accounts/{id}/password", UUID.randomUUID().toString())
				.content(jackson.writeValueAsString(newPassword))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithQuaranoUser("admin")
	void resetPasswordOfUserIsNotPossible() throws Exception {

		var user1 = accounts.findByUsername("user1").orElseThrow();
		var password = "MyN3wAgentPassw0rD";
		var newPassword = Map.of("password", password, "passwordConfirm", password);

		mvc.perform(put("/api/hd/accounts/{id}/password", user1.getId())
				.content(jackson.writeValueAsString(newPassword))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	private StaffAccountCreateInputDto createTestUserInput(RoleType... roles) {

		return new StaffAccountCreateInputDto()
				.setUsername("aNewUsernameNotUsedBefore")
				.setFirstName("Hansi")
				.setLastName("Vogt")
				.setPassword("aSecretPassword")
				.setPasswordConfirm("aSecretPassword")
				.setEmail("myemail@email.de")
				.setRoles(Stream.of(roles).map(Object::toString).collect(Collectors.toList()));
	}
}
