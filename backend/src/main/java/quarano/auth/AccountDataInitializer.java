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
		
		// account for person 4
		accounts.createTrackedPersonAccount("test4", UnencryptedPassword.of("test123"), "Gustav", "Meier",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_PERSON4_ID_DEP1);
		
		// account for person 5
		accounts.createTrackedPersonAccount("test5", UnencryptedPassword.of("test123"), "Nadine", "Ebert",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_PERSON5_ID_DEP1);

		// account for GA user
		accounts.createStaffAccount("admin", UnencryptedPassword.of("admin"), "Mark", "Muster",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, RoleType.ROLE_HD_ADMIN);

		accounts.createStaffAccount("agent1", UnencryptedPassword.of("agent1"), "Horst", "Hallig",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, RoleType.ROLE_HD_CASE_AGENT);

		accounts.createStaffAccount("agent2", UnencryptedPassword.of("agent2"), "Bettina", "Boot",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, RoleType.ROLE_HD_CASE_AGENT);

		accounts.createStaffAccount("agent3", UnencryptedPassword.of("agent3"), "Heike", "Hirsch",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP2, RoleType.ROLE_HD_CASE_AGENT);
		
		
		
		// accounts for security tests
		accounts.createStaffAccount("secGama1", UnencryptedPassword.of("secur1tyTest!"), "Maja", "Menzel",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, RoleType.ROLE_HD_CASE_AGENT);
		
		accounts.createStaffAccount("secGama2", UnencryptedPassword.of("secur1tyTest!"), "Toni", "Tüpper",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, RoleType.ROLE_HD_CASE_AGENT);
		
		accounts.createStaffAccount("secGama3", UnencryptedPassword.of("secur1tyTest!"), "Lars", "Lüppel",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, RoleType.ROLE_HD_CASE_AGENT);
		
		accounts.createStaffAccount("secGama4", UnencryptedPassword.of("secur1tyTest!"), "Bernd", "Böttcher",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, RoleType.ROLE_HD_CASE_AGENT);
		
		accounts.createStaffAccount("secAdmin1", UnencryptedPassword.of("secur1tyTest!"), "Alfons", "Adminus",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, RoleType.ROLE_HD_ADMIN);
		
		accounts.createStaffAccount("secAdmin2", UnencryptedPassword.of("secur1tyTest!"), "Ariana", "Admina",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, RoleType.ROLE_HD_ADMIN);
		
		accounts.createStaffAccount("secAdmin3", UnencryptedPassword.of("secur1tyTest!"), "Alber", "Admino",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, RoleType.ROLE_HD_ADMIN);
		
		accounts.createStaffAccount("secAdmin4", UnencryptedPassword.of("secur1tyTest!"), "Agatha", "Adminiki",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, RoleType.ROLE_HD_ADMIN);
		
		
		accounts.createTrackedPersonAccount("secUser1", UnencryptedPassword.of("secur1tyTest!"), "Siggi", "Seufert",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_SEC1_ID_DEP1);
		
		accounts.createTrackedPersonAccount("secUser2", UnencryptedPassword.of("secur1tyTest!"), "Sarah", "Siebel",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_SEC2_ID_DEP1);
		
		accounts.createTrackedPersonAccount("secUser3", UnencryptedPassword.of("secur1tyTest!"), "Sonja", "Straßmann",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_SEC3_ID_DEP1);
		
		accounts.createTrackedPersonAccount("secUser4", UnencryptedPassword.of("secur1tyTest!"), "Samuel", "Soller",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_SEC4_ID_DEP1);
	}
}
