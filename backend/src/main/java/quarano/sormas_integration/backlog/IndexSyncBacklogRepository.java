package quarano.sormas_integration.backlog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Federico Grasso
 */

/**
 * Index Backlog Table repository
 */

public interface IndexSyncBacklogRepository extends JpaRepository<IndexSyncBacklog, Long> {
    // Returns all modified or inserted records after specified date
    @Query("select distinct b.id from IndexSyncBacklog b where b.syncDate <= :syncDate")
    List<UUID> findBySyncDate(LocalDateTime syncDate);

    // Delete all entries related to specified UUID after specified date
    @Transactional
    @Modifying
    @Query("delete from IndexSyncBacklog b where b.syncDate <= :syncDate and b.id = :uuid")
    void deleteAfterSynchronization(UUID uuid, LocalDateTime syncDate);
}