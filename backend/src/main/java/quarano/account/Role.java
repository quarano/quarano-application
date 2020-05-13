package quarano.account;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A masterdata-entity, which can be assigned to an account.
 *
 * @author Patrick Otto
 * @author Michael J. Simons
 */
@Entity
@Table(name = "roles")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Role {

	@Id //
	@Column(name = "role_id") //
	private UUID id;

	@Column(name = "role_name") //
	private String name;

	public Role(RoleType name) {

		this.id = UUID.randomUUID();
		this.name = name.getCode();
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static Role of(RoleType type) {
		return new Role(type);
	}

	public RoleType getRoleType() {
		return RoleType.valueOf(this.name);
	}

}
