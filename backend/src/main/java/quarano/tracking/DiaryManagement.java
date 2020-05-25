package quarano.tracking;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.List;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
public class DiaryManagement {

	private final @NonNull DiaryEntryRepository diaries;
	private final @NonNull TrackedPersonRepository persons;

	public DiaryEntry updateDiaryEntry(DiaryEntry entry, TrackedPerson person) {

		entry.toEncounters().forEach(person::registerEncounter);

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
