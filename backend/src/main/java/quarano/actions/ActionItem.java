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

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.jddd.core.types.Identifier;

/**
 * @author Oliver Drotbohm
 */
@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public abstract class ActionItem extends QuaranoAggregate<ActionItem, ActionItemIdentifier> {

	private TrackedPersonIdentifier personIdentifier;
	private ItemType type;
	private Description description;
	private boolean resolved;

	protected ActionItem(TrackedPersonIdentifier person, ItemType type, Description description) {

		this.id = ActionItemIdentifier.of(UUID.randomUUID());
		this.personIdentifier = person;
		this.type = type;
		this.description = description;
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
		final UUID departmentId;
	}
}
