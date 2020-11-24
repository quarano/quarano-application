package quarano.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventManagement {

	private final EventRepository eventRepository;

	public Event createEvent(String title, LocalDateTime start, LocalDateTime end) {
		return eventRepository.save(new Event(title, start, end, findValidEventCode()));
	}

	private EventCode findValidEventCode() {
		EventCode eventCode = EventCode.generate();
		return eventRepository.isEventCodeAvailable(eventCode)
				? eventCode
				: findValidEventCode();
	}

	public Optional<Event> registerVisitorListForEvent(EventCode eventCode, VisitorGroup visitorGroup) {
		return eventRepository.findByEventCode(eventCode)
				.map(e -> e.registerVisitorGroup(visitorGroup))
				.map(eventRepository::save);
	}
}
