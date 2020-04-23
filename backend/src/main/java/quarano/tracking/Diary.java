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

import static java.util.stream.Collectors.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import quarano.tracking.DiaryEntry.DiaryEntryIdentifier;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
public class Diary implements Streamable<DiaryEntry> {

	private final List<DiaryEntry> enties;

	public boolean contains(Encounter encounter) {
		return getEntryFor(encounter).isPresent();
	}

	public Optional<DiaryEntry> getEntryFor(Encounter encounter) {

		return enties.stream() //
				.filter(it -> it.contains(encounter)) //
				.findFirst();
	}

	public Optional<DiaryEntry> getEntryFor(DiaryEntryIdentifier identifier) {

		return enties.stream() //
				.filter(it -> it.hasId(identifier)) //
				.findFirst();
	}

	public Stream<DiaryEntryDay> toEntryDays(LocalDate startDate) {

		var byDate = enties.stream() //
				.collect(groupingBy(DiaryEntry::getSlotDate));

		return Stream.iterate(LocalDate.now(), //
				it -> it.isAfter(startDate) || it.equals(startDate), //
				it -> it.minusDays(1)) //
				.map(it -> {

					var entries = byDate.get(it);
					var morningEntry = getSlotEntryFrom(Slot.morningOf(it), entries);
					var eveningEntry = getSlotEntryFrom(Slot.eveningOf(it), entries);

					return DiaryEntryDay.of(it, morningEntry, eveningEntry);
				});
	}

	private static Optional<DiaryEntry> getSlotEntryFrom(Slot slot, Collection<DiaryEntry> sources) {

		if (sources == null) {
			return Optional.empty();
		}

		return sources //
				.stream() //
				.filter(entry -> entry.hasSlot(slot)) //
				.findFirst();
	}

	public boolean containsCurrentEntry() {

		return enties.stream() //
				.anyMatch(it -> it.hasSlot(Slot.now()));
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<DiaryEntry> iterator() {
		return enties.iterator();
	}

	@ToString
	@RequiredArgsConstructor(staticName = "of")
	public static class DiaryEntryDay {

		private final @Getter LocalDate date;
		private final @Getter Optional<DiaryEntry> morning, evening;
	}
}
