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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;
import quarano.core.QuaranoAggregate;
import quarano.tracking.Encounter.EncounterIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;

import org.jddd.core.types.Identifier;
import org.jddd.event.types.DomainEvent;
import org.springframework.util.StringUtils;

/**
 * @author Oliver Drotbohm
 */
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class TrackedPerson extends QuaranoAggregate<TrackedPerson, TrackedPersonIdentifier> {

	private @Getter @Setter String firstName, lastName;
	private @Getter @Setter EmailAddress emailAddress;
	private @Getter @Setter PhoneNumber phoneNumber;

	@AttributeOverride(name = "value", column = @Column(name = "mobilePhoneNumber")) //
	private @Getter @Setter PhoneNumber mobilePhoneNumber;
	private @Getter @Setter Address address = new Address();
	private @Getter @Setter LocalDate dateOfBirth;

	@OneToMany(cascade = CascadeType.ALL) //
	private List<DiaryEntry> entries;
	private LocalDateTime lastEntryUpdate;

	@OneToMany(cascade = CascadeType.ALL) //
	private List<Encounter> encounters;

	public TrackedPerson(String firstName, String lastName) {
		this(new TrackedPersonIdentifier(UUID.randomUUID()), firstName, lastName, null, null, null);
	}

	TrackedPerson(TrackedPersonIdentifier fixedId, String firstName, String lastName, EmailAddress emailAddress,
			PhoneNumber phoneNumber, LocalDate dateOfBirth) {

		this.id = fixedId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.dateOfBirth = dateOfBirth;
		this.entries = new ArrayList<>();
		this.encounters = new ArrayList<>();
	}

	public Diary getDiary() {
		return Diary.of(entries);
	}

	public Encounters getEncounters() {
		return Encounters.of(encounters);
	}

	public boolean hasBirthdayOf(LocalDate date) {
		return this.dateOfBirth.equals(date);
	}

	public TrackedPerson addDiaryEntry(DiaryEntry entry) {

		this.lastEntryUpdate = entry.getDateTime();
		this.entries.add(entry);
		this.encounters.addAll(entry.toEncounters());

		registerEvent(DiaryEntryAdded.of(entry, getId()));

		return this;
	}

	public Stream<ContactPerson> getContactPersons() {

		return encounters.stream() //
				.map(Encounter::getContact);
	}

	public Encounter reportContactWith(ContactPerson person, LocalDate date) {

		var encounter = Encounter.with(person, date);
		this.encounters.add(encounter);

		registerEvent(EncounterReported.of(encounter, id));

		return encounter;
	}

	public boolean hasBeenInTouchWith(ContactPerson person, Period period) {
		return Encounters.of(encounters).hasBeenInTouchWith(person, period);
	}

	public TrackedPerson removeEncounter(EncounterIdentifier identifier) {

		encounters.stream() //
				.filter(it -> it.hasId(identifier)) //
				.findFirst() //
				.ifPresent(encounters::remove);

		return this;
	}

	public boolean isDetailsCompleted() {

		return StringUtils.hasText(firstName) //
				&& StringUtils.hasText(lastName) //
				&& emailAddress != null //
				&& address.isComplete() //
				&& dateOfBirth != null;
	}

	@Value(staticConstructor = "of")
	public static class DiaryEntryAdded implements DomainEvent {
		DiaryEntry entry;
		TrackedPersonIdentifier personIdentifier;
	}

	@Value(staticConstructor = "of")
	public static class EncounterReported implements DomainEvent {
		Encounter encounter;
		TrackedPersonIdentifier personIdentifier;
	}

	@PostLoad
	private void init() {

		if (address == null) {
			this.address = new Address();
		}
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
	public static class TrackedPersonIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -853047182358126916L;

		private final UUID trackedPersonId;

		@Override
		public String toString() {
			return trackedPersonId.toString();
		}
	}
}
