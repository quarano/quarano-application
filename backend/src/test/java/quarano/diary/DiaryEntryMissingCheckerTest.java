package quarano.diary;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Streamable;

@QuaranoUnitTest
class DiaryEntryMissingCheckerTest {

	@Mock DiaryManagement diaries;
	@Mock ApplicationEventPublisher publisher;
	@Mock DiaryProperties properties;

	private DiaryEntryMissingChecker checker;

	@BeforeEach
	void setup() {
		checker = new DiaryEntryMissingChecker(diaries, publisher, properties);
	}

	@Test
	void testCollectAndPublishMissingEntryPersons() {
		var expectedPersons = personIdentifiers();

		when(properties.getToleratedSlotCount()).thenReturn(4);
		ArgumentCaptor<List<Slot>> slotsCaptor = ArgumentCaptor.forClass(List.class);
		when(diaries.findMissingDiaryEntryPersons(slotsCaptor.capture())).thenReturn(Streamable.of(expectedPersons));

		checker.collectAndPublishMissingEntryPersons();

		List<Slot> slots = slotsCaptor.getValue();
		assertThat(slots).hasSize(5);

		ArgumentCaptor<DiaryEntryMissing> missingCaptor = ArgumentCaptor.forClass(DiaryEntryMissing.class);
		verify(publisher, times(expectedPersons.size())).publishEvent(missingCaptor.capture());

		List<DiaryEntryMissing> diaryEntryMissings = missingCaptor.getAllValues();
		assertThat(diaryEntryMissings).hasSize(expectedPersons.size());
		assertThat(diaryEntryMissings).containsExactly(
				expectedPersons.stream().map(person -> DiaryEntryMissing.of(slots, person)).toArray(DiaryEntryMissing[]::new));

	}

	private Collection<TrackedPersonIdentifier> personIdentifiers() {
		return List.of(TrackedPersonIdentifier.of(UUID.randomUUID()), TrackedPersonIdentifier.of(UUID.randomUUID()),
				TrackedPersonIdentifier.of(UUID.randomUUID()));
	}

}
