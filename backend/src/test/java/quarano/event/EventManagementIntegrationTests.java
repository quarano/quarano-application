package quarano.event;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import quarano.QuaranoWebIntegrationTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@QuaranoWebIntegrationTest
@RequiredArgsConstructor
class EventManagementIntegrationTests {
	private final EventManagement eventManagement;

	@Test
	void testCreateEvent() {
		Event event = eventManagement.createEvent("TestEvent", LocalDate.now().atStartOfDay(),
				LocalDate.now().plusDays(1).atStartOfDay());

		assertThat(event.getEventCode()).isNotNull();
	}

	@Test
	void testCreateEventAndAddGroup() {
		Event event = eventManagement.createEvent("TestEvent 2", LocalDate.now().atStartOfDay(),
				LocalDate.now().plusDays(1).atStartOfDay());

		assertThat(event.getEventCode()).isNotNull();

		var visitorGroup = new VisitorGroup(LocalDate.now().atStartOfDay(), event.getEventCode())
				.setComment("Comment")
				.setEnd(LocalDate.now().plusDays(1).atStartOfDay())
				.setLocationName("Event Location ftw")
				.setVisitorList(new ArrayList<>());

		Optional<Event> eventOptional = eventManagement.registerVisitorListForEvent(event.getEventCode(), visitorGroup);
		assertThat(eventOptional.isPresent()).isTrue();
		assertThat(eventOptional.get().getVisitorGroups()).hasSize(1);
	}
}
