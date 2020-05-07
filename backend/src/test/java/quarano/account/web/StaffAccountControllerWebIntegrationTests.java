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
package quarano.account.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.account.Account;
import quarano.account.AccountService;
import quarano.account.DepartmentRepository;
import quarano.account.RoleType;
import quarano.account.web.StaffAccountRepresentations.StaffAccountCreateInputDto;
import quarano.department.TrackedCaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

/**
 * @author Patrick Otto
 */
@RequiredArgsConstructor
@QuaranoWebIntegrationTest
class StaffAccountControllerWebIntegrationTests {

	private final MockMvc mvc;
	private final TrackedCaseRepository cases;
	private final AccountService accounts;
	private final DepartmentRepository departments;
	
	private final ObjectMapper jackson;

	@Test
	@WithQuaranoUser("admin")
	void getAccountsSuccessfully() throws Exception {
		
		// get reference accounts
		var referenceAdminAccount = accounts.findByUsername("admin");
		var referenceAgentAccount = accounts.findByUsername("agent1");
		var referenceNonDepartmentAccount = accounts.findByUsername("DemoAccount");

		var response = mvc.perform(get("/api/hd/accounts")) //
				.andExpect(status().isOk()) //
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

		mvc.perform(get("/api/hd/accounts")) //
				.andExpect(status().isForbidden()) //
				.andReturn().getResponse().getContentAsString();
	}	
	
	@Test
	@WithQuaranoUser("DemoAccount")
	void getAccountsForbiddenForTrackedUser() throws Exception {

		mvc.perform(get("/api/hd/accounts")) //
				.andExpect(status().isForbidden()) //
				.andReturn().getResponse().getContentAsString();
	}		
	
	@Test
	@WithQuaranoUser("admin")
	void createAccountSuccessfully() throws Exception {	
		
		var source = createTestUserInput(RoleType.ROLE_HD_CASE_AGENT, RoleType.ROLE_HD_ADMIN);
		
		// send request
		var result = mvc.perform(post("/api/hd/accounts") //
				.content(jackson.writeValueAsString(source)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isCreated()) //
				.andReturn().getResponse().getContentAsString();

		// assert user is stored
		Optional<Account> account = accounts.findByUsername(source.getUsername());
		assertThat(account.isPresent());
		
		// assert response format
		assertThat(JsonPath.parse(result).read("$.username", String.class).equals(source.getUsername()));
		assertThat(JsonPath.parse(result).read("$.firstName", String.class).equals(source.getFirstName()));
		assertThat(JsonPath.parse(result).read("$.lastName", String.class).equals(source.getLastName()));
		assertThat(JsonPath.parse(result).read("$.accountId", String.class).equals(account.get().getId().toString()));
		assertThat(JsonPath.parse(result).read("$.roles", JSONArray.class).size() == 2 );
		assertThat(JsonPath.parse(result).read("$.roles", JSONArray.class).contains("ROLE_HD_CASE_AGENT") );
		assertThat(JsonPath.parse(result).read("$.roles", JSONArray.class).contains("ROLE_HD_ADMIN") );
		assertThatExceptionOfType(PathNotFoundException.class).isThrownBy(() -> JsonPath.parse(result).read("$.password", String.class));
		
		
	}

	private StaffAccountCreateInputDto createTestUserInput(RoleType... roles) {
		List<String> rolesToAdd = new ArrayList<>();
		
		for(RoleType type: roles) {
			rolesToAdd.add(type.toString());
		}
		
		var source = StaffAccountCreateInputDto.of();
		source.setUsername("aNewUsernameNotUsedBefore");
		source.setFirstName("Hansi");
		source.setLastName("Vogt");
		source.setPassword("aSecretPassword");
		source.setPasswordConfirm("aSecretPassword");
		source.setEmail("myemail@email.de");
		source.setRoles(rolesToAdd);
		return source;
	}
}
