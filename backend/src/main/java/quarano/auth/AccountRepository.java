package quarano.auth;

import quarano.auth.Account.AccountIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

interface AccountRepository extends CrudRepository<Account, AccountIdentifier> {

	Optional<Account> findByUsername(String userName);

	Optional<Account> findByTrackedPersonId(TrackedPersonIdentifier identifier);
}
