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

@Service
@RequiredArgsConstructor
public class DiaryManagement {

	private final @NonNull DiaryEntryRepository diaries;
	private final @NonNull TrackedPersonRepository persons;

	/**
	 * @param entry
	 * @return
	 */
	@Transactional
	public DiaryEntry updateDiaryEntry(DiaryEntry entry) {

		var person = persons.findById(entry.getTrackedPersonId())
				.orElseThrow(() -> new IllegalStateException("No tracked person found for id " + entry.getTrackedPersonId()));

		var date = entry.getSlotDate();

		entry.getContacts().forEach(it -> person.reportContactWith(it, date));

		persons.save(person);

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
