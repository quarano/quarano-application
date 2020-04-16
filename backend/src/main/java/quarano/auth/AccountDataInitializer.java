package quarano.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.DataInitializer;
import quarano.department.DepartmentDataInitializer;
import quarano.tracking.TrackedPersonDataInitializer;

import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
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

	private final AccountRepository accountRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		log.warn("Test data: creating 7 accounts");

		Role userRole = roleRepository.findByName("ROLE_USER");
		Role adminRole = roleRepository.findByName("ROLE_HD_ADMIN");
		Role caseRole = roleRepository.findByName("ROLE_HD_CASE_AGENT");

		// person 1 should not have an account yet

		// account for person 2
		Account accountPerson2 = new Account("DemoAccount", passwordEncoder.encode("DemoPassword"), "Markus", "Hanser",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_PERSON2_ID_DEP1,
				userRole);
		accountRepository.save(accountPerson2);

		// account for person 3
		Account accountPerson3 = new Account("test3", passwordEncoder.encode("test123"), "Sandra", "Schubert",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP2, TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2,
				userRole);
		accountRepository.save(accountPerson3);

		// account for GA user
		Account accountHD1 = new Account("admin", passwordEncoder.encode("admin"), "Mark", "Muster",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, null, adminRole);
		accountRepository.save(accountHD1);

		Account accountHD2 = new Account("agent1", passwordEncoder.encode("agent1"), "Horst", "Hallig",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, null, caseRole);
		accountRepository.save(accountHD2);

		Account accountHD3 = new Account("agent2", passwordEncoder.encode("agent2"), "Bettina", "Boot",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, null, caseRole);
		accountRepository.save(accountHD3);

		Account accountHD4 = new Account("agent3", passwordEncoder.encode("agent3"), "Heike", "Hirsch",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP2, null, caseRole);
		accountRepository.save(accountHD4);
	}
}
