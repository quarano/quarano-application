package quarano.actions;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.department.CaseType;
import quarano.department.TrackedCaseRepository;
import quarano.diary.Diary;
import quarano.diary.DiaryEntry;
import quarano.diary.DiaryEntryMissing;
import quarano.diary.DiaryManagement;
import quarano.diary.Slot;
import quarano.masterdata.Symptom;
import quarano.tracking.BodyTemperature;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
@QuaranoUnitTest
class DiaryEventListenerTest {

	@Mock
	ActionItemRepository items;
	@Mock
	DiaryManagement diaryManagement;
	@Mock
	AnomaliesProperties config;
	@Mock
	TrackedCaseRepository cases;
	@Mock
	MessageSourceAccessor messages;

	@Captor
	ArgumentCaptor<DiaryEntryActionItem> itemCaptor;

	DiaryEventListener listener;

	@BeforeEach
	void setup() {

		listener = new DiaryEventListener(config, items, diaryManagement, cases, messages);

		// stubbing just needed for the diary entry added tests
		lenient().when(config.getTemperatureThreshold(CaseType.CONTACT)).thenReturn(BodyTemperature.of(40.99F));
		lenient().when(config.getTemperatureThreshold(CaseType.INDEX)).thenReturn(BodyTemperature.of(42F));
	}

	@Test
	void testOnDiaryEntryAddedAndUpdatedAnOlderSlot() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var newerEntry = DiaryEntry.of(Slot.now(), person);
		var entry = DiaryEntry.of(Slot.now().previous(), person)
				.setBodyTemperature(BodyTemperature.of(41F));

		when(diaryManagement.findDiaryFor(person)).thenReturn(Diary.of(Streamable.of(newerEntry)));

		listener.handleDiaryEntryForBodyTemperature(entry);

