package quarano.actions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.CaseCreated;
import quarano.department.TrackedCase.CaseStatusUpdated;
import quarano.department.TrackedCase.CaseUpdated;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
public class TrackedCaseEventListener {

	private final @NonNull InitialCallHandler initialCallHandler;
	private final @NonNull MissingDetailsHandler missingDetailsHandler;

	@EventListener
	public void on(CaseCreated event) {
		handleCreatedOrUpdatedCase(event.getTrackedCase());
	}

	@EventListener
	public void on(CaseUpdated event) {
		handleCreatedOrUpdatedCase(event.getTrackedCase());
	}

	@EventListener
	public void on(CaseStatusUpdated event) {
		handleCreatedOrUpdatedCase(event.getTrackedCase());
	}

	private void handleCreatedOrUpdatedCase(TrackedCase trackedCase) {
		initialCallHandler.handleInitialCallOpen(trackedCase);
		missingDetailsHandler.handleTrackedCaseMissingDetails(trackedCase);
	}
}
