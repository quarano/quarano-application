/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.actions;

import static org.assertj.core.api.Assertions.assertThat;
import static quarano.tracking.TrackedPersonDataInitializer.VALID_TRACKED_PERSON4_ID_DEP1;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.actions.ActionItem.ItemType;
import quarano.tracking.BodyTemperature;
import quarano.tracking.DiaryEntry;
import quarano.tracking.DiaryEntryRepository;
import quarano.tracking.Slot;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;

import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import org.assertj.core.api.HamcrestCondition;
import org.assertj.core.data.Index;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class ActionItemRepositoryIntegrationTests {

	private final ActionItemRepository repository;
	private final TrackedPersonRepository persons;
	private final DiaryEntryRepository entries;
	private final EntityManager em;

	@Test
	void persistsDescriptionArguments() {

		var person = persons.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2).orElseThrow();
		var entry = DiaryEntry.of(Slot.of(LocalDateTime.now()), person) //
				.setBodyTemperature(BodyTemperature.of(41.0f));

		entries.save(entry);

		var item = new DiaryEntryActionItem(person.getId(), entry, ItemType.MEDICAL_INCIDENT,
				Description.of(DescriptionCode.INCREASED_TEMPERATURE, BodyTemperature.of(36.0f), BodyTemperature.of(40.0f)));

		item = repository.save(item);

		em.flush();
		em.clear();

		assertThat(repository.findById(item.getId())).hasValueSatisfying(it -> {
			assertThat(it.getDescription().getArguments()).isNotEmpty();
		});
	}

	@Test
	void testFindUnresolvedByPerson() {
		var person = VALID_TRACKED_PERSON4_ID_DEP1;
		repository.findByDescriptionCode(person, DescriptionCode.INCREASED_TEMPERATURE)
				.resolve(repository::save);
		em.flush();
		em.clear();

		ActionItems allItems = repository.findByTrackedPerson(person);
		assertThat(allItems.stream()) //
				.has(new HamcrestCondition<>(ActionItemMatcher.matchesItem(DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM, false)), Index.atIndex(0)) //
				.has(new HamcrestCondition<>(ActionItemMatcher.matchesItem(DescriptionCode.INCREASED_TEMPERATURE, true)), Index.atIndex(1));

		ActionItems unresolvedItems = repository.findUnresolvedByTrackedPerson(person);
		assertThat(unresolvedItems.stream()).hasSize(1) //
				.has(new HamcrestCondition<>(ActionItemMatcher.matchesItem(DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM, false)), Index.atIndex(0));
	}

	@RequiredArgsConstructor(staticName = "matchesItem")
	private static class ActionItemMatcher extends TypeSafeMatcher<ActionItem> {
		private final DescriptionCode descriptionCode;
		private final boolean resolved;

		@Override
		protected boolean matchesSafely(ActionItem item) {
			return item.getDescription().getCode().equals(descriptionCode)
					&& item.isResolved() == resolved
					;
		}

		@Override
		public void describeTo(org.hamcrest.Description description) {
			description.appendText("descriptionCode: ").appendValue(descriptionCode)
					.appendText(" resolved: ").appendValue(resolved);
		}
	}
}
