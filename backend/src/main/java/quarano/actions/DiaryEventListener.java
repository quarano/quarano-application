package quarano.actions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.actions.ActionItem.ItemType;
import quarano.department.CaseType;
import quarano.department.TrackedCaseRepository;
import quarano.diary.DiaryEntry;
import quarano.diary.DiaryEntry.DiaryEntryAdded;
import quarano.diary.DiaryEntry.DiaryEntryUpdated;
import quarano.diary.DiaryEntryMissing;
import quarano.diary.DiaryManagement;
import quarano.diary.Slot;
import quarano.masterdata.Symptoms;
import quarano.tracking.BodyTemperature;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import org.springframework.context.event.EventListener;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
class DiaryEventListener {

	private final @NonNull AnomaliesProperties config;
	private final @NonNull ActionItemRepository items;
	private final @NonNull DiaryManagement diaryManagement;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull MessageSourceAccessor messages;

	@EventListener
	void on(DiaryEntryAdded event) {

		var entry = event.getEntry();

		handleDiaryEntryForBodyTemperature(entry);
		handleDiaryEntryForSuspiciousSymptoms(entry);
		handleDiaryEntryForCharacteristicSymptoms(entry);
		resolveMissingItemsActionItem(entry);
	}

	@EventListener
	void on(DiaryEntryUpdated event) {

		var entry = event.getEntry();

		handleDiaryEntryForBodyTemperature(entry);
		handleDiaryEntryForSuspiciousSymptoms(entry);
		handleDiaryEntryForCharacteristicSymptoms(entry);
	}

	@EventListener
	void on(DiaryEntryMissing diaryEntryMissing) {

		log.debug("Caught diaryEntryMissing Event {}. Save it as DiaryEntryMissingActionItem", diaryEntryMissing);

		diaryEntryMissing.getMissingSlots().stream()
				.map(slot -> new DiaryEntryMissingActionItem(diaryEntryMissing.getTrackedPersonIdentifier(), slot))
				.filter(item -> items.findDiaryEntryMissingActionItemsFor(item.getPersonIdentifier(), item.getSlot()).isEmpty())
				.forEach(items::save);
	}

	/**
	 * Only does something if the entry with the most current slot is edited. First resolves all existing action items for
	 * temperature. If handles an added or updated diary entry with too high temperature a new action item is added. There
	 * is no update of existing items.
	 *
	 * @param entry
	 */
	void handleDiaryEntryForBodyTemperature(DiaryEntry entry) {

		var person = entry.getTrackedPersonId();
		var type = cases.findTypeByTrackedPerson(person).orElse(CaseType.CONTACT);

		if (!determineWhetherEntryMostRecent(person, entry.getSlot())) {
			return;
		}

		items.findUnresolvedByDescriptionCode(person, DescriptionCode.INCREASED_TEMPERATURE)
				.resolveAutomatically(items::save);

		// Body temperature exceeds reference
		BodyTemperature temperatureThreshold = config.getTemperatureThreshold(type);
		if (entry.getBodyTemperature().exceeds(temperatureThreshold)) {

			var description = Description.of(DescriptionCode.INCREASED_TEMPERATURE, entry.getBodyTemperature(),
					temperatureThreshold);

			items.save(new DiaryEntryActionItem(person, entry, ItemType.MEDICAL_INCIDENT, description));
		}
	}

	/**
	 * Examines the diary entry for suspicious symptoms. If a suspicious symptom is selected in the diary entry, the rules
	 * from {@link DiaryEntryActionItems#adjustSuspiciousSymptomTo} are applied. If no suspicious symptom is selected in
	 * the diary entry, the rules from {@link DiaryEntryActionItems#removeSuspiciousSymptomFrom} are applied.
	 * 
	 * @param entry The diary entry to be examined.
	 * @since 1.4
	 */
	void handleDiaryEntryForSuspiciousSymptoms(DiaryEntry entry) {

		var person = entry.getTrackedPersonId();
		var type = cases.findTypeByTrackedPerson(person).orElse(CaseType.CONTACT);

		var actionItems = DiaryEntryActionItems
				.of(items.findUnresolvedByDescriptionCode(person, DescriptionCode.SUSPICIOUS_SYMPTOM), items::save);

		Symptoms symptoms = entry.getSymptoms();
		if (type == CaseType.INDEX
				? symptoms.hasSuspiciousSymptomeAtIndex()
				: symptoms.hasSuspiciousSymptomeAtContact()) {

			actionItems.adjustSuspiciousSymptomTo(entry, type, it -> createSuspiciousItem(person, type, it));
		} else {
			actionItems.removeSuspiciousSymptomFrom(entry, type, () -> diaryManagement.findDiaryFor(person),
					it -> createSuspiciousItem(person, type, it));
		}
	}

	/**
	 * Examines the diary entry for characteristic symptoms. Characteristic symptoms are only considered for index cases.
	 * If a characteristic symptom is selected in the diary entry, the rules from
	 * {@link DiaryEntryActionItems#adjustFirstCharacteristicSymptomTo} are applied. If no characteristic symptom is
	 * selected in the diary entry, the rules from {@link DiaryEntryActionItems#removeFirstCharacteristicSymptomFrom} are
	 * applied.
	 * 
	 * @param entry The diary entry to be examined.
	 */
	void handleDiaryEntryForCharacteristicSymptoms(DiaryEntry entry) {

		var person = entry.getTrackedPersonId();
		var type = cases.findTypeByTrackedPerson(person).orElse(CaseType.CONTACT);

		if (type == CaseType.INDEX) {
			return;
		}

		var actionItems = DiaryEntryActionItems
				.of(items.findUnresolvedByDescriptionCode(person, DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM), items::save);

		if (entry.getSymptoms().hasCharacteristicSymptom()) {
			actionItems.adjustFirstCharacteristicSymptomTo(entry, it -> createCharacteristicItem(person, it));
		} else {
			actionItems.removeFirstCharacteristicSymptomFrom(entry, () -> diaryManagement.findDiaryFor(person),
					it -> createCharacteristicItem(person, it));
		}
	}

	void resolveMissingItemsActionItem(DiaryEntry entry) {

		var person = entry.getTrackedPersonId();
		var slot = entry.getSlot();

		// resolve missing entry item
		items.findDiaryEntryMissingActionItemsFor(person, slot)
				.resolveAutomatically(items::save);
	}

	private boolean determineWhetherEntryMostRecent(TrackedPersonIdentifier personId, Slot slot) {

		return diaryManagement.findDiaryFor(personId)
				.filter(it -> it.getSlot().isAfter(slot))
				.isEmpty();
	}

	private DiaryEntryActionItem createSuspiciousItem(TrackedPersonIdentifier person, CaseType type, DiaryEntry entry) {
		return new DiaryEntryActionItem(person, entry, ItemType.MEDICAL_INCIDENT,
				Description.of(DescriptionCode.SUSPICIOUS_SYMPTOM, determineSymptom(entry, type)));
	}

	private String determineSymptom(DiaryEntry entry, CaseType type) {

		return entry.getSymptoms().stream()
				.filter(it -> type == CaseType.INDEX
						? it.isSuspiciousAtIndex()
						: it.isSuspiciousAtContact())
				.findFirst()
				.map(it -> messages.getMessage("symptom." + it.getId()))
				.orElseThrow();
	}

	private DiaryEntryActionItem createCharacteristicItem(TrackedPersonIdentifier person, DiaryEntry entry) {
		return new DiaryEntryActionItem(person, entry, ItemType.MEDICAL_INCIDENT,
				Description.of(DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM));
	}
}
