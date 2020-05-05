package quarano.tracking;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
@QuaranoIntegrationTest
@RequiredArgsConstructor
class DiaryEntryRepositoryIntegrationTest {
	private final DiaryEntryRepository diaries;

	@Test
	void testFindMissingDiaryEntryPersonsForTwoSlots() {
		Slot now = Slot.now();
		Slot previous = now.previous();

		var diaryEntries = diaries.findMissingDiaryEntryPersons(List.of(now, previous));

		int size = diaryEntries.toList().size();
		assertThat(size).isEqualTo(13);
	}

	@Test
	void testFindMissingDiaryEntryPersonsForThreeSlots() {
		Slot now = Slot.now();
		Slot previous = now.previous();

		var diaryEntries = diaries.findMissingDiaryEntryPersons(List.of(now, previous, previous.previous()));

		int size = diaryEntries.toList().size();
		assertThat(size).isEqualTo(10);
	}
}
