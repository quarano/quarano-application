package quarano.diary;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.tracking.TrackedPersonDataInitializer;

@QuaranoIntegrationTest
@RequiredArgsConstructor
class DiaryEntryRepositoryIntegrationTest {

	private final DiaryEntryRepository diaries;

	@Test
	void testFindMissingDiaryEntryPersonsForLastOneSlot() {

		Slot now = Slot.now();

		var result = diaries.findMissingDiaryEntryPersons(List.of(now)).toList();

		assertThat(result).containsExactlyInAnyOrder(
				TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2,
				TrackedPersonDataInitializer.VALID_TRACKED_PERSON5_ID_DEP1,
				TrackedPersonDataInitializer.VALID_TRACKED_SEC1_ID_DEP1,
				TrackedPersonDataInitializer.VALID_TRACKED_PERSON4_ID_DEP1,
				TrackedPersonDataInitializer.INDEX_PERSON_DELETE1_DEP1,
				TrackedPersonDataInitializer.CONTACT_PERSON_DELETE1_DEP1);
	}

	@Test
	void testFindMissingDiaryEntryPersonsForLastTwoSlots() {

		Slot now = Slot.now();
		Slot previous = now.previous();

		var result = diaries.findMissingDiaryEntryPersons(List.of(now, previous)).toList();

		assertThat(result).containsExactlyInAnyOrder(
				TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2,
				TrackedPersonDataInitializer.VALID_TRACKED_SEC1_ID_DEP1,
				TrackedPersonDataInitializer.VALID_TRACKED_PERSON4_ID_DEP1,
				TrackedPersonDataInitializer.INDEX_PERSON_DELETE1_DEP1,
				TrackedPersonDataInitializer.CONTACT_PERSON_DELETE1_DEP1);
	}
}
