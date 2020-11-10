package quarano.actions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItem.ItemType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Lennart Blom
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@Component
@Profile("!integrationtest")
class QuarantineEndChecker {

	private final @NonNull TrackedCaseRepository trackedCases;
	private final @NonNull ActionItemRepository items;

	@Scheduled(cron = "0 8 * * * *")
	void checkEndingQuarantinesPeriodically() {

		trackedCases.findAll().stream()
				.filter(TrackedCase::isIndexCase)
				.filter(it -> it.isTracking())
				.filter(it -> it.getQuarantine().isOver())
				.filter(it -> items.findQuarantineEndingActionItemsFor(it).isEmpty())
				.map(this::createQuarantineEndedActionItem)
				.forEach(items::save);
	}

	private TrackedCaseActionItem createQuarantineEndedActionItem(TrackedCase trackedCase) {
		return new TrackedCaseActionItem(trackedCase, ItemType.PROCESS_INCIDENT, DescriptionCode.QUARANTINE_ENDING);
	}
}
