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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import quarano.tracking.Slot;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;

/**
 * @author Oliver Drotbohm
 */
@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class DiaryEntryMissingActionItem extends ActionItem {

	private final @Getter Slot slot;

	DiaryEntryMissingActionItem(TrackedPersonIdentifier person, Slot slot) {

		super(person, ItemType.PROCESS_INCIDENT, getDescription(slot));

		this.slot = slot;
	}

	private static Description getDescription(Slot slot) {

		var date = slot.getDate().format(DateTimeFormatter.ofPattern("dd.MM.YYYY"));
		return Description.of(DescriptionCode.DIARY_ENTRY_MISSING, date);
	}
}
