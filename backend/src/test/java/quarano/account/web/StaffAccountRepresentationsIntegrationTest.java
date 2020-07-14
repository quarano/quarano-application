package quarano.account.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.account.Account;
import quarano.account.AccountService;
import quarano.account.RoleRepository;
import quarano.account.RoleType;
import quarano.core.EmailAddress;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author Felix Schultze
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class StaffAccountRepresentationsIntegrationTest {

	private final AccountService accounts;
	private final StaffAccountRepresentations representations;
	private final RoleRepository roles;

	@Test // CORE-179
	void mapsDtoOnExistingInstance() {

		var username = "agent1";
		var agent1 = accounts.findByUsername(username).get();
		var source = StaffAccountRepresentations.StaffAccountUpdateInputDto.of()
				.setFirstName("Max")
				.setLastName("Mustermann")
				.setEmail("max.mustermann@acme.com")
				.setUsername("agent0815")
				.setRoles(List.of(RoleType.ROLE_HD_ADMIN.getCode()));

		Account mappedAccount = representations.from(source, agent1);

		assertThat(mappedAccount.getFirstname()).isEqualTo("Max");
		assertThat(mappedAccount.getLastname()).isEqualTo("Mustermann");
		assertThat(mappedAccount.getEmail()).isEqualTo(EmailAddress.of("max.mustermann@acme.com"));
		assertThat(mappedAccount.getUsername()).isEqualTo("agent0815");
		assertThat(mappedAccount.getRoles()).containsExactly(roles.findByType(RoleType.ROLE_HD_ADMIN));
	}
}
