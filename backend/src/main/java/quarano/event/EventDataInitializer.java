package quarano.event;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import quarano.core.DataInitializer;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class EventDataInitializer implements DataInitializer {

	public final static EventCode EVENT_CODE_1 = EventCode.of("EVENTNR1");

	private final @NonNull EventRepository eventRepository;

	@Override
	public void initialize() {
		eventRepository.save(createEvent());
	}

	public static Event createEvent() {
		return new Event("Event Nr 1", LocalDate.now().atStartOfDay(),
				LocalDate.now().plusDays(1).atStartOfDay(), EVENT_CODE_1);
	}
}
