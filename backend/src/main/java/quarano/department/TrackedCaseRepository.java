package quarano.department;

import quarano.account.Department.DepartmentIdentifier;
import quarano.core.QuaranoRepository;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.tracking.ContactPerson;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

/**
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
public interface TrackedCaseRepository
		extends QuaranoRepository<TrackedCase, TrackedCaseIdentifier>, QuerydslPredicateExecutor<TrackedCase> {

	Streamable<TrackedCase> findByDepartmentId(DepartmentIdentifier id);

	@Query("select c from TrackedCase c JOIN c.trackedPerson p WHERE c.department.id = :id ORDER BY p.lastName")
	Streamable<TrackedCase> findByDepartmentIdOrderByLastNameAsc(DepartmentIdentifier id);

	/**
	 * Returns all {@link TrackedCase} of the given department that match the given query {@link String} in the
	 * {@link TrackedPerson}'s first- or lastname and {@link CaseType} represented by the give type constraint.
	 *
	 * @param query must not be {@literal null}.
	 * @param type must not be {@literal null}.
	 * @param id must not be {@literal null}.
	 * @return
	 */
	default Streamable<TrackedCase> findFiltered(Optional<String> query, Optional<String> type, DepartmentIdentifier id) {

		Assert.notNull(query, "Query must not be null!");
		Assert.notNull(type, "Type constraint must not be null!");
		Assert.notNull(id, "Department identifier must not be null!");

		var $case = QTrackedCase.trackedCase;
		var $person = $case.trackedPerson;
		var builder = new BooleanBuilder();

		var predicate = query.map(it -> builder.and($person.firstName.containsIgnoreCase(it)
				.or($person.lastName.containsIgnoreCase(it))))
				.orElseGet(() -> builder)
				.and(applyCaseType(type))
				.and($case.department.id.eq(id));

		return Streamable.of(findAll(predicate, new OrderSpecifier<>(Order.ASC, $person.lastName)));
	}

	Optional<TrackedCase> findByTrackedPerson(TrackedPerson person);

	@Query("select c from TrackedCase c where c.trackedPerson.id = :identifier")
	Optional<TrackedCase> findByTrackedPerson(TrackedPersonIdentifier identifier);

	/**
	 * Returns whether a {@link TrackedCase} exists that's associated with the given {@link ContactPerson}.
	 *
	 * @param person must not be {@literal null}.
	 * @return
	 */
	boolean existsByOriginContacts(ContactPerson person);

	/**
	 * Returns the optional {@link TrackedCase} that's associated with the given {@link ContactPerson}.
	 *
	 * @param person must not be {@literal null}.
	 * @return
	 */
	Optional<TrackedCase> findByOriginContacts(ContactPerson person);

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
