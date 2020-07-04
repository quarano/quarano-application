package quarano.account;


import static org.assertj.core.api.Assertions.*;

import quarano.QuaranoIntegrationTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import quarano.account.Password.UnencryptedPassword;

@QuaranoIntegrationTest
class AccountServiceMouleTests {

	private AccountService service;
	@Mock private RoleRepository roles;
	@Autowired private AccountRepository accounts;
	@Autowired private PasswordEncoder encoder;

	@BeforeEach
	void setup() {
		service = new AccountService(encoder, accounts, roles);
	}

	@Test
	void testFindStaffAccountsDeliversOnlySatff() {

		var departmentId = DepartmentDataInitializer.DEPARTMENT_ID_DEP1;
		var referenceAdminAccount = accounts.findByUsername("admin");
		var referenceAgentAccount = accounts.findByUsername("agent1");
		
		var accounts = service.findStaffAccountsFor(departmentId);

		assertThat(accounts).allMatch(it -> hasAtLeastOneDepartmentRole(it));
		assertThat(accounts.size() > 0);
		assertThat(accounts).containsOnlyOnce(referenceAdminAccount.get());
		assertThat(accounts).containsOnlyOnce(referenceAgentAccount.get());

	}

	@Test
	void changePasswordForUser() {
		var unencryptedPassword = UnencryptedPassword.of("12345");
		var agentAccount = accounts.findByUsername("user1");
		assertThat(agentAccount.get().getPassword().isExpired()).isFalse();

		var updatedAgentAccount = service.changePassword(unencryptedPassword, agentAccount.get());

		assertThat(service.matches(unencryptedPassword, updatedAgentAccount.getPassword())).isTrue();
		assertThat(updatedAgentAccount.getPassword().isExpired()).isFalse();
	}

	@Test
	void resetStaffAccountPasswordForStaffAccount() {
		var unencryptedPassword = UnencryptedPassword.of("12345");
		var agentAccount = accounts.findByUsername("agent3");
		assertThat(agentAccount.get().getPassword().isExpired()).isFalse();

		var updatedAgentAccount = service.resetStaffAccountPassword(unencryptedPassword, agentAccount.get());

		assertThat(service.matches(unencryptedPassword, updatedAgentAccount.getPassword())).isTrue();
		assertThat(updatedAgentAccount.getPassword().isExpired()).isTrue();
	}

	@Test
	void resetStaffAccountPasswordForUser() {
		var unencryptedPassword = UnencryptedPassword.of("12345");
		var agentAccount = accounts.findByUsername("user1");

		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> service.resetStaffAccountPassword(unencryptedPassword, agentAccount.get()))
				.withMessage("Password reset allowed for staff accounts only.");
	}

	private boolean hasAtLeastOneDepartmentRole(Account account) {
		return account.getRoles().stream().filter(x -> x.getRoleType().isDepartmentRole()).findFirst().isPresent();
	}

}
