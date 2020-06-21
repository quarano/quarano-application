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
	 * Returns all {@link TrackedCase} of the given department that match the given query String in the
	 * {@link TrackedPerson}'s first- or lastname.
	 *
	 * @param query must not be {@literal null}.
	 * @param id must not be {@literal null}.
	 * @return
	 */
	default Streamable<TrackedCase> findFiltered(Optional<String> query, DepartmentIdentifier id) {

		Assert.notNull(query, "Query must not be null!");
		Assert.notNull(id, "Department identifier must not be null!");

		var $case = QTrackedCase.trackedCase;
		var $person = $case.trackedPerson;
		var builder = new BooleanBuilder();

		var predicate = query.map(it -> builder.and($person.firstName.containsIgnoreCase(it) //
				.or($person.lastName.containsIgnoreCase(it)))) //
				.orElseGet(() -> builder) //
				.and($case.department.id.eq(id)); //

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
}
