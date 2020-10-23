package quarano.tracking;

import quarano.account.Account;
import quarano.core.EmailAddress;
import quarano.core.QuaranoRepository;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.Locale;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

/**
 * @author Oliver Drotbohm
 */
public interface TrackedPersonRepository extends QuaranoRepository<TrackedPerson, TrackedPersonIdentifier> {

	TrackedPerson findRequiredById(TrackedPersonIdentifier id);

	Optional<TrackedPerson> findByEmailAddress(EmailAddress emailAddress);

	Optional<TrackedPerson> findByAccount(Account account);

	/**
	 * Returns the preferred {@link Locale} for the {@link TrackedPerson} with the given {@link Account}.
	 *
	 * @param account must not be {@literal null}.
	 * @return
	 */
	@Query("select p.locale from TrackedPerson p where p.account = :account")
	Optional<Locale> findLocaleByAccount(Account account);
}
