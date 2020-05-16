package quarano.actions.quarantine;

import static quarano.department.TrackedCase.Status.OPEN;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import quarano.actions.ActionItemRepository;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;

@RequiredArgsConstructor
@Slf4j
@Component
public class QuarantineEndChecker {

	private final TrackedCaseRepository trackedCases;
	private final ActionItemRepository items;

	@Scheduled(cron = "0 8 * * *")
	public void checkEndingQuarantinesPeriodically() {
		List<TrackedCase> endingQuarantineCases = getEndingQuarantineCases();
		createActionItems(endingQuarantineCases);
	}

	private void createActionItems(List<TrackedCase> endingQuarantineCases) {
		endingQuarantineCases.stream()
				.filter(trackedCase -> items.findQuarantineEndingActionItemsFor(trackedCase.getTrackedPerson().getId()).isEmpty())
				.map(trackedCase -> new QuarantineEndingActionItem(trackedCase.getTrackedPerson().getId()))
				.forEach(items::save);
	}

	private List<TrackedCase> getEndingQuarantineCases() {
		return trackedCases.findAll().stream()
				.filter(TrackedCase::isIndexCase)
				.filter(trackedCase -> OPEN.equals(trackedCase.getStatus()))
				.filter(trackedCase -> {
					var quarantineEnd = trackedCase.getQuarantine().getTo();
					DateTime now = DateTime.now();
					return now.isAfter(quarantineEnd.toEpochSecond(LocalTime.MIN, ZoneOffset.UTC));
				})
				.collect(Collectors.toList());
	}
}
