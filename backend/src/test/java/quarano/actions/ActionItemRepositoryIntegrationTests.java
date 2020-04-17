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

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.actions.ActionItem.ItemType;
import quarano.tracking.BodyTemperature;
import quarano.tracking.DiaryEntry;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class ActionItemRepositoryIntegrationTests {

	private final ActionItemRepository repository;
	private final TrackedPersonRepository persons;
	private final EntityManager em;

	@Test
	void persistsDescriptionArguments() {

		DiaryEntry entry = new DiaryEntry(LocalDateTime.now(), "");
		entry.setBodyTemperature(BodyTemperature.of(41.0f));

		var person = persons.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2) //
				.map(it -> it.addDiaryEntry(entry)) //
				.map(persons::save).orElseThrow();

		var item = new DiaryEntryActionItem(person.getId(), entry, ItemType.MEDICAL_INCIDENT,
				Description.of(DescriptionCode.INCREASED_TEMPERATURE, BodyTemperature.of(36.0f), BodyTemperature.of(40.0f)));

		item = repository.save(item);

		em.flush();
		em.clear();

		assertThat(repository.findById(item.getId())).hasValueSatisfying(it -> {
			assertThat(it.getDescription().getArguments()).isNotEmpty();
		});
	}
}
