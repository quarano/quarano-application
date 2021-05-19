package quarano.sormas_integration.lookup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import quarano.tracking.TrackedPerson;
import java.util.Optional;

public interface SormasLookupRepository extends JpaRepository<SormasLookup, String> {
    @Query("select p from SormasLookup p where p.personId = :personId")
    Optional<SormasLookup> findByPerson(String personId);
}