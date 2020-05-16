package quarano.actions;

import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItem.ItemType;
import quarano.department.TrackedCase;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class MissingDetailsHandler {
	private final ActionItemRepository items;

	void handleTrackedCaseMissingDetails(TrackedCase trackedCase) {

		var person = trackedCase.getTrackedPerson();
		var descriptionCode = trackedCase.isIndexCase() ? DescriptionCode.MISSING_DETAILS_INDEX : DescriptionCode.MISSING_DETAILS_CONTACT;
		if (trackedCase.isInvestigationNeeded()) {
			if (items.findByDescriptionCode(person.getId(), descriptionCode).isEmpty()) {
				items.save(
						new TrackedCaseActionItem(person.getId(), trackedCase.getId(), ItemType.PROCESS_INCIDENT, descriptionCode));
			}
		} else {
			items.findByDescriptionCode(person.getId(), descriptionCode) //
					.resolveAutomatically(items::save);
		}
	}
}
