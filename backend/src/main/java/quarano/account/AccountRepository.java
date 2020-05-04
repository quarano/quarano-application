package quarano.account;

import quarano.account.Account.AccountIdentifier;
import quarano.account.Department.DepartmentIdentifier;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

interface AccountRepository extends CrudRepository<Account, AccountIdentifier> {

	Optional<Account> findByUsername(String userName);

	@Query("SELECT a FROM Account a, Role r WHERE a.departmentId = :departmentId and r in a.roles and r.name like 'ROLE_HD%' ORDER BY a.lastname ASC")
	List<Account> findStaffAccountsFor(DepartmentIdentifier departmentId);
}
