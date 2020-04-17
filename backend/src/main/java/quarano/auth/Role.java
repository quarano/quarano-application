package quarano.auth;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.NoArgsConstructor;


/**
 * A masterdata-entity, which can be assigned to an account.
 * @author Patrick Otto
 *
 */
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
	
	public Role(RoleType name, int id){
		this.name = name.getCode();
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
}
