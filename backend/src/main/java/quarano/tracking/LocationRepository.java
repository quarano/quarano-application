package quarano.tracking;

import org.springframework.stereotype.Repository;
import quarano.core.QuaranoRepository;

@Repository("locationRepository")
public interface LocationRepository extends QuaranoRepository<Location, Location.LocationId> {
}
