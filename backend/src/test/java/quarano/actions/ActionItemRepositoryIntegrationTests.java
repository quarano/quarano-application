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
import quarano.AbstractQuaranoIntegrationTests;
import quarano.actions.ActionItem.ItemType;
import quarano.tracking.BodyTemperature;
import quarano.tracking.DiaryEntry;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oliver Drotbohm
 */

@RequiredArgsConstructor
public class ActionItemRepositoryIntegrationTests extends AbstractQuaranoIntegrationTests {

	private final ActionItemRepository repository;
	private final TrackedPersonRepository persons;
	private final EntityManager em;

	@Transactional
	@Test
	void persistsDescriptionArguments() {

		DiaryEntry entry = new DiaryEntry(LocalDateTime.now(), "");
		entry.setBodyTemperature(BodyTemperature.of(41.0f));

		var person = persons.save(TrackedPersonDataInitializer.INDEX_PERSON3_WITH_ACTIVE_TRACKING //
				.addDiaryEntry(entry));

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
