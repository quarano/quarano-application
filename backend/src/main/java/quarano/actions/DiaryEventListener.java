package quarano.actions;

import io.jsonwebtoken.lang.Assert;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.actions.ActionItem.ItemType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.diary.DiaryEntry;
import quarano.diary.DiaryEntry.DiaryEntryAdded;
import quarano.diary.DiaryEntry.DiaryEntryUpdated;
import quarano.diary.DiaryEntryMissing;
import quarano.diary.DiaryManagement;
import quarano.diary.Slot;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;
import quarano.tracking.TrackedPersonRepository;

@Component
@Slf4j
@RequiredArgsConstructor
class DiaryEventListener {

	private final @NonNull AnomaliesProperties config;
	private final @NonNull ActionItemRepository items;
	private final @NonNull DiaryManagement diaryManagement;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull TrackedPersonRepository persons;


	@EventListener
	void on(DiaryEntryAdded event) {

		var entry = event.getEntry();

		if (!isIndexCase(entry)) {
			handleDiaryEntryForBodyTemperature(entry);
			handleDiaryEntryForCharacteristicSymptoms(entry);
		}
		resolveMissingItemsActionItem(entry);
	}

	@EventListener
	void on(DiaryEntryUpdated event) {

		var entry = event.getEntry();

		if (!isIndexCase(entry)) {
			handleDiaryEntryForBodyTemperature(entry);
			handleDiaryEntryForCharacteristicSymptoms(entry);
		}
	}

	private boolean isIndexCase(DiaryEntry entry) {
		return cases.findByTrackedPerson(entry.getTrackedPersonId())
				.map(TrackedCase::isIndexCase)
				.orElse(Boolean.FALSE);
	}

	void resolveMissingItemsActionItem(DiaryEntry entry) {

		var person = entry.getTrackedPersonId();
		var slot = entry.getSlot();

		// resolve missing entry item
		items.findDiaryEntryMissingActionItemsFor(person, slot)
				.resolveAutomatically(items::save);
	}

	/**
	 * Only does something if the entry with the most current slot is edited. First resolves all existing action items for
	 * temperature. If handles an added or updated diary entry with too high temperature a new action item is added. There
	 * is no update of existing items.
	 *
	 * @param entry
	 */
	void handleDiaryEntryForBodyTemperature(DiaryEntry entry) {

		var person = entry.getTrackedPerson();

		if (!determineWhetherEntryMostRecent(person.getId(), entry.getSlot())) {
			return;
		}

		items.findUnresolvedByDescriptionCode(person.getId(), DescriptionCode.INCREASED_TEMPERATURE)
				.resolveAutomatically(items::save);

		// Body temperature exceeds reference
		if (entry.getBodyTemperature().exceeds(config.getTemperatureThreshold())) {

			var trackedCase = person.getTrackedCases().get(0);
			Assert.notNull(trackedCase, "No tracked case for person found");
			Description description = Description.of(DescriptionCode.INCREASED_TEMPERATURE,
					entry.getBodyTemperature(),
					config.getTemperatureThreshold());

			items.save(new DiaryEntryActionItem(person, trackedCase, entry, ItemType.MEDICAL_INCIDENT, description));
		}
	}

	private boolean determineWhetherEntryMostRecent(TrackedPersonIdentifier personId, Slot slot) {

		return diaryManagement.findDiaryFor(personId)
				.filter(it -> it.getSlot().isAfter(slot))
				.isEmpty();
	}

	/**
	 * manage the list of characteristic symptoms - the resulting action depends
	 * @param entry
	 */
	void handleDiaryEntryForCharacteristicSymptoms(DiaryEntry entry) {

		var person = entry.getTrackedPerson();
		var trackedCase = person.getTrackedCases().get(0);
		var actionItems = DiaryEntryActionItems
				.of(items.findUnresolvedByDescriptionCode(person.getId(), DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM), items::save);

		// Characteristic symptom reported
		if (entry.getSymptoms().hasCharacteristicSymptom()) {
			actionItems.adjustFirstCharacteristicSymptomTo(entry, it -> createItem(person, trackedCase, it));
		} else {
			actionItems.removeFirstCharacteristicSymptomFrom(entry, () -> diaryManagement.findDiaryFor(person),
					it -> createItem(person, trackedCase, it));
		}
	}

	private DiaryEntryActionItem createItem(TrackedPerson person, TrackedCase trackedCase, DiaryEntry entry) {
		return new DiaryEntryActionItem(person, trackedCase, entry, ItemType.MEDICAL_INCIDENT,
				Description.of(DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM));
	}

	@EventListener
	void on(DiaryEntryMissing diaryEntryMissing) {

		log.debug("Caught diaryEntryMissing Event {}. Save it as DiaryEntryMissingActionItem", diaryEntryMissing);
		persons.findById(diaryEntryMissing.getTrackedPersonIdentifier()).ifPresent(person -> {
			var trackedCase = person.getTrackedCases().get(0);
			Assert.notNull(trackedCase, "tracked person must have a tracked case association");
			diaryEntryMissing.getMissingSlots().stream()
					.map(slot -> new DiaryEntryMissingActionItem(person, trackedCase, slot))
					.filter(
							item -> items.findDiaryEntryMissingActionItemsFor(item.getPersonIdentifier(), item.getSlot()).isEmpty())
					.forEach(items::save);
		});
	}
}
