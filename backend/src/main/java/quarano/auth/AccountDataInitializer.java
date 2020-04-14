package quarano.auth;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.department.DepartmentDataInitializer;
import quarano.tracking.TrackedPersonDataInitializer;

/**
 * Create some dummy accounts for test and development
 * 
 * @author Patrick Otto
 *
 */
@Component
@Order(500)
@RequiredArgsConstructor
@Slf4j
class AccountDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

	private final AccountRepository accountRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void onApplicationEvent(ApplicationReadyEvent event) {

		log.warn("Test data: creating 7 accounts");

		Role userRole = roleRepository.findByName("ROLE_USER");
		Role adminRole = roleRepository.findByName("ROLE_HD_ADMIN");
		Role caseRole = roleRepository.findByName("ROLE_HD_CASE_AGENT");

		// create dummy acccounts
		accountRepository.deleteAll();

		
		// person 1 should not have an account yet
		
		// account for person 2
		Account accountPerson2 = new Account("DemoAccount", passwordEncoder.encode("DemoPassword"), "Markus", "Hanser",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				TrackedPersonDataInitializer.INDEX_PERSON2_IN_ENROLLMENT.getId(),
				RoleType.valueOf(userRole.toString()));
		accountRepository.save(accountPerson2);
		
		// account for person 3
		Account accountPerson3 = new Account("test3", passwordEncoder.encode("test123"), "Sandra", "Schubert",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP2,
				TrackedPersonDataInitializer.INDEX_PERSON3_WITH_ACTIVE_TRACKING.getId(),
				RoleType.valueOf(userRole.toString()));
		accountRepository.save(accountPerson3);
		
		
		// account for GA user
		Account accountHD1 = new Account("admin", passwordEncoder.encode("admin"), "Mark", "Muster",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				null,
				RoleType.valueOf(adminRole.toString()));
		accountRepository.save(accountHD1);
		
		Account accountHD2 = new Account("agent1", passwordEncoder.encode("agent1"), "Horst", "Hallig",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				null,
				RoleType.valueOf(caseRole.toString()));
		accountRepository.save(accountHD2);
		
		
		Account accountHD3 = new Account("agent2", passwordEncoder.encode("agent2"), "Bettina", "Boot",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
				null,
				RoleType.valueOf(caseRole.toString()));
		accountRepository.save(accountHD3);
		
		Account accountHD4 = new Account("agent3", passwordEncoder.encode("agent3"), "Heike", "Hirsch",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP2,
				null,
				RoleType.valueOf(caseRole.toString()));
		accountRepository.save(accountHD4);
		

	}
}
