package quarano.tracking;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryManagement {
	private final @NonNull DiaryEntryRepository diaries;
	private final @NonNull TrackedPersonRepository persons;

	public DiaryEntry updateDiaryEntry(DiaryEntry entry, TrackedPerson person) {
		DiaryEntry savedEntry = diaries.save(entry);
		savedEntry.toEncounters().forEach(person::registerEncounter);
		persons.save(person);
		return savedEntry;
	}
}
