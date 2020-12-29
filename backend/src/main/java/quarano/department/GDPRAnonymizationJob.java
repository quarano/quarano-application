package quarano.department;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import quarano.diary.DiaryManagement;
import quarano.tracking.ContactPersonRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;

import org.jmolecules.event.types.DomainEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class collects all old data from cases with their person and related data and anonymizes the personal data to be
 * GDPR compliant.
 *
 * @author Jens Kutzsche
 * @since 1.4
 */
@Component("department.GDPRAnonymizationJob")
@Slf4j
@RequiredArgsConstructor
public class GDPRAnonymizationJob {

	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull ContactPersonRepository contacts;
	private final @NonNull DiaryManagement diaries;
	private final @NonNull CaseAnonymizationProperties properties;
	private final @NonNull ApplicationEventPublisher eventPublisher;

	@Transactional
	@Scheduled(cron = "0 10 1 * * *")
	void anonymizeCasesAndRelatedData() {

		var refDate = LocalDate.now().minus(properties.getAnonymizationAfter()).atStartOfDay();

		var oldCases = cases.findByMetadataCreatedIsBefore(refDate).toList();

		log.debug(
				"{} cases with their person, account, comments, questionnaire and diaries are anonymized {} days after their creation!",
				oldCases.size(),
				properties.getAnonymizationAfter().getDays());

		oldCases.forEach(TrackedCase::anonymize);
		cases.saveAll(oldCases);

		oldCases.stream().forEach(it -> diaries.anonymizeDiaryFor(it.getTrackedPerson()));

		eventPublisher.publishEvent(CasesAnonymized.of(oldCases));

		log.info(
				"{} cases with their person, account, comments, questionnaire and diaries were anonymized {} days after their creation!",
				oldCases.size(),
				properties.getAnonymizationAfter().getDays());
	}

	@ConstructorBinding
	@RequiredArgsConstructor
	@ConfigurationProperties("quarano.gdpr.cases")
	public static class CaseAnonymizationProperties {

		private final Period DEFAULT_ANONYMIZATION_AFTER = Period.ofMonths(6);

		/**
		 * Defines the {@link Period} after that a case will be anonymized starting from the creation date.
		 */
		private final Period anonymizationAfter;

		public Period getAnonymizationAfter() {
			return anonymizationAfter == null ? DEFAULT_ANONYMIZATION_AFTER : anonymizationAfter;
		}
	}

	@Value(staticConstructor = "of")
	public static class CasesAnonymized implements DomainEvent {
		Collection<TrackedCase> cases;
	}
}
