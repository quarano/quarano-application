package de.wevsvirushackathon.coronareport.authentication;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import de.wevsvirushackathon.coronareport.client.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * An account of a user. Can be connected to a {@link Client} or can be a HD employee account
 * @author Patrick Otto
 *
 */
@Entity
@NoArgsConstructor
public class Account {
	
    @Id
    @GeneratedValue
    @Getter private Long id;

    @Getter private String username;
    @Getter private String password;

    @Getter private String firstname;
    @Getter private String lastname;
    
    @Getter private String hdId;
    
    
    // will be null if account belongs to a health department employee
    @Getter private Long clientId;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @Getter private List<Role> roles = new ArrayList<>();
    
    
    Account(String username, String encryptedPassword, String firstname, String lastname, String departmentId, Long clientId, RoleType roletype){
    	super();
    	
    	this.username = username.trim();
    	this.firstname = firstname;
    	this.lastname = lastname;
    	this.hdId = departmentId;
    	this.clientId = clientId;
    	this.password = encryptedPassword;
    	
    	this.addRole(roletype);
    }
    
    
    /**
     * Adds a role to the roles list of the user if is not already included
     * @param roletype
     */
    public void addRole(RoleType roletype) {
    	Role newRole = new Role(roletype);
    	if(!roles.contains(newRole)) {
    		this.roles.add(newRole);
    	}
    }
    
    
    
    

}
