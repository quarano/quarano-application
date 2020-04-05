package de.wevsvirushackathon.coronareport.authentication.provider;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import de.wevsvirushackathon.coronareport.authentication.Account;
import de.wevsvirushackathon.coronareport.authentication.Role;
import de.wevsvirushackathon.coronareport.authentication.RoleType;
import de.wevsvirushackathon.coronareport.authentication.RoleRepository;

@Component
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
    	// initialize existing roles
    	roleRepository.deleteAll();
    	Role userRole = new Role(RoleType.ROLE_USER);
    	userRole = roleRepository.save(userRole);
    	
    	// create dummy acccounts
        accountRepository.deleteAll();
        Account account = new Account();
        account.setFirstname("Demo");
        account.setLastname("Account");
        account.setUsername("DemoAccount");
        account.setPassword(passwordEncoder.encode("DemoPassword"));
        account.getRoles().add(userRole);

        accountRepository.save(account);
    }
}
