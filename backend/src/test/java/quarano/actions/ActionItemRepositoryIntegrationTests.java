package quarano.actions;

import static org.assertj.core.api.Assertions.*;
import static quarano.actions.ActionItemRepositoryIntegrationTests.MatchesItemCondition.*;
import static quarano.tracking.TrackedPersonDataInitializer.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.actions.ActionItem.ItemType;
import quarano.department.TrackedCaseDataInitializer;
import quarano.department.TrackedCaseRepository;
import quarano.diary.DiaryEntry;
import quarano.diary.DiaryManagement;
import quarano.diary.Slot;
import quarano.tracking.BodyTemperature;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;

import java.time.LocalDateTime;

import org.assertj.core.api.Condition;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class ActionItemRepositoryIntegrationTests {

	private final ActionItemRepository repository;
	private final TrackedPersonRepository persons;
	private final TrackedCaseRepository cases;
	private final DiaryManagement diaries;

	@Test
	void persistsDescriptionArguments() {

		var person = persons.findRequiredById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2);
		var entry = diaries.updateDiaryEntry(DiaryEntry.of(Slot.of(LocalDateTime.now()), person)
				.setBodyTemperature(BodyTemperature.of(41.0f)));

		var item = new DiaryEntryActionItem(person.getId(), entry, ItemType.MEDICAL_INCIDENT,
				Description.of(DescriptionCode.INCREASED_TEMPERATURE, BodyTemperature.of(36.0f), BodyTemperature.of(40.0f)));

		item = repository.save(item);

		assertThat(repository.findById(item.getId())).hasValueSatisfying(it -> {
			assertThat(it.getDescription().getArguments()).isNotEmpty();
		});
	}

	@Test // CORE-162
	void testFindUnresolvedByPerson() {

		var person = VALID_TRACKED_PERSON4_ID_DEP1;
		repository.findByDescriptionCode(person, DescriptionCode.INCREASED_TEMPERATURE)
				.resolveAutomatically(repository::save);

		assertThat(repository.findByTrackedPerson(person).stream())
				.has(itemMatching(DescriptionCode.INCREASED_TEMPERATURE, true), Index.atIndex(0));

		assertThat(repository.findUnresolvedByTrackedPerson(person).stream())
				.hasSize(0);
	}

	@Test // For performance checks
	void loadsUnresolvedActionItemsForCase() {

		var result = cases.findById(TrackedCaseDataInitializer.TRACKED_CASE_TANJA).stream()
				.flatMap(it -> repository.findUnresolvedByActiveCase(it).stream());

		assertThat(result).isNotEmpty();
	}

	@RequiredArgsConstructor(staticName = "itemMatching")
	static class MatchesItemCondition extends Condition<ActionItem> {

		private final DescriptionCode descriptionCode;
		private final boolean resolved;

		/*
		 * (non-Javadoc)
		 * @see org.assertj.core.api.Condition#matches(java.lang.Object)
		 */
		@Override
		public boolean matches(ActionItem item) {
			return item.getDescription().getCode().equals(descriptionCode)
					&& item.isResolved() == resolved;
		}
	}
}
