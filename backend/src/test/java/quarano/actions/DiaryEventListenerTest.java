package quarano.actions;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.reference.Symptom;
import quarano.tracking.BodyTemperature;
import quarano.tracking.DiaryEntry;
import quarano.tracking.DiaryEntryMissing;
import quarano.tracking.Slot;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

@QuaranoUnitTest
class DiaryEventListenerTest {

	@Mock ActionItemRepository items;
	@Mock AnomaliesProperties config;

	DiaryEventListener listener;

	@BeforeEach
	void setup() {

		listener = new DiaryEventListener(config, items);

		// stubbing just needed for the diary entry added tests
		lenient().when(config.getTemperatureThreshold()).thenReturn(BodyTemperature.of(40.99F));
	}

	@Test
	void testOnDiaryEntryAddedWithExceedingTemperature() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var entry = DiaryEntry.DiaryEntryAdded.of(DiaryEntry.of(Slot.now(), person) //
				.setBodyTemperature(BodyTemperature.of(41F)));

		listener.onDiaryEntryAddedForBodyTemperature(entry);

		var itemCaptor = ArgumentCaptor.forClass(DiaryEntryActionItem.class);
		verify(items, times(1)).save(itemCaptor.capture());

		DiaryEntryActionItem item = itemCaptor.getValue();

		assertThat(item.getType()).isEqualTo(ActionItem.ItemType.MEDICAL_INCIDENT);
		assertThat(item.getDescription().getArguments()).isEqualTo(new Object[] { "41,0°C", "41,0°C" });
		assertThat(item.getDescription().getCode()).isEqualTo(DescriptionCode.INCREASED_TEMPERATURE);
	}

	@Test
	void testOnDiaryEntryAddedWithExceedingTemperatureResolved() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var event = DiaryEntry.DiaryEntryAdded.of(DiaryEntry.of(Slot.now(), person) //
				.setBodyTemperature(BodyTemperature.of(39F)));

		var actionItem = mock(ActionItem.class);
		when(actionItem.isUnresolved()).thenReturn(true);
		when(actionItem.resolve()).thenReturn(actionItem);

		when(items.findByDescriptionCode(person, DescriptionCode.INCREASED_TEMPERATURE))
				.thenReturn(ActionItems.of(actionItem));

		listener.onDiaryEntryAddedForBodyTemperature(event);

		verify(actionItem, times(1)).resolve();
		verify(items, times(1)).save(actionItem);
	}

	@Test
	void testOnDiaryEntryAddedWithCharacteristicSymptoms() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());

		var symptom = mock(Symptom.class);
		when(symptom.isCharacteristic()).thenReturn(true);

		var event = DiaryEntry.DiaryEntryAdded.of(DiaryEntry.of(Slot.now(), person) //
				.setSymptoms(List.of(symptom)) //
				.setBodyTemperature(BodyTemperature.of(36)));

		when(items.findByDescriptionCode(person, DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM))
				.thenReturn(ActionItems.empty());

		listener.onDiaryEntryAddedForCharacteristicSymptoms(event);

		var itemCaptor = ArgumentCaptor.forClass(DiaryEntryActionItem.class);
		verify(items, times(1)).save(itemCaptor.capture());
		DiaryEntryActionItem item = itemCaptor.getValue();
		assertThat(item.getType()).isEqualTo(ActionItem.ItemType.MEDICAL_INCIDENT);
		assertThat(item.getDescription().getCode()).isEqualTo(DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM);
	}

	@Test
	void testOnDiaryEntryAddedWithCharacteristicSymptomsNotOverwritingExisting() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var symptom = mock(Symptom.class);
		var event = DiaryEntry.DiaryEntryAdded.of(DiaryEntry.of(Slot.now(), person) //
				.setSymptoms(List.of(symptom)) //
				.setBodyTemperature(BodyTemperature.of(36)));

		when(symptom.isCharacteristic()).thenReturn(true);
		when(items.findByDescriptionCode(person, DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM))
				.thenReturn(ActionItems.of(mock(DiaryEntryActionItem.class)));

		listener.onDiaryEntryAddedForCharacteristicSymptoms(event);

		verify(items, times(0)).save(any());
	}

	@Test
	void testOnDiaryEntryMissing() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var slots = List.of(Slot.now(), Slot.now().previous());
		var event = DiaryEntryMissing.of(slots, person);

		when(items.findDiaryEntryMissingActionItemsFor(person, slots.get(0))).thenReturn(ActionItems.empty());
		when(items.findDiaryEntryMissingActionItemsFor(person, slots.get(1)))
				.thenReturn(ActionItems.of(mock(DiaryEntryMissingActionItem.class)));

		listener.on(event);

		var itemCaptor = ArgumentCaptor.forClass(DiaryEntryMissingActionItem.class);

		verify(items).save(itemCaptor.capture());

		assertThat(itemCaptor.getAllValues()).hasSize(1);
		assertThat(itemCaptor.getValue().getSlot()).isEqualTo(slots.get(0));
		assertThat(itemCaptor.getValue().getDescription().getCode()).isEqualTo(DescriptionCode.DIARY_ENTRY_MISSING);
	}
}
