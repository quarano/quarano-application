package quarano.account;


import quarano.account.Account.AccountIdentifier;
import quarano.account.Department.DepartmentIdentifier;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

interface AccountRepository extends CrudRepository<Account, AccountIdentifier> {

	Optional<Account> findByUsername(String userName);

	@Query("SELECT a FROM Account a WHERE a.departmentId = :departmentId ORDER BY a.lastname ASC")
	Stream<Account> findAccountsFor(DepartmentIdentifier departmentId);
}
