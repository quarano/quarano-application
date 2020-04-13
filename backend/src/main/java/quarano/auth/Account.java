package quarano.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.jddd.core.types.Identifier;
import org.springframework.lang.Nullable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import quarano.auth.Account.AccountIdentifier;
import quarano.core.QuaranoAggregate;
import quarano.department.Department.DepartmentIdentifier;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

/**
 * An account of a user. Can be connected to a {@link TrackedPerson} by the {@link TrackedPersonIdentifier} or can be a HD employee account
 * @author Patrick Otto
 *	
 */
@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Account extends QuaranoAggregate<Account, AccountIdentifier>{
	
    @Getter private final String username;
    @Getter private final String password;

    @Getter private String firstname;
    @Getter private String lastname;
    
    @Getter private DepartmentIdentifier departmentId;
    
    // will be null if account belongs to a health department employee
    @Getter private @Nullable TrackedPersonIdentifier trackedPersonId;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @Getter private List<Role> roles = new ArrayList<>();
    
    
    Account(String username, String encryptedPassword, String firstname, String lastname, DepartmentIdentifier departmentId, TrackedPersonIdentifier trackedPersonId, RoleType roletype){
    	super();
    	
    	this.id = AccountIdentifier.of(UUID.randomUUID());
    	this.username = username.trim();
    	this.firstname = firstname;
    	this.lastname = lastname;
    	this.departmentId = departmentId;
    	this.trackedPersonId = trackedPersonId;
    	this.password = encryptedPassword;
    	
    	this.addRole(roletype);
    }
    
    // for testing purposes
     Account(String username, String encryptedPassword, String firstname, String lastname, DepartmentIdentifier departmentId, TrackedPersonIdentifier trackedPersonId, RoleType roletype, UUID fixedUID){
    	super();
    	
    	this.id = AccountIdentifier.of(fixedUID);
    	this.username = username.trim();
    	this.firstname = firstname;
    	this.lastname = lastname;
    	this.departmentId = departmentId;
    	this.trackedPersonId = trackedPersonId;
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
    
	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class AccountIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = 7871473225101042167L;
		final UUID accountId;
	}

	public boolean isTrackedPerson() {
		
		return this.roles.contains(new Role(RoleType.ROLE_USER));
		
	}

}
