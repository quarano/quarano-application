package quarano.actions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.department.GDPRAnonymizationJob.CasesAnonymized;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.CaseConcluded;
import quarano.department.TrackedCase.CaseCreated;
import quarano.department.TrackedCase.CaseStatusUpdated;
import quarano.department.TrackedCase.CaseUpdated;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseRepository;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Slf4j
@Component("actions.TrackedCaseEventListener")
@RequiredArgsConstructor
class TrackedCaseEventListener {

	private final @NonNull InitialCallHandler initialCallHandler;
	private final @NonNull MissingDetailsHandler missingDetailsHandler;

	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull ActionItemRepository items;

	@EventListener
	void on(CaseCreated event) {
		handleCreatedOrUpdatedCase(event.getTrackedCase());
	}

	@EventListener
	void on(CaseUpdated event) {
		handleCreatedOrUpdatedCase(event.getTrackedCase());
	}

	@EventListener
	void on(CaseStatusUpdated event) {
		handleCreatedOrUpdatedCase(event.getTrackedCase());
	}

	@EventListener
	void on(CaseConcluded event) {

		cases.findById(event.getCaseIdentifier())
				.ifPresent(it -> {

					ActionItems openItems = items.findQuarantineEndingActionItemsFor(it);

					if (!openItems.hasUnresolvedItems()) {
						return;
					}

					log.debug("Resolving quarantine to be closed action item on case conclusion for case {}.", it.getId());

					openItems.resolveManually(items::save);
				});
	}

	@EventListener
	void on(CasesAnonymized event) {

		var cases = event.getCases();

		log.debug("actions of {} cases are anonymized!", cases.size());

		for (TrackedCase trackedCase : cases) {
			items.saveAll(items.findByCase(trackedCase).map(ActionItem::anonymize).toList());
		}

		log.info("actions of {} cases were anonymized!", cases.size());
	}

	private void handleCreatedOrUpdatedCase(TrackedCaseIdentifier identifier) {

		var trackedCase = cases.findById(identifier).orElseThrow();

		initialCallHandler.handleInitialCallOpen(trackedCase);
		missingDetailsHandler.handleTrackedCaseMissingDetails(trackedCase);
	}
}
