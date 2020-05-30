package quarano.actions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.actions.ActionItem.ItemType;
import quarano.diary.DiaryEntryMissing;
import quarano.diary.DiaryManagement;
import quarano.diary.Slot;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;
import quarano.diary.DiaryEntry;
import quarano.diary.DiaryEntry.DiaryEntryAdded;
import quarano.diary.DiaryEntry.DiaryEntryUpdated;

import java.util.Comparator;

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
	void onDiaryEntryAddedToResolveMissingItems(DiaryEntryAdded event) {

		var entry = event.getEntry();
		var person = entry.getTrackedPersonId();
		var slot = entry.getSlot();

		// resolve missing entry item
		items.findDiaryEntryMissingActionItemsFor(person, slot) //
				.resolveAutomatically(items::save);
	}

	@EventListener
	void onDiaryEntryAddedForBodyTemperature(DiaryEntryAdded event) {
		handleDiaryEntryForBodyTemprature(event.getEntry());
	}

	@EventListener
	void onDiaryEntryUpdatedForBodyTemperature(DiaryEntryUpdated event) {
		handleDiaryEntryForBodyTemprature(event.getEntry());
	}

	/**
	 * Only does something if the entry with the most current slot is edited. First resolves all existing action items for
	 * temperature. If handles an added or updated diary entry with to high temperature a new action item is added. There
	 * is no update of existing items.
	 * 
	 * @param entry
	 */
	private void handleDiaryEntryForBodyTemprature(DiaryEntry entry) {
		var person = entry.getTrackedPersonId();

		if (!determineWhetherEntryMostRecent(person, entry.getSlot())) {
			return;
		}

		items.findByDescriptionCode(person, DescriptionCode.INCREASED_TEMPERATURE) //
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
		return diaryManagement.findDiaryFor(personId).filter(it -> it.getSlot().compareTo(slot) > 0).isEmpty();
	}

	@EventListener
	void onDiaryEntryAddedForCharacteristicSymptoms(DiaryEntryAdded event) {
		handleDiaryEntryForCharacteristicSymptoms(event.getEntry());
	}

	@EventListener
	void onDiaryEntryUpdatedForCharacteristicSymptoms(DiaryEntryUpdated event) {
		handleDiaryEntryForCharacteristicSymptoms(event.getEntry());
	}

	private void handleDiaryEntryForCharacteristicSymptoms(DiaryEntry entry) {
		var slot = entry.getSlot();
		var person = entry.getTrackedPersonId();

		var actionItems = items.findByDescriptionCode(person, DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM);
		var diaryEntryActionItems = actionItems.filter(DiaryEntryActionItem.class::isInstance)//
				.map(DiaryEntryActionItem.class::cast);

		// First characteristic symptom reported
		if (entry.getSymptoms().hasCharacteristicSymptom()) {
			if (actionItems.isEmpty()) {
				createAndSaveItem(person, entry);
			} else {
				var isNewer = diaryEntryActionItems//
						.filter(it -> it.getEntry().getSlot().compareTo(slot) > 0)//
						.map(DiaryEntryActionItem::resolve)//
						.map(items::save).isEmpty();
				if (!isNewer) {
					createAndSaveItem(person, entry);
				}
			}
		} else {
			// resolve action item if the corresponding diary entry was modified
			var actionResolved = !diaryEntryActionItems.filter(it -> it.getEntry().getSlot().compareTo(slot) == 0)//
					.map(DiaryEntryActionItem::resolve)//
					.map(items::save)//
					.isEmpty();

			if (actionResolved) {
				// search another first diary entry with symptoms when the previous action item was resolved
				var diary = diaryManagement.findDiaryFor(person);
				var firstEntryWithSymptoms = diary.stream()//
						.sorted(Comparator.comparing(DiaryEntry::getSlot))//
						.filter(it -> it.getSymptoms().hasCharacteristicSymptom())//
						.findFirst();
				firstEntryWithSymptoms.ifPresent(it -> createAndSaveItem(person, it));
			}
		}
	}

	private DiaryEntryActionItem createAndSaveItem(TrackedPersonIdentifier person, DiaryEntry entry) {
		return items.save(new DiaryEntryActionItem(person, entry, ItemType.MEDICAL_INCIDENT,
				Description.of(DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM)));
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
