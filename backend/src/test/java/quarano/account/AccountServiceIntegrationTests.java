package quarano.account;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.account.Password.UnencryptedPassword;
import quarano.core.EmailAddress;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@QuaranoIntegrationTest
class AccountServiceIntegrationTests {

	private final AccountRepository accounts;
	private final PasswordEncoder encoder;
	private final AuthenticationManager authentication;

	@Mock RoleRepository roles;
	AccountService service;

	@BeforeEach
	void setup() {
		service = new AccountService(encoder, accounts, roles, authentication);
	}

	@Test
	void testFindStaffAccountsDeliversOnlyStaff() {

		var departmentId = DepartmentDataInitializer.DEPARTMENT_ID_DEP1;
		var referenceAdminAccount = accounts.findByUsername("admin").get();
		var referenceAgentAccount = accounts.findByUsername("agent1").get();

		var accounts = service.findStaffAccountsFor(departmentId);

		assertThat(accounts).allMatch(it -> hasAtLeastOneDepartmentRole(it));
		assertThat(accounts.size() > 0);
		assertThat(accounts).containsOnlyOnce(referenceAdminAccount, referenceAgentAccount);
	}

	@Test // CORE-231
	@WithQuaranoUser("user1")
	void changePasswordForUser() {

		var unencryptedPassword = UnencryptedPassword.of("12345");
		var agentAccount = accounts.findByUsername("user1").orElseThrow();

		assertThat(agentAccount.getPassword().isExpired()).isFalse();

		var updatedAgentAccount = service.changePassword(unencryptedPassword, agentAccount);

		assertThat(service.matches(unencryptedPassword, updatedAgentAccount.getPassword())).isTrue();
		assertThat(updatedAgentAccount.getPassword().isExpired()).isFalse();
	}

	@Test // CORE-231
	@WithQuaranoUser("admin")
	void resetStaffAccountPasswordForStaffAccount() {

		var unencryptedPassword = UnencryptedPassword.of("12345");
		var agentAccount = accounts.findByUsername("agent3").orElseThrow();
		var password = agentAccount.getPassword();

		assertThat(password.isExpired()).isFalse();

		var updatedAgentAccount = service.changePassword(unencryptedPassword, agentAccount);
		var updatedPassword = updatedAgentAccount.getPassword();

		assertThat(service.matches(unencryptedPassword, updatedPassword)).isTrue();
		assertThat(updatedPassword.isExpired()).isTrue();
	}

	@Test // CORE-231
	void rejectsChangingOfPasswordForTrackedUser() {

		var unencryptedPassword = UnencryptedPassword.of("12345");
		var agentAccount = accounts.findByUsername("user1").orElseThrow();

		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> service.changePassword(unencryptedPassword, agentAccount))
				.withMessage("Tracked people can only change their passwords themselves!");
	}

	@Test // CORE-231
	@WithQuaranoUser("admin")
	void adminsPasswordIsNotExpiredIfSheChangesItHerself() {

		var account = accounts.findByUsername("admin").orElseThrow();

		var result = service.changePassword(UnencryptedPassword.of("test"), account);

		assertThat(result.getPassword().isExpired()).isFalse();
	}

	@Test // CORE-231
	@WithQuaranoUser("admin")
	void adminCreatingAnAccountResultsInExpiredPassword() throws InterruptedException {

		var account = accounts.findByUsername("admin").orElseThrow();

		var result = service.createStaffAccount("username", UnencryptedPassword.of("password"), "Michael", "Mustermann",
				EmailAddress.of("michael@mustermann.de"), account.getDepartmentId(), RoleType.ROLE_HD_CASE_AGENT);
		// short wait helps to make comparison "now" vs "just moments before" more reliable
		Thread.sleep(10);
		assertThat(result.getPassword().isExpired()).isTrue();
	}

	private static boolean hasAtLeastOneDepartmentRole(Account account) {

		return account.getRoles()
				.stream()
				.map(Role::getRoleType)
				.filter(RoleType::isDepartmentRole)
				.findFirst()
				.isPresent();
	}
}
