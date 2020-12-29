package quarano.occasion;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.DepartmentRepository;
import quarano.tracking.TrackedPersonRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class collects all old occasion and visitor data and anonymizes the personal data to be GDPR compliant.
 *
 * @author Jens Kutzsche
 * @since 1.4
 */
@Component
@Slf4j
@RequiredArgsConstructor
class GDPRDeleteJob {

	private final @NonNull TrackedPersonRepository people;
	private final @NonNull DepartmentRepository departments;
	private final @NonNull OccasionRepository occasions;
	private final @NonNull OccationDeletionProperties properties;

	@Transactional
	@Scheduled(cron = "0 10 0 * * *")
	void deleteOccasions() {

		var refDate = LocalDate.now().minus(properties.getDeleteOccasionsAfter()).atStartOfDay();

		var oldOccasions = occasions.findByMetadataCreatedIsBefore(refDate).toList();

		log.debug("{} occasions are deleted {} days after their creation!",
				oldOccasions.size(),
				properties.getDeleteOccasionsAfter().getDays());

		occasions.deleteAll(oldOccasions);

		log.info("{} occasions were deleted {} days after their creation!",
				oldOccasions.size(),
				properties.getDeleteOccasionsAfter().getDays());
	}

	@Transactional
	@Scheduled(cron = "0 20 0 * * *")
	void deleteVisitors() {

		var refDate = LocalDate.now().minus(properties.getDeleteVisitorsAfter()).atStartOfDay();

		var oldOccasions = occasions.findByMetadataCreatedIsBefore(refDate).toList();

		log.debug("Visitor groups and visitors of {} occasions are deleted {} days after their creation!",
				(long) oldOccasions.size(),
				properties.getDeleteOccasionsAfter().getDays());

		deleteVisitorsOf(oldOccasions);

		log.info("Visitor groups and visitors of {} occasions were deleted {} days after their creation!",
				(long) oldOccasions.size(),
				properties.getDeleteOccasionsAfter().getDays());
	}

	private void deleteVisitorsOf(List<Occasion> oldOccasions) {

		occasions.deleteAllVisitors(oldOccasions.stream()
				.flatMap(it -> it.getVisitorGroups().stream())
				.flatMap(it -> (it.getVisitors() != null ? it.getVisitors() : List.<Visitor> of()).stream())
				.collect(Collectors.toList()));

		occasions.deleteVisitorGroupsByOccasionCodeIn(oldOccasions.stream()
				.map(Occasion::getOccasionCode)
				.collect(Collectors.toList()));

		occasions.saveAll(oldOccasions.stream()
				.peek(it -> it.getVisitorGroups().clear())
				.collect(Collectors.toList()));
	}

	@ConstructorBinding
	@RequiredArgsConstructor
	@ConfigurationProperties("quarano.gdpr.occasions")
	public static class OccationDeletionProperties {

		private final Period DEFAULT_DELETE_OCCASIONS_AFTER = Period.ofMonths(6);
		private final Period DEFAULT_DELETE_VISITORS_AFTER = Period.ofDays(16);

		/**
		 * Defines the {@link Period} after that a occation will be deleted starting from the creation date.
		 */
		private final Period deleteOccasionsAfter;

		/**
		 * Defines the {@link Period} after that a visitor group with its visitors will be deleted starting from the
		 * creation date of its occation.
		 */
		private final Period deleteVisitorsAfter;

		public Period getDeleteOccasionsAfter() {
			return deleteOccasionsAfter == null ? DEFAULT_DELETE_OCCASIONS_AFTER : deleteOccasionsAfter;
		}

		public Period getDeleteVisitorsAfter() {
			return deleteVisitorsAfter == null ? DEFAULT_DELETE_VISITORS_AFTER : deleteVisitorsAfter;
		}
	}
}
