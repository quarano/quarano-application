package quarano.event;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EventRepository extends CrudRepository<Event, Event.EventIdentifier> {
	@Query("select count(1) = 0 from Event e where e.eventCode = :eventCode")
	boolean isEventCodeAvailable(EventCode eventCode);

	@Query("select e from Event e where e.eventCode = :eventCode")
	Optional<Event> findByEventCode(EventCode eventCode);
}
