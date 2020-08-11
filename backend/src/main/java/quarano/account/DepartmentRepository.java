package quarano.account;

import quarano.account.Department.DepartmentIdentifier;
import quarano.core.QuaranoRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

/**
 * @author Oliver Drotbohm
 */
public interface DepartmentRepository extends QuaranoRepository<Department, DepartmentIdentifier> {

	Optional<Department> findByName(String name);

	@Query("select d from Department d join fetch d.contacts where d.id = :identifier")
	Optional<Department> findDepartmentAndContactsById(DepartmentIdentifier identifier);
}
