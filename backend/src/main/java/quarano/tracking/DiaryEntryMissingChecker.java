package quarano.tracking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This class collects the persons with missing diary entries and publishes Events of them combining the slots
 * with missing diary entries
 *
 * @author Felix Schultze
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class DiaryEntryMissingChecker {
	private final DiaryEntryRepository diaries;
	private final ApplicationEventPublisher publisher;
	private final DiaryProperties properties;

	@Scheduled(cron = "0 5 12,23 * * *")
	void checkPeriodically() {
		collectAndPublishMissingEntryPersons();
	}

	/**
	 *
	 */
	void collectAndPublishMissingEntryPersons() {
		var now = Slot.now();
		var slots = new ArrayList<Slot>();
		slots.add(now);

		// calculate slots to check for missing diary entries
		for (int tolerance = 0; tolerance < properties.getToleratedSlotCount(); tolerance++) {
			var currentSlot = slots.get(slots.size() - 1);
			slots.add(currentSlot.previous());
		}

		log.debug("Check missing diary entries for slots {}", slots);
		diaries.findMissingDiaryEntryPersons(slots)
				.map(person -> DiaryEntryMissing.of(slots, person))
				.forEach(publisher::publishEvent);
	}

}
