package quarano.account;

import quarano.account.Account.AccountIdentifier;
import quarano.account.Department.DepartmentIdentifier;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

interface AccountRepository extends CrudRepository<Account, AccountIdentifier> {

	static final String DEFAULT_SELECT = "select distinct a from Account a left join fetch a.roles ";

	@Query(DEFAULT_SELECT + "where a.username = :userName")
	Optional<Account> findByUsername(String userName);

	@Query(DEFAULT_SELECT + "where a.departmentId = :departmentId ORDER BY a.lastname ASC")
	Stream<Account> findAccountsFor(DepartmentIdentifier departmentId);
}