		verify(items, times(0)).save(itemCaptor.capture());
	}

	@Test // CORE-222
	void testOnDiaryEntryAddedWithExceedingTemperature() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var entry = DiaryEntry.of(Slot.now(), person)
				.setBodyTemperature(BodyTemperature.of(41F));

		when(diaryManagement.findDiaryFor(person)).thenReturn(Diary.of(Streamable.empty()));
		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.INCREASED_TEMPERATURE))
				.thenReturn(ActionItems.empty());

		listener.handleDiaryEntryForBodyTemperature(entry);

		verify(items, times(1)).save(itemCaptor.capture());

		DiaryEntryActionItem item = itemCaptor.getValue();

		assertThat(item.getType()).isEqualTo(ActionItem.ItemType.MEDICAL_INCIDENT);

		Description description = item.getDescription();
		assertThat(description.getArguments()).isEqualTo(new Object[] { "41,0°C", "41,0°C" });
		assertThat(description.getCode()).isEqualTo(DescriptionCode.INCREASED_TEMPERATURE);
	}

	@Test // CORE-222
	void testOnDiaryEntryUpdatesWithExceedingTemperature() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());

		// first update with exceeding temperature
		var entry = DiaryEntry.of(Slot.now(), person)
				.setBodyTemperature(BodyTemperature.of(41F));

		when(diaryManagement.findDiaryFor(person)).thenReturn(Diary.of(Streamable.empty()));
		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.INCREASED_TEMPERATURE))
				.thenReturn(ActionItems.empty());

		listener.handleDiaryEntryForBodyTemperature(entry);

		verify(items, times(1)).save(itemCaptor.capture());

		DiaryEntryActionItem item = itemCaptor.getValue();

		assertThat(item.getType()).isEqualTo(ActionItem.ItemType.MEDICAL_INCIDENT);
		Description description = item.getDescription();

		assertThat(description.getArguments()).isEqualTo(new Object[] { "41,0°C", "41,0°C" });
		assertThat(description.getCode()).isEqualTo(DescriptionCode.INCREASED_TEMPERATURE);

		// second update with exceeding temperature
		entry = DiaryEntry.of(Slot.now(), person)
				.setBodyTemperature(BodyTemperature.of(42F));

		ActionItem actionItem = createMockedActionItem();
		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.INCREASED_TEMPERATURE))
				.thenReturn(ActionItems.of(actionItem));

		listener.handleDiaryEntryForBodyTemperature(entry);

		verify(actionItem, times(1)).resolve();
		itemCaptor = ArgumentCaptor.forClass(DiaryEntryActionItem.class);
		verify(items, times(3)).save(itemCaptor.capture());

		item = itemCaptor.getValue();
		description = item.getDescription();

		assertThat(item.getType()).isEqualTo(ActionItem.ItemType.MEDICAL_INCIDENT);
		assertThat(description.getArguments()).isEqualTo(new Object[] { "42,0°C", "41,0°C" });
		assertThat(description.getCode()).isEqualTo(DescriptionCode.INCREASED_TEMPERATURE);
	}

	@Test // CORE-222
	void testOnDiaryEntryAddedWithExceedingTemperatureResolved() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var entry = DiaryEntry.of(Slot.now(), person)
				.setBodyTemperature(BodyTemperature.of(39F));
		var actionItem = createMockedActionItem();

		when(diaryManagement.findDiaryFor(person)).thenReturn(Diary.of(Streamable.of(entry)));
		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.INCREASED_TEMPERATURE))
				.thenReturn(ActionItems.of(actionItem));

		listener.handleDiaryEntryForBodyTemperature(entry);

		verify(actionItem, times(1)).resolve();
		verify(items, times(1)).save(actionItem);
	}

	@Test // CORE-222
	void testOnDiaryEntryAddedWithCharacteristicSymptoms() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var symptom = createMockedSymptom();
		var entry = DiaryEntry.of(Slot.now(), person)
				.setSymptoms(List.of(symptom))
				.setBodyTemperature(BodyTemperature.of(36));

		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM))
				.thenReturn(ActionItems.empty());

		listener.handleDiaryEntryForCharacteristicSymptoms(entry);

		verify(items, times(1)).save(itemCaptor.capture());

		DiaryEntryActionItem item = itemCaptor.getValue();

		assertThat(item.getType()).isEqualTo(ActionItem.ItemType.MEDICAL_INCIDENT);
		assertThat(item.getDescription().getCode()).isEqualTo(DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM);
	}

	@Test // CORE-222
	void testOnDiaryEntryWithSymptomsNotOverwritingExistingIfNewerOrEquals() {

		// Updates equal slot
		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var symptom = createMockedSymptom();
		var actionItem = createMockedActionItem();
		var diaryEntry = DiaryEntry.of(Slot.now(), person)
				.setSymptoms(List.of(symptom))
				.setBodyTemperature(BodyTemperature.of(36));

		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM))
				.thenReturn(ActionItems.of(actionItem));
		when(actionItem.getEntry()).thenReturn(diaryEntry);

		listener.handleDiaryEntryForCharacteristicSymptoms(diaryEntry);

		verify(items, times(0)).save(any());

		// Addes newer slot
		when(actionItem.getEntry()).thenReturn(DiaryEntry.of(Slot.now().previous(), person));

		listener.handleDiaryEntryForCharacteristicSymptoms(diaryEntry);

		verify(items, times(0)).save(any());
	}

	@Test // CORE-222
	void testOnDiaryEntryWithSymptomsOverwritingExistingIfOlder() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var symptom = createMockedSymptom();
		var entry = DiaryEntry.of(Slot.now().previous(), person)
				.setSymptoms(List.of(symptom))
				.setBodyTemperature(BodyTemperature.of(36));
		var actionItem = createMockedActionItem();

		when(actionItem.getEntry()).thenReturn(DiaryEntry.of(Slot.now(), person));
		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM))
				.thenReturn(ActionItems.of(actionItem));

		// Updates older slot
		listener.handleDiaryEntryForCharacteristicSymptoms(entry);

		verify(items, times(2)).save(itemCaptor.capture());
		assertThat(itemCaptor.getAllValues()).contains(actionItem);

		// Adds older slot
		listener.handleDiaryEntryForCharacteristicSymptoms(entry);

		verify(items, times(4)).save(itemCaptor.capture());
		assertThat(itemCaptor.getAllValues()).contains(actionItem);
	}

	@Test // CORE-222
	void testOnDiaryEntryWithSymptomsForFirstActionItem() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var symptom = createMockedSymptom();
		var entry = DiaryEntry.of(Slot.now(), person)
				.setSymptoms(List.of(symptom))
				.setBodyTemperature(BodyTemperature.of(36));

		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM))
				.thenReturn(ActionItems.empty());

		// Updates older slot
		listener.handleDiaryEntryForCharacteristicSymptoms(entry);

		verify(items, times(1)).save(itemCaptor.capture());
		assertThat(itemCaptor.getValue().getEntry()).isEqualTo(entry);

		// Adds older slot
		listener.handleDiaryEntryForCharacteristicSymptoms(entry);

		verify(items, times(2)).save(itemCaptor.capture());
		assertThat(itemCaptor.getValue().getEntry()).isEqualTo(entry);
	}

	@Test // CORE-222
	void diaryEntryUpdatedNewWithoutSymptomsResolvesActionItemAndCreateNewOne() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var symptom = createMockedSymptom();
		var diaryEntry3 = DiaryEntry.of(Slot.now(), person).setSymptoms(List.of(symptom));
		var diaryEntry2 = DiaryEntry.of(Slot.now().previous(), person).setSymptoms(List.of(symptom));
		var diaryEntry1 = DiaryEntry.of(Slot.now().previous().previous(), person)//
				.setBodyTemperature(BodyTemperature.of(36));
		var actionItem = createMockedActionItem();

		when(actionItem.getEntry()).thenReturn(diaryEntry1);
		when(diaryManagement.findDiaryFor(person)).thenReturn(Diary.of(Streamable.of(diaryEntry3, diaryEntry2)));
		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM))
				.thenReturn(ActionItems.of(actionItem));

		listener.handleDiaryEntryForCharacteristicSymptoms(diaryEntry1);

		verify(items, times(2)).save(itemCaptor.capture());
		List<DiaryEntryActionItem> allValues = itemCaptor.getAllValues();

		assertThat(allValues.get(0)).isEqualTo(actionItem);
		assertThat(allValues.get(1).getEntry()).isEqualTo(diaryEntry2);
	}

	@Test
	void testOnDiaryEntryUpdatedAgainWithoutSymptomsDoNothing() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var diaryEntry1 = DiaryEntry.of(Slot.now().previous(), person);
		var diaryEntry2 = DiaryEntry.of(Slot.now(), person)
				.setSymptoms(List.of())
				.setBodyTemperature(BodyTemperature.of(36));
		var actionItem = createMockedActionItem();

		when(actionItem.getEntry()).thenReturn(diaryEntry1);
		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM))
				.thenReturn(ActionItems.of(actionItem));

		listener.handleDiaryEntryForCharacteristicSymptoms(diaryEntry2);

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

	@Test // CORE-593
	void testDifferentTemperatureThresholdsForIndexAndContact() {

		// for contact
		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var entry = DiaryEntry.of(Slot.now(), person)
				.setBodyTemperature(BodyTemperature.of(41F));

		when(diaryManagement.findDiaryFor(person)).thenReturn(Diary.of(Streamable.empty()));
		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.INCREASED_TEMPERATURE))
				.thenReturn(ActionItems.empty());
		when(cases.findTypeByTrackedPerson(person)).thenReturn(Optional.of(CaseType.CONTACT));

		listener.handleDiaryEntryForBodyTemperature(entry);

		verify(items, times(1)).save(itemCaptor.capture());

		var item = itemCaptor.getValue();

		assertThat(item.getType()).isEqualTo(ActionItem.ItemType.MEDICAL_INCIDENT);

		var description = item.getDescription();
		assertThat(description.getArguments()).isEqualTo(new Object[] { "41,0°C", "41,0°C" });
		assertThat(description.getCode()).isEqualTo(DescriptionCode.INCREASED_TEMPERATURE);

		// for index
		entry = DiaryEntry.of(Slot.now(), person)
				.setBodyTemperature(BodyTemperature.of(43F));

		when(cases.findTypeByTrackedPerson(person)).thenReturn(Optional.of(CaseType.INDEX));

		listener.handleDiaryEntryForBodyTemperature(entry);

		verify(items, times(1 + 1)).save(itemCaptor.capture());

		item = itemCaptor.getValue();

		assertThat(item.getType()).isEqualTo(ActionItem.ItemType.MEDICAL_INCIDENT);

		description = item.getDescription();
		assertThat(description.getArguments()).isEqualTo(new Object[] { "43,0°C", "42,0°C" });
		assertThat(description.getCode()).isEqualTo(DescriptionCode.INCREASED_TEMPERATURE);
	}

	@Test // CORE-593
	void testOnDiaryEntryAddedWithSuspiciousSymptoms() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var symptom = createMockedSymptom();
		var entry = DiaryEntry.of(Slot.now(), person)
				.setSymptoms(List.of(symptom))
				.setBodyTemperature(BodyTemperature.of(36));

		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.SUSPICIOUS_SYMPTOM))
				.thenReturn(ActionItems.empty());

		listener.handleDiaryEntryForSuspiciousSymptoms(entry);

		verify(items, times(1)).save(itemCaptor.capture());

		DiaryEntryActionItem item = itemCaptor.getValue();

		assertThat(item.getType()).isEqualTo(ActionItem.ItemType.MEDICAL_INCIDENT);
		assertThat(item.getDescription().getCode()).isEqualTo(DescriptionCode.SUSPICIOUS_SYMPTOM);
	}

	@Test // CORE-593
	void testOnDiaryEntryWithSuspiciousSymptomsNotOverwritingExistingIfNewerOrEquals() {

		// Updates equal slot
		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var symptom = createMockedSymptom();
		var actionItem = createMockedActionItem();
		var diaryEntry = DiaryEntry.of(Slot.now(), person)
				.setSymptoms(List.of(symptom))
				.setBodyTemperature(BodyTemperature.of(36));

		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.SUSPICIOUS_SYMPTOM))
				.thenReturn(ActionItems.of(actionItem));
		when(actionItem.getEntry()).thenReturn(diaryEntry);

		listener.handleDiaryEntryForSuspiciousSymptoms(diaryEntry);

		verify(items, times(0)).save(any());

		// Addes newer slot
		when(actionItem.getEntry()).thenReturn(DiaryEntry.of(Slot.now().previous(), person));

		listener.handleDiaryEntryForSuspiciousSymptoms(diaryEntry);

		verify(items, times(0)).save(any());
	}

	@Test // CORE-593
	void testOnDiaryEntryWithSuspiciousSymptomsOverwritingExistingIfOlder() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var symptom = createMockedSymptom();
		var entry = DiaryEntry.of(Slot.now().previous(), person)
				.setSymptoms(List.of(symptom))
				.setBodyTemperature(BodyTemperature.of(36));
		var actionItem = createMockedActionItem();

		when(actionItem.getEntry()).thenReturn(DiaryEntry.of(Slot.now(), person));
		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.SUSPICIOUS_SYMPTOM))
				.thenReturn(ActionItems.of(actionItem));

		listener.handleDiaryEntryForSuspiciousSymptoms(entry);

		verify(items, times(2)).save(itemCaptor.capture());
		assertThat(itemCaptor.getAllValues()).contains(actionItem);
	}

	@Test // CORE-593
	void testOnDiaryEntryWithSuspiciousSymptomsForFirstActionItem() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var symptom = createMockedSymptom();
		var entry = DiaryEntry.of(Slot.now(), person)
				.setSymptoms(List.of(symptom))
				.setBodyTemperature(BodyTemperature.of(36));

		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.SUSPICIOUS_SYMPTOM))
				.thenReturn(ActionItems.empty());

		listener.handleDiaryEntryForSuspiciousSymptoms(entry);

		verify(items, times(1)).save(itemCaptor.capture());
		assertThat(itemCaptor.getValue().getEntry()).isEqualTo(entry);
	}

	@Test // CORE-593
	void diaryEntryUpdatedNewWithoutSuspiciousSymptomsResolvesActionItemAndCreateNewOne() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var symptom = createMockedSymptom();
		var diaryEntry3 = DiaryEntry.of(Slot.now(), person).setSymptoms(List.of(symptom));
		var diaryEntry2 = DiaryEntry.of(Slot.now().previous(), person).setSymptoms(List.of(symptom));
		var diaryEntry1 = DiaryEntry.of(Slot.now().previous().previous(), person)//
				.setBodyTemperature(BodyTemperature.of(36));
		var actionItem = createMockedActionItem();

		when(actionItem.getEntry()).thenReturn(diaryEntry1);
		when(diaryManagement.findDiaryFor(person)).thenReturn(Diary.of(Streamable.of(diaryEntry3, diaryEntry2)));
		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.SUSPICIOUS_SYMPTOM))
				.thenReturn(ActionItems.of(actionItem));

		listener.handleDiaryEntryForSuspiciousSymptoms(diaryEntry1);

		verify(items, times(2)).save(itemCaptor.capture());
		List<DiaryEntryActionItem> allValues = itemCaptor.getAllValues();

		assertThat(allValues.get(0)).isEqualTo(actionItem);
		assertThat(allValues.get(1).getEntry()).isEqualTo(diaryEntry2);
	}

	@Test // CORE-593
	void testOnDiaryEntryUpdatedAgainWithoutSuspiciousSymptomsDoNothing() {

		var person = TrackedPersonIdentifier.of(UUID.randomUUID());
		var diaryEntry1 = DiaryEntry.of(Slot.now().previous(), person);
		var diaryEntry2 = DiaryEntry.of(Slot.now(), person)
				.setSymptoms(List.of())
				.setBodyTemperature(BodyTemperature.of(36));
		var actionItem = createMockedActionItem();

		when(actionItem.getEntry()).thenReturn(diaryEntry1);
		when(items.findUnresolvedByDescriptionCode(person, DescriptionCode.SUSPICIOUS_SYMPTOM))
				.thenReturn(ActionItems.of(actionItem));

		listener.handleDiaryEntryForSuspiciousSymptoms(diaryEntry2);

		verify(items, times(0)).save(any());
	}

	private static DiaryEntryActionItem createMockedActionItem() {

		var actionItem = mock(DiaryEntryActionItem.class);

		lenient().when(actionItem.isUnresolved()).thenReturn(true);
		lenient().when(actionItem.resolve()).thenReturn(actionItem);

		return actionItem;
	}

	Symptom createMockedSymptom() {

		var symptom = mock(Symptom.class);

		lenient().when(symptom.isCharacteristic()).thenReturn(true);
		lenient().when(symptom.isSuspiciousAtContact()).thenReturn(true);
		lenient().when(symptom.getId()).thenReturn(UUID.fromString("8687c622-d223-42fb-a93f-7a1c3677a4a6"));
		lenient().when(messages.getMessage("symptom.8687c622-d223-42fb-a93f-7a1c3677a4a6"))
				.thenReturn("Gefühl zu wenig / keine Luft zu bekommen");

		return symptom;
	}
}
