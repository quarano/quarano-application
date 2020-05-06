package quarano.account;


import static org.assertj.core.api.Assertions.*;

import quarano.QuaranoIntegrationTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

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

	private boolean hasAtLeastOneDepartmentRole(Account account) {
		return account.getRoles().stream().filter(x -> x.getRoleType().isDepartmentRole()).findFirst().isPresent();
	}

}
