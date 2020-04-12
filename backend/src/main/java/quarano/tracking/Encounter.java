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
package quarano.tracking;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import quarano.core.QuaranoEntity;
import quarano.tracking.Encounter.EncounterIdentifier;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.jddd.core.types.Identifier;
import org.springframework.util.Assert;

/**
 * @author Oliver Drotbohm
 */
@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Encounter extends QuaranoEntity<TrackedPerson, EncounterIdentifier> {

	private final @Getter @ManyToOne ContactPerson contact;
	private final @Getter LocalDate date;

	private Encounter(ContactPerson contact, LocalDate date) {

		Assert.notNull(contact, "ContactPerson must not be null!");
		Assert.notNull(date, "Date must not be null!");

		this.id = EncounterIdentifier.of(UUID.randomUUID());
		this.contact = contact;
		this.date = date;
	}

	public static Encounter with(ContactPerson person, LocalDate date) {
		return new Encounter(person, date);
	}

	public boolean isEncounterWith(ContactPerson person) {
		return this.contact == person;
	}

	public boolean hasId(EncounterIdentifier id) {
		return this.id.equals(id);
	}

	public boolean happenedOn(LocalDate date) {
		return this.date.equals(date);
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
	public static class EncounterIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = -5998761917714158567L;
		private final UUID encounterId;
	}
}
