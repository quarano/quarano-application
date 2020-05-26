package quarano.department;

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
public interface TrackedCaseRepository extends QuaranoRepository<TrackedCase, TrackedCaseIdentifier> {

	Streamable<TrackedCase> findByDepartmentId(DepartmentIdentifier id);

	@Query("select c from TrackedCase c JOIN c.trackedPerson p WHERE c.department.id = :id ORDER BY p.lastName")
	Streamable<TrackedCase> findByDepartmentIdOrderByLastNameAsc(DepartmentIdentifier id);

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
}
