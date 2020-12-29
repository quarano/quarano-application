package quarano.diary;

import quarano.diary.DiaryEntry.DiaryEntryIdentifier;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 */
public interface DiaryEntryRepository extends CrudRepository<DiaryEntry, DiaryEntryIdentifier> {

	/**
	 * We can only prefetch one of the related collections during a query. We resolve the symptoms with the first query as
	 * it's more likely to have symptoms associated with the entry than it is to have contacts as the tracked person is
	 * supposed to quarantine in the first place.
	 */
	static final String DEFAULT_SELECT = "select distinct e from DiaryEntry e" //
			+ " left join fetch e.symptoms";

	default Diary findByTrackedPerson(TrackedPerson person) {
		return findByTrackedPersonId(person.getId());
	}

	@Query(DEFAULT_SELECT + " where e.trackedPersonId = :id")
	Diary findByTrackedPersonId(TrackedPersonIdentifier id);

	@Query("select distinct c.trackedPerson.id from TrackedCase c " +
			"	where c.status = quarano.department.TrackedCase$Status.TRACKING" +
			"	and c.trackedPerson.id not in (" +
			"		select distinct d.trackedPersonId from DiaryEntry d where d.slot in :slots" +
			"	)")
	Streamable<TrackedPersonIdentifier> findMissingDiaryEntryPersons(List<Slot> slots);
}
