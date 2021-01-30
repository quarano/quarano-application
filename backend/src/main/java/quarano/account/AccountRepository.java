package quarano.account;

import quarano.account.Account.AccountIdentifier;
import quarano.account.Department.DepartmentIdentifier;
import quarano.core.EmailAddress;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

interface AccountRepository extends CrudRepository<Account, AccountIdentifier> {

	static final String DEFAULT_SELECT = "select distinct a from Account a left join fetch a.roles ";

	/**
	 * Returns the account with the given username.
	 *
	 * @param userName must not be {@literal null}.
	 * @return
	 */
	@Query(DEFAULT_SELECT + "where a.username = :userName")
	Optional<Account> findByUsername(String userName);

	/**
	 * Returns whether the given username is still available.
	 *
	 * @param username must not be {@literal null} or empty.
	 * @return
	 * @since 1.4
	 */
	@Query("select count(1) = 0 from Account a where a.username = :username")
	boolean isUsernameAvailable(String username);

	/**
	 * Returns the Account with the given {@link EmailAddress}.
	 *
	 * @param emailAddress must not be {@literal null}.
	 * @return
	 * @since 1.4
	 */
	@Query(DEFAULT_SELECT + "where a.email = :emailAddress")
	Stream<Account> findByEmailAddress(EmailAddress emailAddress);

	/**
	 * Returns all accounts registered for the {@link Department} with the given {@link DepartmentIdentifier}.
	 *
	 * @param identifier must not be {@literal null}.
	 * @return
	 */
	@Query(DEFAULT_SELECT + "where a.departmentId = :identifier ORDER BY a.lastname ASC")
	Stream<Account> findAccountsFor(DepartmentIdentifier identifier);

	/**
	 * Returns the Account for which the given password reset token was registered.
	 *
	 * @param token
	 * @return
	 * @since 1.4
	 */
	@Query(DEFAULT_SELECT + "where a.passwordResetToken.token = :token")
	Optional<Account> findByResetToken(UUID token);
}
