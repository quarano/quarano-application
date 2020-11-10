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

		var id = trackedCase.getTrackedPerson().getId();
		var descriptionCode = trackedCase.isIndexCase()
				? DescriptionCode.INITIAL_CALL_OPEN_INDEX
				: DescriptionCode.INITIAL_CALL_OPEN_CONTACT;

		if (!trackedCase.isInitialCallNeeded()) {
			items.findByDescriptionCode(id, descriptionCode).resolveAutomatically(items::save);
			return;
		}

		if (items.findByDescriptionCode(id, descriptionCode).isEmpty()) {
			items.save(new TrackedCaseActionItem(trackedCase, ItemType.PROCESS_INCIDENT, descriptionCode));
		}
	}
}
