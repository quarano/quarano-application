package quarano.actions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItem.ItemType;
import quarano.department.TrackedCase;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialCallHandler {
	private final @NonNull ActionItemRepository items;

	void handleInitialCallOpen(TrackedCase trackedCase) {
		var person = trackedCase.getTrackedPerson();
		var descriptionCode = trackedCase.isIndexCase() ? DescriptionCode.INITIAL_CALL_OPEN_INDEX
				: DescriptionCode.INITIAL_CALL_OPEN_CONTACT;

		if (trackedCase.isInitialCallNeeded()) {
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
