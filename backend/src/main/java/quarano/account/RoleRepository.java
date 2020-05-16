package quarano.account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

public interface RoleRepository extends CrudRepository<Role, Integer> {

	@Nullable
	Role findByName(String roleName);

	@Nullable
	default Role findByRoleType(RoleType roleType) {
		return this.findByName(roleType.getCode());
	}
}
