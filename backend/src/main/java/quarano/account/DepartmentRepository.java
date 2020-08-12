package quarano.account;

import quarano.account.Department.DepartmentIdentifier;
import quarano.core.QuaranoRepository;

import java.util.Optional;

/**
 * @author Oliver Drotbohm
 */
public interface DepartmentRepository extends QuaranoRepository<Department, DepartmentIdentifier> {

	Optional<Department> findByName(String name);
}
