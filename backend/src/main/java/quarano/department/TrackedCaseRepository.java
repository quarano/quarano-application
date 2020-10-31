package quarano.department;

import quarano.account.Account;
import quarano.account.Department.DepartmentIdentifier;
import quarano.core.QuaranoRepository;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.tracking.ContactPerson;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
public interface TrackedCaseRepository
		extends QuaranoRepository<TrackedCase, TrackedCaseIdentifier>, TrackedCaseQuerydslFilters {

	static final String DEFAULT_SELECT = //
			"select c from TrackedCase c "
					+ "join fetch c.trackedPerson p "
					+ "join fetch c.department d "
					+ "left join fetch c.originCases ";

	@Query(DEFAULT_SELECT + " where d.id = :identifier")
	Streamable<TrackedCase> findByDepartmentId(DepartmentIdentifier identifier);

	@Query(DEFAULT_SELECT + " where d.id = :id order by p.lastName")
	Streamable<TrackedCase> findByDepartmentIdOrderByLastNameAsc(DepartmentIdentifier id);

	Optional<TrackedCase> findByTrackedPerson(TrackedPerson person);

	@Query(DEFAULT_SELECT + " where p.id = :identifier")
	Optional<TrackedCase> findByTrackedPerson(TrackedPersonIdentifier identifier);

	@Query(DEFAULT_SELECT + " where p.account = :account")
	Optional<TrackedCase> findByAccount(Account account);

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
