package quarano.tracking;

import quarano.account.Account;
import quarano.core.EmailAddress;
import quarano.core.QuaranoRepository;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.Optional;

/**
 * @author Oliver Drotbohm
 */
public interface TrackedPersonRepository extends QuaranoRepository<TrackedPerson, TrackedPersonIdentifier> {

	TrackedPerson findRequiredById(TrackedPersonIdentifier id);

	Optional<TrackedPerson> findByEmailAddress(EmailAddress emailAddress);

	Optional<TrackedPerson> findByAccount(Account account);
}
