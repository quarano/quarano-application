package de.wevsvirushackathon.coronareport.authentication.provider;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import de.wevsvirushackathon.coronareport.authentication.Account;
import de.wevsvirushackathon.coronareport.authentication.Role;
import de.wevsvirushackathon.coronareport.authentication.RoleRepository;

/**
 * Create some dummy accounts for test and development
 * @author Patrick Otto
 *
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
class DummyDataiInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private AccountRepository accountRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public DummyDataiInitializer(AccountRepository accountRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

    	Role userRole = roleRepository.findByName("ROLE_HD_ADMIN");
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
        accountRepository.save(user);
        
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
