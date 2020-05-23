package quarano.account;

import quarano.account.Department.DepartmentIdentifier;
import quarano.core.QuaranoRepository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Oliver Drotbohm
 */
public interface DepartmentRepository extends QuaranoRepository<Department, DepartmentIdentifier> {

	Optional<Department> findByName(String name);

	@Modifying
	@Query("delete from DepartmentContact departmentContact where departmentContact in :departmentContacts")
	void deleteDepartmentContacts(@Param("departmentContacts") Collection<DepartmentContact> departmentContacts);
}
