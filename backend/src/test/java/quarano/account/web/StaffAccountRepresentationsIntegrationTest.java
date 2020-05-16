package quarano.account.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.account.Account;
import quarano.account.AccountService;
import quarano.account.Role;
import quarano.account.RoleType;
import quarano.core.EmailAddress;

import java.util.List;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

@QuaranoIntegrationTest
@RequiredArgsConstructor
class StaffAccountRepresentationsIntegrationTest {
	private final AccountService accounts;
	private final StaffAccountRepresentations representations;

	@Test
	void testFromUpdateDto() {
		var username = "agent1";
		var agent1 = accounts.findByUsername(username).get();

		Account mappedAccount = representations.from(agent1, StaffAccountRepresentations.StaffAccountUpdateInputDto.of()
				.setFirstName("Max")
				.setLastName("Mustermann")
				.setEmail("max.mustermann@acme.com")
				.setUsername("agent0815")
				.setRoles(List.of(RoleType.ROLE_HD_ADMIN.getCode()))
		);

		assertThat(mappedAccount.getFirstname()).isEqualTo("Max");
		assertThat(mappedAccount.getLastname()).isEqualTo("Mustermann");
		assertThat(mappedAccount.getEmail()).isEqualTo(EmailAddress.of("max.mustermann@acme.com"));
		assertThat(mappedAccount.getUsername()).isEqualTo("agent0815");
		assertThat(mappedAccount.getRoles()).haveExactly(1, new Condition<>("role") {
			@Override
			public boolean matches(Role value) {
				return value.getRoleType().equals(RoleType.ROLE_HD_ADMIN);
			}
		});
	}


}
