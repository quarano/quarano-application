package quarano.department;

import quarano.account.Department.DepartmentIdentifier;
import quarano.department.TrackedCase.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.util.Optionals;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.jpa.JPQLQuery;

/**
 * Manual implementations of Querydsl-based queries. {@link QuerydslPredicateExecutor} was not an option as we need to
 * define fetch joins explicitly.
 *
 * @author Oliver Drotbohm
 */
public interface TrackedCaseQuerydslFilters {

	Streamable<TrackedCase> findFiltered(Optional<String> query, Optional<String> type, DepartmentIdentifier id);

	Streamable<TrackedCase> findFilteredByQuarantine(Optional<LocalDate> quarantineChangedFrom,
			Optional<LocalDate> quarantineChangedTo, Optional<String> type, DepartmentIdentifier id);

	/**
	 * @since 1.4
	 */
	Streamable<TrackedCase> findFiltered(Optional<LocalDate> createdFrom, Optional<LocalDate> createdTo,
			Optional<LocalDate> lastModifiedFrom, Optional<LocalDate> lastModifiedTo,
			IncludeFilterOptions sormasId, IncludeFilterOptions externalCases, Optional<String> type,
			Optional<String> status, DepartmentIdentifier id);

	static class TrackedCaseQuerydslFiltersImpl extends QuerydslRepositorySupport implements TrackedCaseQuerydslFilters {

		public TrackedCaseQuerydslFiltersImpl() {
			super(TrackedCase.class);
		}

		@Override
		public Streamable<TrackedCase> findFiltered(Optional<String> query, Optional<String> type,
				DepartmentIdentifier id) {

			Assert.notNull(query, "Query must not be null!");
			Assert.notNull(type, "Type constraint must not be null!");
			Assert.notNull(id, "Department identifier must not be null!");

			var $case = QTrackedCase.trackedCase;
			var $casePerson = $case.trackedPerson;
			var builder = new BooleanBuilder();

			var predicate = query.map(it -> builder.and($casePerson.firstName.containsIgnoreCase(it)
					.or($casePerson.lastName.containsIgnoreCase(it))))
					.orElseGet(() -> builder)
					.and(applyCaseType(type))
					.and($case.department.id.eq(id));

			var jpql = queryBase().where(predicate)
					.orderBy($casePerson.lastName.asc(), $casePerson.firstName.asc());

			return Streamable.of(jpql.fetch());
		}

		/**
		 * Returns all {@link TrackedCase} of the given department that currently have a quarantine and whose quarantine has
		 * been changed in the range of the two date parameters {@code quarantineChangedFrom} and
		 * {@code quarantineChangedFrom} and whose {@link CaseType} represented by the give type constraint.
		 *
		 * @param quarantineChangedFrom must not be {@literal null}.
		 * @param quarantineChangedTo must not be {@literal null}.
		 * @param type must not be {@literal null}.
		 * @param id must not be {@literal null}.
		 * @return
		 */
		@Override
		public Streamable<TrackedCase> findFilteredByQuarantine(Optional<LocalDate> quarantineChangedFrom,
				Optional<LocalDate> quarantineChangedTo, Optional<String> type, DepartmentIdentifier id) {

			Assert.notNull(quarantineChangedFrom, "Quarantine changed from must not be null!");
			Assert.notNull(quarantineChangedTo, "Quarantine changed to must not be null!");
			Assert.notNull(type, "Type constraint must not be null!");
			Assert.notNull(id, "Department identifier must not be null!");

			var $case = QTrackedCase.trackedCase;
			var $casePerson = $case.trackedPerson;
			var $address = $casePerson.address;
			var $zipCode = $address.zipCode;

			var builder = new BooleanBuilder($case.quarantineLastModified.isNotNull());

			if (Optionals.isAnyPresent(quarantineChangedFrom, quarantineChangedTo)) {

				builder = builder.and($case.quarantineLastModified
						.between(
								fromDate(quarantineChangedFrom),
								toDate(quarantineChangedTo)));
			}

			var predicate = builder
					.and(DateExpression.currentDate(LocalDate.class).between($case.quarantine.from, $case.quarantine.to))
					.and(applyCaseType(type))
					.and($case.department.id.eq(id));

			var jpql = queryBase().where(predicate)
					.orderBy($zipCode.value.asc(), $address.city.asc(), $casePerson.lastName.asc(), $casePerson.firstName.asc());

			return Streamable.of(jpql.fetch());
		}

