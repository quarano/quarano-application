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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItem.ActionItemIdentifier;
import quarano.core.QuaranoAggregate;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jddd.core.types.Identifier;

/**
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 */
@Entity
@Table(name = "action_items")
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public abstract class ActionItem extends QuaranoAggregate<ActionItem, ActionItemIdentifier> {

	private TrackedPersonIdentifier personIdentifier;
	private @Column(name = "item_type") ItemType type;
	private Description description;
	private boolean resolved;

	protected ActionItem(TrackedPersonIdentifier person, ItemType type, Description description) {

		this.id = ActionItemIdentifier.of(UUID.randomUUID());
		this.personIdentifier = person;
		this.type = type;
		this.description = description;
	}

	public boolean isUnresolved() {
		return !isResolved();
	}

	public boolean isMedicalItem() {
		return this.getType().equals(ItemType.MEDICAL_INCIDENT);
	}

	public boolean isProcessItem() {
		return this.getType().equals(ItemType.PROCESS_INCIDENT);
	}

	public float getWeight() {
		return getDescription().getCode().getMultiplier() * getTypeWeight();
	}

	private float getTypeWeight() {

		switch (getType()) {
			case MEDICAL_INCIDENT:
				return 0.7f;
			case PROCESS_INCIDENT:
			default:
				return 0.3f;
		}
	}

	ActionItem resolve() {

		this.resolved = true;

		return this;
	}

	public enum ItemType {

		MEDICAL_INCIDENT,

		PROCESS_INCIDENT;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class ActionItemIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = 7871473225101042167L;

		final UUID actionItemId;
	}
}
