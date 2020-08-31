package quarano.diary;

import quarano.diary.DiaryEntry.DiaryEntryIdentifier;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 */
public interface DiaryEntryRepository extends CrudRepository<DiaryEntry, DiaryEntryIdentifier> {

	default Diary findByTrackedPerson(TrackedPerson person) {
		return findByTrackedPersonId(person.getId());
	}

	Diary findByTrackedPersonId(TrackedPersonIdentifier id);

	@Query("select distinct c.trackedPerson.id from TrackedCase c " +
			"	where c.status = quarano.department.TrackedCase$Status.TRACKING" +
			"	and c.trackedPerson.id not in (" +
			"		select distinct d.trackedPersonId from DiaryEntry d where d.slot in :slots" +
			"	)")
	Streamable<TrackedPersonIdentifier> findMissingDiaryEntryPersons(List<Slot> slots);

	@Query("select count(de) from DiaryEntry de where de.trackedPerson = :person and de.metadata.lastModified > :limit")
	long countNewerDiaryEntries(TrackedPerson person, LocalDateTime limit);
}
