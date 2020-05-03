package quarano.actions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.actions.ActionItem.ItemType;
import quarano.tracking.DiaryEntry;
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
	void on(DiaryEntry.DiaryEntryAdded event) {

		var entry = event.getEntry();
		var person = entry.getTrackedPersonId();
		var slot = entry.getSlot();

		items.findDiaryEntryMissingActionItemsFor(person, slot)
				.map(ActionItem::resolve)
				.forEach(items::save);

		// Body temperature exceeds reference

		if (entry.getBodyTemperature().exceeds(config.getTemperatureThreshold())) {

			Description description = Description.of(DescriptionCode.INCREASED_TEMPERATURE, //
					entry.getBodyTemperature(), //
					config.getTemperatureThreshold());

			items.save(new DiaryEntryActionItem(person, entry, ItemType.MEDICAL_INCIDENT, description));
		}

		// First characteristic symptom reported

		if (entry.getSymptoms().hasCharacteristicSymptom() && //
				items.findByDescriptionCode(person, DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM).isEmpty()) {

			items.save(new DiaryEntryActionItem(person, entry, ItemType.MEDICAL_INCIDENT,
					Description.of(DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM)));
		}
	}

	@EventListener
	public void on(DiaryEntryMissing diaryEntryMissing) {
		log.debug("Caught diaryEntryMissing Event {}. Save it as DiaryEntryMissingActionItem", diaryEntryMissing);

		diaryEntryMissing.getMissingSlots().stream()
			.map(slot -> new DiaryEntryMissingActionItem(diaryEntryMissing.getTrackedPersonIdentifier(), slot))
			.filter(item -> items.countDiaryEntryMissingActionItemsFor(item.getPersonIdentifier(), item.getSlot()) == 0)
			.forEach(items::save);
	}
}
