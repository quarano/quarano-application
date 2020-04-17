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

import lombok.Data;
import lombok.EqualsAndHashCode;
import quarano.tracking.DiaryEntry;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

/**
 * @author Oliver Drotbohm
 */
@Data
@EqualsAndHashCode(callSuper = true, of = {})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DiaryEntryActionItem extends ActionItem {

	private @ManyToOne DiaryEntry entry;

	DiaryEntryActionItem(TrackedPersonIdentifier person, DiaryEntry entry, ItemType type, Description description) {

		super(person, type, description);

		this.entry = entry;
	}

	@SuppressWarnings("unused")
	private DiaryEntryActionItem() {
		super();
	}
}
