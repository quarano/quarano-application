package quarano.account;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.util.Assert;

public interface RoleRepository extends CrudRepository<Role, Integer> {

	Optional<Role> findByName(String roleName);

	default Role findByType(RoleType roleType) {

		Assert.notNull(roleType, "Role type must not be null!");

		return findByName(roleType.getCode())
				.orElseThrow(() -> new IllegalStateException("Did not find a role instance for type " + roleType + "!"));
	}
}
