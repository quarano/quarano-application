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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import quarano.core.QuaranoEntity;
import quarano.reference.NewSymptom;
import quarano.tracking.DiaryEntry.DiaryEntryIdentifier;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.jddd.core.types.Identifier;
import org.springframework.util.Assert;

/**
 * A bi-daily diary entry capturing a report of medical conditions and potential {@link ContactPerson}s.
 *
 * @author Oliver Drotbohm
 */
@EqualsAndHashCode(callSuper = true, of = {})
@Entity(name = "newDE")
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class DiaryEntry extends QuaranoEntity<TrackedPerson, DiaryEntryIdentifier> implements Comparable<DiaryEntry> {

	private static final Comparator<DiaryEntry> BY_DATE = Comparator.comparing(DiaryEntry::getDateTime);

	private LocalDateTime date;
	private @ManyToMany List<ContactPerson> contacts = new ArrayList<>();
	private @ManyToMany List<NewSymptom> symptoms = new ArrayList<>();
	private String note;
	private BodyTemperature bodyTemperature;

	public DiaryEntry(LocalDateTime date, String note) {
		this(DiaryEntryIdentifier.of(UUID.randomUUID()), date, note);
	}

	DiaryEntry(DiaryEntryIdentifier id, LocalDateTime date, String note) {

		this.id = id;
		this.date = date;
		this.note = note;
	}

	public static DiaryEntry of(String note, LocalDateTime date) {
		return new DiaryEntry(date, note);
	}

	public LocalDate getDate() {
		return date.toLocalDate();
	}

	public LocalDateTime getDateTime() {
		return date;
	}

	public DiaryEntry add(NewSymptom symptom) {

		Assert.notNull(symptom, "Symptom must not be null!");

		this.symptoms.add(symptom);
		return this;
	}

	public Symptoms getSymptoms() {
		return Symptoms.of(symptoms);
	}

	List<Encounter> toEncounters() {

		return contacts.stream() //
				.map(it -> Encounter.with(it, date.toLocalDate())) //
				.collect(Collectors.toList());
	}

	boolean contains(Encounter encounter) {

		// if (!encounter.happenedOn(getDate())) {
		// return false;
		// }

		return contacts.contains(encounter.getContact());
	}

	public boolean hasId(DiaryEntryIdentifier identifier) {
		return this.id.equals(identifier);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DiaryEntry that) {
		return BY_DATE.compare(this, that);
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
	public static class DiaryEntryIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -8938479214117686141L;

		private final UUID id;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return id.toString();
		}
	}
}
