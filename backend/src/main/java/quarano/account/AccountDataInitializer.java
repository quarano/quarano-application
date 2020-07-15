package quarano.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.Password.UnencryptedPassword;
import quarano.core.DataInitializer;
import quarano.core.EmailAddress;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Create some dummy accounts for test and development
 *
 * @author Patrick Otto
 * @author Oliver Drotbohm
 * @author Jan Stamer
 */
@Component
@Order(500)
@RequiredArgsConstructor
@Slf4j
class AccountDataInitializer implements DataInitializer {

	private final AccountService accounts;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		log.warn("Test data: creating test accounts…");

		// account for GA user
		accounts.createStaffAccount("admin", UnencryptedPassword.of("admin"), "Mark", "Muster",
				EmailAddress.of("muster@department1.de"), DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				RoleType.ROLE_HD_ADMIN);

		accounts.createStaffAccount("agent1", UnencryptedPassword.of("agent1"), "Horst", "Hallig",
				EmailAddress.of("hallig@department1.de"), DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				RoleType.ROLE_HD_CASE_AGENT);

		accounts.createStaffAccount("agent2", UnencryptedPassword.of("agent2"), "Bettina", "Boot",
				EmailAddress.of("boot@department1.de"), DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				RoleType.ROLE_HD_CASE_AGENT);

		accounts.createStaffAccount("agent3", UnencryptedPassword.of("agent3"), "Heike", "Hirsch",
				EmailAddress.of("hirsch@department1.de"), DepartmentDataInitializer.DEPARTMENT_ID_DEP2,
				RoleType.ROLE_HD_CASE_AGENT);

		accounts.createStaffAccount("agent4", UnencryptedPassword.of("agent4"), "Karlotta", "Kirsche",
				EmailAddress.of("kirsche@department1.de"), DepartmentDataInitializer.DEPARTMENT_ID_DEP2,
				RoleType.ROLE_HD_CASE_AGENT);

		// accounts for security tests
		var securityTestPassword = UnencryptedPassword.of("secur1tyTest!");

		accounts.createStaffAccount("secGama1", securityTestPassword, "Maja", "Menzel",
				EmailAddress.of("menzel@department1.de"), DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				RoleType.ROLE_HD_CASE_AGENT);

		accounts.createStaffAccount("secGama2", securityTestPassword, "Toni", "Tüpper",
				EmailAddress.of("tuepper@department1.de"), DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				RoleType.ROLE_HD_CASE_AGENT);

		accounts.createStaffAccount("secGama3", securityTestPassword, "Lars", "Lüppel",
				EmailAddress.of("lueppel@department1.de"), DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				RoleType.ROLE_HD_CASE_AGENT);

		accounts.createStaffAccount("secGama4", securityTestPassword, "Bernd", "Böttcher",
				EmailAddress.of("boettcher@department1.de"), DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				RoleType.ROLE_HD_CASE_AGENT);

		accounts.createStaffAccount("secAdmin1", securityTestPassword, "Alfons", "Adminus",
				EmailAddress.of("adminus@department1.de"), DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				RoleType.ROLE_HD_ADMIN);

		accounts.createStaffAccount("secAdmin2", securityTestPassword, "Ariana", "Admina",
				EmailAddress.of("admina@department1.de"), DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				RoleType.ROLE_HD_ADMIN);

		accounts.createStaffAccount("secAdmin3", securityTestPassword, "Alber", "Admino",
				EmailAddress.of("admino@department1.de"), DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				RoleType.ROLE_HD_ADMIN);

		accounts.createStaffAccount("secAdmin4", securityTestPassword, "Agatha", "Adminiki",
				EmailAddress.of("adminki@department1.de"), DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				RoleType.ROLE_HD_ADMIN);

		// user accounts
		accounts.createTrackedPersonAccount("user1", UnencryptedPassword.of("user1"), "Daniel", "Dilemma",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1);
	}
}
