package quarano.department;

import quarano.account.Department.DepartmentIdentifier;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

/**
 * Manual implementations of Querydsl-based queries. {@link QuerydslPredicateExecutor} was not an option as we need to
 * define fetch joins explicitly.
 *
 * @author Oliver Drotbohm
 */
public interface TrackedCaseQuerydslFilters {

	Streamable<TrackedCase> findFiltered(Optional<String> query, Optional<String> type, DepartmentIdentifier id);

	static class TrackedCaseQuerydslFiltersImpl extends QuerydslRepositorySupport implements TrackedCaseQuerydslFilters {

		public TrackedCaseQuerydslFiltersImpl() {
			super(TrackedCase.class);
		}

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

			var jpql = from($case)
					.join($casePerson).fetchJoin()
					.leftJoin($case.questionnaire).fetchJoin()
					.leftJoin($case.originCases).fetchJoin()
					.where(predicate)
					.orderBy($casePerson.lastName.asc(), $casePerson.firstName.asc());

			return Streamable.of(jpql.fetch());
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

				switch (it) {
					case "index":
						return $case.type.eq(CaseType.INDEX);
					case "contact":
						return $case.type.in(CaseType.CONTACT.getAllTypes());
					default:
						return none;
				}

			}).orElse(none);
		}
	}
}
