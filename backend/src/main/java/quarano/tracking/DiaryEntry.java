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
import quarano.core.QuaranoEntity;
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
import javax.persistence.OneToMany;

import org.jddd.core.types.Identifier;

/**
 * A bi-daily diary entry capturing a report of medical conditions and potential {@link ContactPerson}s.
 *
 * @author Oliver Drotbohm
 */
@Entity(name = "newDE")
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class DiaryEntry extends QuaranoEntity<TrackedPerson, DiaryEntryIdentifier> implements Comparable<DiaryEntry> {

	private static final Comparator<DiaryEntry> BY_DATE = Comparator.comparing(DiaryEntry::getDateTime);

	private final LocalDateTime date;
	private final @OneToMany List<ContactPerson> contacts;
	private final @Getter String note;
	private final BodyTemperature temperature;

	private DiaryEntry(LocalDateTime date, List<ContactPerson> contacts, String note) {

		this.id = new DiaryEntryIdentifier();
		this.date = date;
		this.contacts = contacts;
		this.note = note;
		this.temperature = null;
	}

	public static DiaryEntry of(String note, LocalDateTime date) {
		return new DiaryEntry(date, new ArrayList<>(), note);
	}

	public LocalDate getDate() {
		return date.toLocalDate();
	}

	public LocalDateTime getDateTime() {
		return date;
	}

	List<Encounter> toEncounters() {

		return contacts.stream() //
				.map(it -> Encounter.with(it, date.toLocalDate())) //
				.collect(Collectors.toList());
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
	static class DiaryEntryIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = -8938479214117686141L;
		UUID id = UUID.randomUUID();
	}
}
