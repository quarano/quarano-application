package quarano.department;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.department.TrackedCase.CaseConvertedToIndex;

import java.util.Optional;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
@Slf4j
@Component("department.TrackedCaseEventListener")
@RequiredArgsConstructor
class TrackedCaseEventListener {

	private final @NonNull TrackedCaseRepository cases;

	@EventListener
	void on(CaseConvertedToIndex event) {

		Optional.of(event.getTrackedCase())
				.filter(TrackedCase::isIndexCase)
				.stream()
				.flatMap(CaseSource::forAllContacts)
				.filter(it -> !cases.existsByOriginContacts(it.getPerson()))
				.peek(it -> log.info("Created automatic contact case from contact " + it.getPerson().getId()))
				.map(TrackedCase::of)
				.forEach(cases::save);
	}
}
