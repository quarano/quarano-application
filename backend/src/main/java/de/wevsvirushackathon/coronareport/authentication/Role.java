package de.wevsvirushackathon.coronareport.authentication;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Role {
	
    @Id
    @GeneratedValue
	private int id;
	private String name;
	
	public Role(RoleType name){
		this.name = name.getCode();
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	
}
