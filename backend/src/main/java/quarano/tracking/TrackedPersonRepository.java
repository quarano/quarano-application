package quarano.tracking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import quarano.account.Account;
import quarano.core.EmailAddress;
import quarano.core.QuaranoRepository;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

/**
 * @author Oliver Drotbohm
 */
public interface TrackedPersonRepository extends QuaranoRepository<TrackedPerson, TrackedPersonIdentifier> {

	TrackedPerson findRequiredById(TrackedPersonIdentifier id);

	Optional<TrackedPerson> findByEmailAddress(EmailAddress emailAddress);

	Optional<TrackedPerson> findByAccount(Account account);

	@Query("select p from TrackedPerson p where p.metadata.lastModified <= :limit")
	List<TrackedPerson> findObsoletePersons(LocalDateTime limit, Pageable pageable);

	@Query("select count(p) from TrackedPerson p where p.metadata.lastModified <= :limit")
	long countObsoletePersons(LocalDateTime limit);

	@Query("select e from Encounter e")
    List<Encounter> getAllEncounters();
}
