package quarano.sormas_integration.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Federico Grasso
 *
 * Contacts Synchronization Report Table repository
 */

public interface ContactsSyncReportRepository extends JpaRepository<ContactsSyncReport, UUID> {
    // Returns most recent report entity
    @Query(nativeQuery = true, value = "SELECT * FROM contacts_sync_report ORDER BY sync_date DESC LIMIT 1")
    List<ContactsSyncReport> getOrderBySyncDateDesc();
}
