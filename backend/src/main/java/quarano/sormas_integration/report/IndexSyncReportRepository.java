package quarano.sormas_integration.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Federico Grasso
 *
 * Index Synchronization Report Table repository
 */

public interface IndexSyncReportRepository extends JpaRepository<IndexSyncReport, UUID> {
    // Return most recent report entity
    @Query(nativeQuery = true, value = "SELECT * FROM index_sync_report ORDER BY sync_date DESC LIMIT 1")
    List<IndexSyncReport> getOrderBySyncDateDesc();

    // Get most recent successful report entity
    @Query(nativeQuery = true, value = "SELECT * FROM index_sync_report WHERE status = 'SUCCESS' ORDER BY sync_date DESC LIMIT 1")
    List<IndexSyncReport> getSuccessfulOrderBySyncDateDesc();
}
