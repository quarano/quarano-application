package quarano.tracking;

import quarano.core.QuaranoRepository;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

/**
 * @author Oliver Drotbohm
 */
@Repository("newContactRepository")
public interface ContactPersonRepository extends QuaranoRepository<ContactPerson, ContactPersonIdentifier> {

	Streamable<ContactPerson> findByOwnerId(TrackedPersonIdentifier ownerId);
}
