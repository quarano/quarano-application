package quarano.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

interface RoleRepository extends CrudRepository<Role, Integer> {

	@Nullable
	Role findByName(String roleName);
}