		/**
		 * @since 1.4
		 */
		@Override
		public Streamable<TrackedCase> findFiltered(Optional<LocalDate> createdFrom,
				Optional<LocalDate> createdTo, Optional<LocalDate> lastModifiedFrom,
				Optional<LocalDate> lastModifiedTo, IncludeFilterOptions sormasId,
				IncludeFilterOptions onlyExternalCases, Optional<String> type, Optional<String> status,
				DepartmentIdentifier id) {

			Assert.notNull(createdFrom, "Created from must not be null!");
			Assert.notNull(createdTo, "Created to must not be null!");
			Assert.notNull(lastModifiedFrom, "Last modified from must not be null!");
			Assert.notNull(lastModifiedTo, "Last modified to must not be null!");
			Assert.notNull(type, "Type constraint must not be null!");
			Assert.notNull(id, "Department identifier must not be null!");

			var $case = QTrackedCase.trackedCase;
			var $caseMetadata = $case.metadata;
			var person = $case.trackedPerson;
			var $personMetadata = person.metadata;
			var $address = person.address;
			var $zipCode = $address.zipCode;

			var builder = new BooleanBuilder($caseMetadata.lastModified.isNotNull());

			if (Optionals.isAnyPresent(createdFrom, createdTo)) {

				builder = builder.and($caseMetadata.created
						.between(fromDate(createdFrom), toDate(createdTo)));
			}

			if (Optionals.isAnyPresent(lastModifiedFrom, lastModifiedTo)) {

				builder = builder.and($caseMetadata.lastModified
						.between(fromDate(lastModifiedFrom), toDate(lastModifiedTo))
						.or($personMetadata.lastModified
								.between(fromDate(lastModifiedFrom), toDate(lastModifiedTo))));
			}

			if (sormasId == IncludeFilterOptions.INCLUDE_ONLY) {
				builder = builder.and($case.sormasCaseId.isNotNull());
			} else if (sormasId == IncludeFilterOptions.EXCLUDE_ONLY) {
				builder = builder.and($case.sormasCaseId.isNull());
			}

			if (onlyExternalCases == IncludeFilterOptions.INCLUDE_ONLY) {
				builder = builder.and($case.status.eq(Status.EXTERNAL_ZIP));
			} else if (onlyExternalCases == IncludeFilterOptions.EXCLUDE_ONLY) {
				builder = builder.and($case.status.ne(Status.EXTERNAL_ZIP));
			}

			var predicate = builder
					.and(applyCaseType(type))
					.and(applyStatus(status))
					.and($case.department.id.eq(id));

			var jpql = queryBase().where(predicate)
					.orderBy(person.lastName.asc(), person.firstName.asc());

			return Streamable.of(jpql.fetch());
		}

		private LocalDateTime toDate(Optional<LocalDate> to) {
			return to.map(it -> it.plusDays(1)).map(LocalDate::atStartOfDay).orElse(null);
		}

		private LocalDateTime fromDate(Optional<LocalDate> from) {
			return from.map(LocalDate::atStartOfDay).orElse(null);
		}

		private JPQLQuery<TrackedCase> queryBase() {

			var $case = QTrackedCase.trackedCase;
			var $casePerson = $case.trackedPerson;

			return from($case)
					.join($casePerson).fetchJoin()
					.leftJoin($case.questionnaire).fetchJoin()
					.leftJoin($case.department).fetchJoin();
		}

		/**
		 * Returns a {@link Predicate} to filter for the {@link CaseType} referred to using the given type token.
		 *
		 * @param type must not be {@literal null}.
		 * @return
		 */
		private static Predicate applyCaseType(Optional<String> type) {

			var $case = QTrackedCase.trackedCase;
			var none = new BooleanBuilder();

			return type.map(it -> {

				switch (it.toLowerCase()) {
					case "index":
						return $case.type.eq(CaseType.INDEX);
					case "contact":
						return $case.type.in(CaseType.CONTACT.getAllTypes());
					default:
						return none;
				}

			}).orElse(none);
		}

		/**
		 * Returns a {@link Predicate} to filter for the {@link Status} referred to using the given status token.
		 *
		 * @param status must not be {@literal null}.
		 * @return
		 * @since 1.4
		 */
		private static Predicate applyStatus(Optional<String> status) {

			var $case = QTrackedCase.trackedCase;
			var none = new BooleanBuilder();

			return status.map(it -> {

				try {
					return $case.status.eq(Status.valueOf(it.toUpperCase()));
				} catch (Exception e) {
					return none;
				}

			}).orElse(none);
		}
	}

	static enum IncludeFilterOptions {
		INCLUDE_ONLY, EXCLUDE_ONLY, IRRELEVANT
	}
}
