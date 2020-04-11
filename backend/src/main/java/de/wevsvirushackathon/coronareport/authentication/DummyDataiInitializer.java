package de.wevsvirushackathon.coronareport.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.wevsvirushackathon.coronareport.client.Client;
import de.wevsvirushackathon.coronareport.client.ClientRepository;

/**
 * Create some dummy accounts for test and development
 * 
 * @author Patrick Otto
 *
 */
@Component
@Order(500)
class DummyDataiInitializer implements ApplicationListener<ApplicationReadyEvent> {

	private AccountRepository accountRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private ClientRepository clientRepository;

	private final Log logger = LogFactory.getLog(DummyDataiInitializer.class);

	public DummyDataiInitializer(AccountRepository accountRepository, PasswordEncoder passwordEncoder,
			RoleRepository roleRepository, ClientRepository clientRepository) {
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.clientRepository = clientRepository;
	}

	@Override
	@Transactional
	public void onApplicationEvent(ApplicationReadyEvent event) {

		logger.warn("Adding dummy 4 accounts to database");

		Client client = clientRepository.findByClientCode("738d3d1f-a9f1-4619-9896-2b5cb3a89c22");

		String hdId = client.getHealthDepartment().getId();

		Role userRole = roleRepository.findByName("ROLE_USER");
		Role adminRole = roleRepository.findByName("ROLE_HD_ADMIN");
		Role caseRole = roleRepository.findByName("ROLE_HD_CASE_AGENT");

		// create dummy acccounts
		accountRepository.deleteAll();
		Account user = new Account("DemoAccount", "DemoPassword", "Max", "Mustermann", hdId, client.getClientId(),
				RoleType.valueOf(userRole.toString()));
		accountRepository.save(user);

		// create 2nd dummy acccount without client
		Account user2 = new Account("hansmueller", "hansmueller", "Hans", "Müller", hdId, client.getClientId(),
				RoleType.valueOf(userRole.toString()));
		accountRepository.save(user2);

		Account admin = new Account("admin", "admin", "Tanita", "Beyer", hdId, client.getClientId(),
				RoleType.valueOf(adminRole.toString()));
		accountRepository.save(admin);

		Account caseAgent = new Account("agent1", "agent1", "Hans", "Baum", hdId, client.getClientId(),
				RoleType.valueOf(caseRole.toString()));
		accountRepository.save(caseAgent);

		Account caseAgent2 = new Account("agent2", "agent2", "Berta", "Strauch", hdId, client.getClientId(),
				RoleType.valueOf(caseRole.toString()));
		accountRepository.save(caseAgent2);
	}
}
