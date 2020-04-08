package de.wevsvirushackathon.coronareport.authentication.provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import de.wevsvirushackathon.coronareport.authentication.Account;
import de.wevsvirushackathon.coronareport.authentication.Role;
import de.wevsvirushackathon.coronareport.authentication.RoleRepository;
import de.wevsvirushackathon.coronareport.client.Client;
import de.wevsvirushackathon.coronareport.client.ClientRepository;

/**
 * Create some dummy accounts for test and development
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

    public DummyDataiInitializer(AccountRepository accountRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    	
    	logger.warn("Adding dummy 4 accounts to database");
    	
    	Client client = clientRepository.findByClientCode("738d3d1f-a9f1-4619-9896-2b5cb3a89c22");

    	Role userRole = roleRepository.findByName("ROLE_USER");
    	Role adminRole = roleRepository.findByName("ROLE_HD_ADMIN");
    	Role caseRole = roleRepository.findByName("ROLE_HD_CASE_AGENT");
    	
    	// create dummy acccounts
        accountRepository.deleteAll();
        Account user = new Account();
        user.setFirstname("Demo");
        user.setLastname("Account");
        user.setUsername("DemoAccount");
        user.setPassword(passwordEncoder.encode("DemoPassword"));
        user.getRoles().add(userRole);
        user.setClientId(client.getClientId());
        accountRepository.save(user);
        
    	// create 2nd dummy acccount without client
        Account user2 = new Account();
        user2.setFirstname("Hans");
        user2.setLastname("Müller");
        user2.setUsername("hansmueller");
        user2.setPassword(passwordEncoder.encode("hansmueller"));
        user2.getRoles().add(userRole);
        accountRepository.save(user2);        
        
        Account admin = new Account();
        admin.setFirstname("Tanita");
        admin.setLastname("Beyer");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.getRoles().add(adminRole);        
        accountRepository.save(admin);
        
        Account caseAgent = new Account();
        caseAgent.setFirstname("Hans");
        caseAgent.setLastname("Baum");
        caseAgent.setUsername("agent1");
        caseAgent.setPassword(passwordEncoder.encode("agent1"));
        caseAgent.getRoles().add(caseRole);        
        accountRepository.save(caseAgent);
        
        Account caseAgent2 = new Account();
        caseAgent2.setFirstname("Berta");
        caseAgent2.setLastname("Strauch");
        caseAgent2.setUsername("agent2");
        caseAgent2.setPassword(passwordEncoder.encode("agent2"));
        caseAgent2.getRoles().add(caseRole);        
        accountRepository.save(caseAgent2);        
    }
}
