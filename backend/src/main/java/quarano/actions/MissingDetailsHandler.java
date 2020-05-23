package quarano.actions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItem.ItemType;
import quarano.department.TrackedCase;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class MissingDetailsHandler {

	private final @NonNull ActionItemRepository items;

	void handleTrackedCaseMissingDetails(TrackedCase trackedCase) {

		var personId = trackedCase.getTrackedPerson().getId();
		var descriptionCode = trackedCase.isIndexCase() //
				? DescriptionCode.MISSING_DETAILS_INDEX //
				: DescriptionCode.MISSING_DETAILS_CONTACT;

		if (!trackedCase.isInvestigationNeeded()) {
			items.findByDescriptionCode(personId, descriptionCode).resolveAutomatically(items::save);
			return;
		}

		if (items.findByDescriptionCode(personId, descriptionCode).isEmpty()) {
			items.save(new TrackedCaseActionItem(trackedCase, ItemType.PROCESS_INCIDENT, descriptionCode));
		}
	}
}
