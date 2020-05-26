package quarano.diary;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.diary.DiaryEntryRepository;
import quarano.tracking.TrackedPersonDataInitializer;

import java.util.List;

import org.junit.jupiter.api.Test;

@QuaranoIntegrationTest
@RequiredArgsConstructor
class DiaryEntryRepositoryIntegrationTest {

	private final DiaryEntryRepository diaries;

	@Test
	void testFindMissingDiaryEntryPersonsForLastOneSlot() {

		Slot now = Slot.now();

		var result = diaries.findMissingDiaryEntryPersons(List.of(now)).toList();

		assertThat(result).containsExactlyInAnyOrder( //
				TrackedPersonDataInitializer.createSandra().getId(), //
				TrackedPersonDataInitializer.createNadine().getId(), //
				TrackedPersonDataInitializer.createSiggi().getId(), //
				TrackedPersonDataInitializer.createGustav().getId());
	}

	@Test
	void testFindMissingDiaryEntryPersonsForLastTwoSlots() {

		Slot now = Slot.now();
		Slot previous = now.previous();

		var result = diaries.findMissingDiaryEntryPersons(List.of(now, previous)).toList();

		assertThat(result).containsExactlyInAnyOrder( //
				TrackedPersonDataInitializer.createSandra().getId(), //
				TrackedPersonDataInitializer.createSiggi().getId(), //
				TrackedPersonDataInitializer.createGustav().getId());
	}
}
