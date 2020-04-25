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

import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItem.ItemType;
import quarano.core.DataInitializer;
import quarano.tracking.Slot;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Order(800)
@Component
@RequiredArgsConstructor
public class ActionItemDataInitializer implements DataInitializer {

	private final TrackedPersonRepository people;
	private final ActionItemRepository items;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		TrackedPerson sandra = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2).orElseThrow();
		TrackedPerson gustav = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON4_ID_DEP1).orElseThrow();
		TrackedPerson nadine = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON5_ID_DEP1).orElseThrow();

		items.save(new DiaryEntryMissingActionItem(sandra.getId(), Slot.now().previous()));
		items.save(new DiaryEntryActionItem(gustav.getId(), gustav.getDiary().iterator().next(), ItemType.MEDICAL_INCIDENT, Description.of(DescriptionCode.INCREASED_TEMPERATURE, 40.1f)));
		items.save(new DiaryEntryActionItem(nadine.getId(), nadine.getDiary().iterator().next(), ItemType.MEDICAL_INCIDENT, Description.of(DescriptionCode.INCREASED_TEMPERATURE, 40.1f)));
	}
}
