package quarano.tracking;

import quarano.core.QuaranoRepository;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

/**
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
@Repository("newContactRepository")
public interface ContactPersonRepository extends QuaranoRepository<ContactPerson, ContactPersonIdentifier> {

	Streamable<ContactPerson> findByOwnerId(TrackedPersonIdentifier ownerId);

	/**
	 * Returns the {@link ContactPerson}s which are not yet anonymized and created before the given {@link LocalDateTime}.
	 * 
	 * @param refDate must not be {@literal null}.
	 * @return
	 * @since 1.4
	 */
	@Query("select c from ContactPerson c where c.anonymized = FALSE and c.metadata.created < :refDate")
	Streamable<ContactPerson> findByMetadataCreatedIsBefore(LocalDateTime refDate);
}
