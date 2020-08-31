package quarano.actions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.data.util.Streamable;

import quarano.QuaranoUnitTest;
import quarano.department.CaseType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.diary.*;
import quarano.reference.Symptom;
import quarano.tracking.BodyTemperature;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;

/**
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
@QuaranoUnitTest
class DiaryEventListenerTest {

	@Mock ActionItemRepository items;
	@Mock DiaryManagement diaryManagement;
	@Mock AnomaliesProperties config;
	@Mock TrackedCaseRepository cases;
	@Mock TrackedPersonRepository persons;

	@Captor ArgumentCaptor<DiaryEntryActionItem> itemCaptor;

	DiaryEventListener listener;

	@BeforeEach
	void setup() {

		listener = new DiaryEventListener(config, items, diaryManagement, cases, persons);

		// stubbing just needed for the diary entry added tests
		lenient().when(config.getTemperatureThreshold()).thenReturn(BodyTemperature.of(40.99F));
	}

	@Test
	void testOnDiaryEntryAddedAndUpdatedAnOlderSlot() {

		var person = preparePerson();
		var newerEntry = DiaryEntry.of(Slot.now(), person);
		var entry = DiaryEntry.of(Slot.now().previous(), person)
				.setBodyTemperature(BodyTemperature.of(41F));

		when(diaryManagement.findDiaryFor(person.getId())).thenReturn(Diary.of(Streamable.of(newerEntry)));

		listener.handleDiaryEntryForBodyTemperature(entry);

		verify(items, times(0)).save(itemCaptor.capture());
	}


	@Test // CORE-222
	void testOnDiaryEntryAddedWithExceedingTemperature() {

		var person = preparePerson();

		person.getTrackedCases().add(new TrackedCase(person, CaseType.INDEX, null));
		var entry = DiaryEntry.of(Slot.now(), person)
				.setBodyTemperature(BodyTemperature.of(41F));

		when(diaryManagement.findDiaryFor(person.getId())).thenReturn(Diary.of(Streamable.<DiaryEntry> empty()));
		when(items.findUnresolvedByDescriptionCode(person.getId(), DescriptionCode.INCREASED_TEMPERATURE))
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

		var person = preparePerson();
		// first update with exceeding temperature
		var entry = DiaryEntry.of(Slot.now(), person)
				.setBodyTemperature(BodyTemperature.of(41F));

		when(diaryManagement.findDiaryFor(person.getId())).thenReturn(Diary.of(Streamable.<DiaryEntry> empty()));
		when(items.findUnresolvedByDescriptionCode(person.getId(), DescriptionCode.INCREASED_TEMPERATURE))
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
		when(items.findUnresolvedByDescriptionCode(person.getId(), DescriptionCode.INCREASED_TEMPERATURE))
				.thenReturn(ActionItems.of(actionItem));

		listener.handleDiaryEntryForBodyTemperature(entry);

		verify(actionItem, times(1)).resolve();
		itemCaptor = ArgumentCaptor.forClass(DiaryEntryActionItem.class);
		verify(items, times(3)).save(itemCaptor.capture());

		item = itemCaptor.getValue();

		assertThat(item.getType()).isEqualTo(ActionItem.ItemType.MEDICAL_INCIDENT);
		assertThat(item.getDescription().getArguments()).isEqualTo(new Object[] { "42,0°C", "41,0°C" });
		assertThat(item.getDescription().getCode()).isEqualTo(DescriptionCode.INCREASED_TEMPERATURE);
	}

	@Test // CORE-222
	void testOnDiaryEntryAddedWithExceedingTemperatureResolved() {

		var person = preparePerson();
		var entry = DiaryEntry.of(Slot.now(), person)
				.setBodyTemperature(BodyTemperature.of(39F));
		var actionItem = createMockedActionItem();

		when(diaryManagement.findDiaryFor(person.getId())).thenReturn(Diary.of(Streamable.of(entry)));
		when(items.findUnresolvedByDescriptionCode(person.getId(), DescriptionCode.INCREASED_TEMPERATURE))
				.thenReturn(ActionItems.of(actionItem));

		listener.handleDiaryEntryForBodyTemperature(entry);

		verify(actionItem, times(1)).resolve();
		verify(items, times(1)).save(actionItem);
	}

	@Test // CORE-222
	void testOnDiaryEntryAddedWithCharacteristicSymptoms() {

		var person = preparePerson();
		var symptom = mock(Symptom.class);
		var entry = DiaryEntry.of(Slot.now(), person)
				.setSymptoms(List.of(symptom))
				.setBodyTemperature(BodyTemperature.of(36));

		when(symptom.isCharacteristic()).thenReturn(true);
		when(items.findUnresolvedByDescriptionCode(person.getId(), DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM))
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
		var person = preparePerson();
		var symptom = mock(Symptom.class);
		var actionItem = createMockedActionItem();
		var diaryEntry = DiaryEntry.of(Slot.now(), person)
				.setSymptoms(List.of(symptom))
				.setBodyTemperature(BodyTemperature.of(36));

		when(symptom.isCharacteristic()).thenReturn(true);

		when(items.findUnresolvedByDescriptionCode(person.getId(), DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM))
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

		var person = preparePerson();
		person.getTrackedCases().add(new TrackedCase(person, CaseType.INDEX, null));
		var symptom = mock(Symptom.class);
		var entry = DiaryEntry.of(Slot.now().previous(), person)
				.setSymptoms(List.of(symptom))
				.setBodyTemperature(BodyTemperature.of(36));
		var actionItem = createMockedActionItem();

		when(symptom.isCharacteristic()).thenReturn(true);
		when(actionItem.getEntry()).thenReturn(DiaryEntry.of(Slot.now(), person));
		when(items.findUnresolvedByDescriptionCode(person.getId(), DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM))
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

		var person = preparePerson();
		var symptom = mock(Symptom.class);
		var entry = DiaryEntry.of(Slot.now(), person)
				.setSymptoms(List.of(symptom))
				.setBodyTemperature(BodyTemperature.of(36));

		when(symptom.isCharacteristic()).thenReturn(true);
		when(items.findUnresolvedByDescriptionCode(person.getId(), DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM))
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

		var person = preparePerson();
		var symptom = mock(Symptom.class);
		var diaryEntry3 = DiaryEntry.of(Slot.now(), person).setSymptoms(List.of(symptom));
		var diaryEntry2 = DiaryEntry.of(Slot.now().previous(), person).setSymptoms(List.of(symptom));
		var diaryEntry1 = DiaryEntry.of(Slot.now().previous().previous(), person)//
				.setBodyTemperature(BodyTemperature.of(36));
		var actionItem = createMockedActionItem();

		when(symptom.isCharacteristic()).thenReturn(true);
		when(actionItem.getEntry()).thenReturn(diaryEntry1);
		when(diaryManagement.findDiaryFor(person)).thenReturn(Diary.of(Streamable.of(diaryEntry3, diaryEntry2)));
		when(items.findUnresolvedByDescriptionCode(person.getId(), DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM))
				.thenReturn(ActionItems.of(actionItem));

		listener.handleDiaryEntryForCharacteristicSymptoms(diaryEntry1);

		verify(items, times(2)).save(itemCaptor.capture());
		List<DiaryEntryActionItem> allValues = itemCaptor.getAllValues();

		assertThat(allValues.get(0)).isEqualTo(actionItem);
		assertThat(allValues.get(1).getEntry()).isEqualTo(diaryEntry2);
	}

	@Test
	void testOnDiaryEntryUpdatedAgainWithoutSymptomsDoNothing() {

		var person = preparePerson();
		var diaryEntry1 = DiaryEntry.of(Slot.now().previous(), person);
		var diaryEntry2 = DiaryEntry.of(Slot.now(), person)
				.setSymptoms(List.of())
				.setBodyTemperature(BodyTemperature.of(36));
		var actionItem = createMockedActionItem();

		when(actionItem.getEntry()).thenReturn(diaryEntry1);
		when(items.findUnresolvedByDescriptionCode(person.getId(), DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM))
				.thenReturn(ActionItems.of(actionItem));

		listener.handleDiaryEntryForCharacteristicSymptoms(diaryEntry2);

		verify(items, times(0)).save(any());
	}

	@Test
	void testOnDiaryEntryMissing() {

		var person = preparePerson();
		var optionalPerson = Optional.of(person);
		var personId = person.getId();
		var slots = List.of(Slot.now(), Slot.now().previous());
		var event = DiaryEntryMissing.of(slots, personId);

		when(items.findDiaryEntryMissingActionItemsFor(personId, slots.get(0))).thenReturn(ActionItems.empty());
		when(items.findDiaryEntryMissingActionItemsFor(personId, slots.get(1)))
				.thenReturn(ActionItems.of(mock(DiaryEntryMissingActionItem.class)));
		when(persons.findById(personId)).thenReturn(optionalPerson);


		listener.on(event);

		var itemCaptor = ArgumentCaptor.forClass(DiaryEntryMissingActionItem.class);

		verify(items).save(itemCaptor.capture());

		assertThat(itemCaptor.getAllValues()).hasSize(1);
		assertThat(itemCaptor.getValue().getSlot()).isEqualTo(slots.get(0));
		assertThat(itemCaptor.getValue().getDescription().getCode()).isEqualTo(DescriptionCode.DIARY_ENTRY_MISSING);
	}

	@Test
	void testNoActionItemForIndexPerson() {

		var now = Slot.now();
		var person = preparePerson();
		var entry = mock(DiaryEntry.class);
		var event = DiaryEntry.DiaryEntryAdded.of(entry);
		var trackedCase = mock(TrackedCase.class);

		when(entry.getTrackedPersonId()).thenReturn(person.getId());
		lenient().when(entry.getSlot()).thenReturn(now);
		lenient().when(entry.getBodyTemperature()).thenReturn(BodyTemperature.of(42.0f)); // body temp well above conf threshold
		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));
		when(trackedCase.isIndexCase()).thenReturn(true);
		when(items.findDiaryEntryMissingActionItemsFor(person.getId(), now)).thenReturn(ActionItems.empty());

		listener.on(event);

		verify(items, times(0)).save(itemCaptor.capture());
	}

	private static DiaryEntryActionItem createMockedActionItem() {

		var actionItem = mock(DiaryEntryActionItem.class);

		lenient().when(actionItem.isUnresolved()).thenReturn(true);
		lenient().when(actionItem.resolve()).thenReturn(actionItem);

		return actionItem;
	}

	private TrackedPerson preparePerson() {
		var person = new TrackedPerson("John", "Smith");
		person.getTrackedCases().add(new TrackedCase(person, CaseType.INDEX, null));
		return person;
	}
}
