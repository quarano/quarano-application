package quarano.maintenance;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import quarano.core.QuaranoRepository;
import quarano.tracking.TrackedPerson;

public interface TrackedCaseRepositoryHelper extends QuaranoRepository<TrackedPerson, TrackedPerson.TrackedPersonIdentifier> {

    @Modifying
    @Query("update TrackedPerson p set p.metadata.lastModified=:date where p.id=:id")
    void manipulatePersonDate(LocalDateTime date, TrackedPerson.TrackedPersonIdentifier id);

    @Modifying
    @Query("update DiaryEntry d set d.metadata.lastModified=:date where d.trackedPersonId = :id")
    void manipulateDiaryDate(LocalDateTime date, TrackedPerson.TrackedPersonIdentifier id);
}
