package quarano.sormas_integration.listeners;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;
import quarano.sormas_integration.backlog.ContactsSyncBacklogRepository;
import quarano.sormas_integration.backlog.ContactsSynchBacklog;
import quarano.sormas_integration.backlog.IndexSyncBacklogRepository;
import quarano.sormas_integration.backlog.IndexSynchBacklog;
import quarano.tracking.TrackedPerson;

import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
class UpdateListener implements PostUpdateEventListener {

    private final @NonNull IndexSyncBacklogRepository indexBacklog;
    private final @NonNull ContactsSyncBacklogRepository contactsBacklog;

    @Override
    public void onPostUpdate(PostUpdateEvent postUpdateEvent) {
        if(postUpdateEvent.getEntity() instanceof TrackedPerson){

            log.debug("Trigger invoked...");

            IndexSynchBacklog newIndexEntry = new IndexSynchBacklog(
                    UUID.fromString(
                            ((TrackedPerson) postUpdateEvent.getEntity())
                                    .getId()
                                    .toString()
                    ),
                    new Date()
            );

            ContactsSynchBacklog newContactEntry = new ContactsSynchBacklog(
                    UUID.fromString(
                            ((TrackedPerson) postUpdateEvent.getEntity())
                                    .getId()
                                    .toString()
                    ),
                    new Date()
            );

            indexBacklog.save(newIndexEntry);
            contactsBacklog.save(newContactEntry);
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