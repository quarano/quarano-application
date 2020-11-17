package quarano.diary;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;
import quarano.tracking.TrackedPersonRepository;

import java.util.List;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class DiaryManagement {

	private final @NonNull DiaryEntryRepository diaries;
	private final @NonNull TrackedPersonRepository people;

	/**
	 * @param entry must not be {@literal null}.
	 * @return
	 */
	@Transactional
	public DiaryEntry updateDiaryEntry(DiaryEntry entry) {

		Assert.notNull(entry, "Diary entry must not be null!");

		var person = people.findRequiredById(entry.getTrackedPersonId());

		entry.getContacts()
				.forEach(it -> person.reportContactWith(it, entry.getSlotDate()));

		people.save(person);

		return diaries.save(entry);
	}

	public Diary findDiaryFor(TrackedPerson person) {
		return diaries.findByTrackedPerson(person);
	}

	public Diary findDiaryFor(TrackedPersonIdentifier person) {
		return diaries.findByTrackedPersonId(person);
	}

	public Streamable<TrackedPersonIdentifier> findMissingDiaryEntryPersons(List<Slot> slots) {
		return diaries.findMissingDiaryEntryPersons(slots);
	}
}
