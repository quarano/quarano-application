package quarano.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.auth.Password.UnencryptedPassword;
import quarano.core.DataInitializer;
import quarano.department.DepartmentDataInitializer;
import quarano.tracking.TrackedPersonDataInitializer;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Create some dummy accounts for test and development
 *
 * @author Patrick Otto
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

		log.warn("Test data: creating 7 accounts");

		// person 1 should not have an account yet

		// account for person 2
		accounts.createTrackedPersonAccount("DemoAccount", UnencryptedPassword.of("DemoPassword"), "Markus", "Hanser",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_PERSON2_ID_DEP1);

		// account for person 3
		accounts.createTrackedPersonAccount("test3", UnencryptedPassword.of("test123"), "Sandra", "Schubert",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP2, TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2);

		// account for GA user
		accounts.createStaffAccount("admin", UnencryptedPassword.of("admin"), "Mark", "Muster",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, RoleType.ROLE_HD_ADMIN);

		accounts.createStaffAccount("agent1", UnencryptedPassword.of("agent1"), "Horst", "Hallig",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, RoleType.ROLE_HD_CASE_AGENT);

		accounts.createStaffAccount("agent2", UnencryptedPassword.of("agent2"), "Bettina", "Boot",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, RoleType.ROLE_HD_CASE_AGENT);

		accounts.createStaffAccount("agent3", UnencryptedPassword.of("agent3"), "Heike", "Hirsch",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP2, RoleType.ROLE_HD_CASE_AGENT);
	}
}
