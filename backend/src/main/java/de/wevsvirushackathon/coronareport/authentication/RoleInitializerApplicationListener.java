package de.wevsvirushackathon.coronareport.authentication;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Create fix role setup on startup based on role enum {@link RoleType}
 * @author Patrick Otto
 *
 */
@Component
class RoleInitializerApplicationListener implements ApplicationListener<ApplicationReadyEvent> {


    private RoleRepository roleRepository;
    
    

    public RoleInitializerApplicationListener(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     *    adds each role defined in enum {@link RoleType} that is not existing in the database 
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    	
    	for(RoleType type: RoleType.values()) {
    		Role role = roleRepository.findByName(type.getCode());
    		if(role == null) {
            	Role userRole = new Role(type);
            	userRole = roleRepository.save(userRole);
    		}
    	}
 
    }
}
