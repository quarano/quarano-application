package quarano.actions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.actions.ActionItem.ItemType;
import quarano.tracking.DiaryEntry.DiaryEntryAdded;
import quarano.tracking.DiaryEntryMissing;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DiaryEventListener {

	private final AnomaliesProperties config;
	private final ActionItemRepository items;

	@EventListener
	void onDiaryEntryAddedToResolveMissingItems(DiaryEntryAdded event) {

		var entry = event.getEntry();
		var person = entry.getTrackedPersonId();
		var slot = entry.getSlot();

		// resolve missing entry item
		items.findDiaryEntryMissingActionItemsFor(person, slot) //
				.resolve(items::save);
	}

	@EventListener
	void onDiaryEntryAddedForBodyTemperature(DiaryEntryAdded event) {

		var entry = event.getEntry();
		var person = entry.getTrackedPersonId();

		// Body temperature exceeds reference
		if (entry.getBodyTemperature().exceeds(config.getTemperatureThreshold())) {

			Description description = Description.of(DescriptionCode.INCREASED_TEMPERATURE, //
					entry.getBodyTemperature(), //
					config.getTemperatureThreshold());

			items.save(new DiaryEntryActionItem(person, entry, ItemType.MEDICAL_INCIDENT, description));
		} else {
			items.findByDescriptionCode(person, DescriptionCode.INCREASED_TEMPERATURE) //
					.resolve(items::save);
		}
	}

	@EventListener
	void onDiaryEntryAddedForCharacteristicSymptoms(DiaryEntryAdded event) {

		var entry = event.getEntry();
		var person = entry.getTrackedPersonId();

		// First characteristic symptom reported

		if (entry.getSymptoms().hasCharacteristicSymptom() && //
				items.findByDescriptionCode(person, DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM).isEmpty()) {

			items.save(new DiaryEntryActionItem(person, entry, ItemType.MEDICAL_INCIDENT,
					Description.of(DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM)));
		}
	}

	@EventListener
	void on(DiaryEntryMissing diaryEntryMissing) {

		log.debug("Caught diaryEntryMissing Event {}. Save it as DiaryEntryMissingActionItem", diaryEntryMissing);

		diaryEntryMissing.getMissingSlots().stream()
				.map(slot -> new DiaryEntryMissingActionItem(diaryEntryMissing.getTrackedPersonIdentifier(), slot))
				.filter(item -> items.findDiaryEntryMissingActionItemsFor(item.getPersonIdentifier(), item.getSlot()).isEmpty())
				.forEach(items::save);
	}
}
