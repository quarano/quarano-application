package quarano.actions.quarantine;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import quarano.actions.ActionItemRepository;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.CaseConcluded;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

@Component
@Slf4j
@RequiredArgsConstructor
public class QuarantineEventListener {

	private final ActionItemRepository items;
	private final TrackedCaseRepository cases;

	@EventListener
	public void on(CaseConcluded event) {
		TrackedCaseIdentifier quarantineCaseId = event.getCaseIdentifier();
		deleteQuarantineActionItems(quarantineCaseId);
	}

	private void deleteQuarantineActionItems(TrackedCaseIdentifier quarantineCaseId) {
		Optional<TrackedCase> quarantineCase = cases.findById(quarantineCaseId);
		if (quarantineCase.isEmpty()) {
			log.debug("Case {} concluded before QuarantineActionItem was created", quarantineCaseId);
			return;
		}
		TrackedPersonIdentifier trackedPerson = getTrackedPersonIdentifier(quarantineCase.get());

		items.findQuarantineEndingActionItemsFor(trackedPerson)
				.forEach(items::delete);
	}

	private TrackedPersonIdentifier getTrackedPersonIdentifier(TrackedCase trackedCase) {
		return trackedCase.getTrackedPerson().getId();
	}
}
