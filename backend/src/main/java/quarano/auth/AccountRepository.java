package quarano.auth;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import quarano.auth.Account.AccountIdentifier;

public interface AccountRepository extends CrudRepository<Account, AccountIdentifier> {

    Optional<Account> findOneByUsername(String username);
}
