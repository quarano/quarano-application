package quarano.auth;

import org.springframework.data.repository.CrudRepository;

interface RoleRepository extends CrudRepository<Role, Integer> {

	Role findByName(String roleName);
}
