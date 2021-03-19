package quarano.sormas_integration.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.FlushMode;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;
import quarano.tracking.TrackedPerson;

import java.util.Date;

/**
 * @author Federico Grasso
 *
 * Insert listener
 */

@Slf4j
@RequiredArgsConstructor
@Component
class InsertListener implements PostInsertEventListener {

    @Override
    public void onPostInsert(PostInsertEvent postInsertEvent) {
        if(postInsertEvent.getEntity() instanceof TrackedPerson){

            log.debug("Trigger invoked...");

            postInsertEvent.getSession().createNativeQuery(
                    "INSERT INTO index_synch_backlog (id, sync_date) " +
                            "VALUES (:id, :sync_date)")
                    .setParameter("id", ((TrackedPerson) postInsertEvent.getEntity())
                            .getId()
                            .toString())
                    .setParameter("sync_date", new Date())
                    .setFlushMode(FlushMode.MANUAL)
                    .executeUpdate();

            postInsertEvent.getSession().createNativeQuery(
                    "INSERT INTO contacts_synch_backlog (id, sync_date) " +
                            "VALUES (:id, :sync_date)")
                    .setParameter("id", ((TrackedPerson) postInsertEvent.getEntity())
                            .getId()
                            .toString())
                    .setParameter("sync_date", new Date())
                    .setFlushMode(FlushMode.MANUAL)
                    .executeUpdate();
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return false;
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return false;
    }
}
