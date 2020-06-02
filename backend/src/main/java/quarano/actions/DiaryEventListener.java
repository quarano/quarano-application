package quarano.actions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.actions.ActionItem.ItemType;
import quarano.diary.DiaryEntry;
import quarano.diary.DiaryEntry.DiaryEntryAdded;
import quarano.diary.DiaryEntry.DiaryEntryUpdated;
import quarano.diary.DiaryEntryMissing;
import quarano.diary.DiaryManagement;
import quarano.diary.Slot;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
class DiaryEventListener {

	private final @NonNull AnomaliesProperties config;
	private final @NonNull ActionItemRepository items;
	private final @NonNull DiaryManagement diaryManagement;

	@EventListener
	void on(DiaryEntryAdded event) {

		var entry = event.getEntry();

		handleDiaryEntryForBodyTemprature(entry);
		handleDiaryEntryForCharacteristicSymptoms(entry);
		resolveMissingItemsActionItem(entry);
	}

	@EventListener
	void on(DiaryEntryUpdated event) {

		var entry = event.getEntry();

		handleDiaryEntryForBodyTemprature(entry);
		handleDiaryEntryForCharacteristicSymptoms(entry);
	}

	void resolveMissingItemsActionItem(DiaryEntry entry) {

		var person = entry.getTrackedPersonId();
		var slot = entry.getSlot();

		// resolve missing entry item
		items.findDiaryEntryMissingActionItemsFor(person, slot) //
				.resolveAutomatically(items::save);
	}

	/**
	 * Only does something if the entry with the most current slot is edited. First resolves all existing action items for
	 * temperature. If handles an added or updated diary entry with to high temperature a new action item is added. There
	 * is no update of existing items.
	 *
	 * @param entry
	 */
	void handleDiaryEntryForBodyTemprature(DiaryEntry entry) {

		var person = entry.getTrackedPersonId();

		if (!determineWhetherEntryMostRecent(person, entry.getSlot())) {
			return;
		}

		items.findUnresolvedByDescriptionCode(person, DescriptionCode.INCREASED_TEMPERATURE) //
				.resolveAutomatically(items::save);

		// Body temperature exceeds reference
		if (entry.getBodyTemperature().exceeds(config.getTemperatureThreshold())) {
			Description description = Description.of(DescriptionCode.INCREASED_TEMPERATURE, //
					entry.getBodyTemperature(), //
					config.getTemperatureThreshold());

			items.save(new DiaryEntryActionItem(person, entry, ItemType.MEDICAL_INCIDENT, description));
		}
	}

	private boolean determineWhetherEntryMostRecent(TrackedPersonIdentifier personId, Slot slot) {

		return diaryManagement.findDiaryFor(personId) //
				.filter(it -> it.getSlot().isAfter(slot)) //
				.isEmpty();
	}

	void handleDiaryEntryForCharacteristicSymptoms(DiaryEntry entry) {

		var person = entry.getTrackedPersonId();
		var actionItems = DiaryEntryActionItems
				.of(items.findUnresolvedByDescriptionCode(person, DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM), items::save);

		// Characteristic symptom reported
		if (entry.getSymptoms().hasCharacteristicSymptom()) {
			actionItems.adjustFirstCharacteristicSymptomTo(entry, it -> createItem(person, it));
		} else {
			actionItems.removeFirstCharacteristicSymptomFrom(entry, () -> diaryManagement.findDiaryFor(person),
					it -> createItem(person, it));
		}
	}

	private DiaryEntryActionItem createItem(TrackedPersonIdentifier person, DiaryEntry entry) {
		return new DiaryEntryActionItem(person, entry, ItemType.MEDICAL_INCIDENT,
				Description.of(DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM));
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
