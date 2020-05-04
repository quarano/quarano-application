package quarano.auth;

import quarano.auth.Account.AccountIdentifier;
import quarano.department.Department.DepartmentIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

interface AccountRepository extends CrudRepository<Account, AccountIdentifier> {

	Optional<Account> findByUsername(String userName);

	Optional<Account> findByTrackedPersonId(TrackedPersonIdentifier identifier);

	@Query("SELECT a FROM Account a WHERE a.departmentId = :departmentId and a.trackedPersonId is null ORDER BY a.lastname ASC" )
	List<Account> findStaffAccountsOfDepartmentOrderedByLastNameASC(DepartmentIdentifier departmentId);
}
