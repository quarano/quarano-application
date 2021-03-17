package quarano.tracking;

import quarano.core.QuaranoRepository;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

/**
 * @author David Bauknecht
 */
@Repository("locationRepository")
public interface LocationRepository extends QuaranoRepository<Location, Location.LocationIdentifier> {

	Streamable<Location> findByOwnerId(TrackedPerson.TrackedPersonIdentifier ownerId);
}
