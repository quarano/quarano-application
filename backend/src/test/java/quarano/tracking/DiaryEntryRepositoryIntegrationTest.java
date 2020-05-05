package quarano.tracking;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.List;
import org.junit.jupiter.api.Test;

@QuaranoIntegrationTest
@RequiredArgsConstructor
class DiaryEntryRepositoryIntegrationTest {

	private final DiaryEntryRepository diaries;

	@Test
	void testFindMissingDiaryEntryPersonsForLastOneSlot() {

		Slot now = Slot.now();

		var diaryEntries = diaries.findMissingDiaryEntryPersons(List.of(now));

		List<TrackedPersonIdentifier> personIds = diaryEntries.toList();
		assertThat(personIds).containsExactly(TrackedPersonDataInitializer.createSandra().getId(),
				TrackedPersonDataInitializer.createNadine().getId(), TrackedPersonDataInitializer.createSiggi().getId(),
				TrackedPersonDataInitializer.createGustav().getId());
	}

	@Test
	void testFindMissingDiaryEntryPersonsForLastTwoSlots() {

		Slot now = Slot.now();
		Slot previous = now.previous();

		var diaryEntries = diaries.findMissingDiaryEntryPersons(List.of(now, previous));

		List<TrackedPersonIdentifier> personIds = diaryEntries.toList();
		assertThat(personIds).containsExactly(TrackedPersonDataInitializer.createSandra().getId(),
				TrackedPersonDataInitializer.createSiggi().getId(), TrackedPersonDataInitializer.createGustav().getId());
	}
}
