package quarano.diary;

import static java.util.stream.Collectors.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import quarano.diary.DiaryEntry.DiaryEntryIdentifier;
import quarano.tracking.ContactPerson;
import quarano.tracking.Encounter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.util.Streamable;
import org.springframework.lang.Nullable;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
public class Diary implements Streamable<DiaryEntry> {

	private final Streamable<DiaryEntry> entries;

	public boolean contains(Encounter encounter) {
		return getEntryFor(encounter).isPresent();
	}

	public Optional<DiaryEntry> getEntryFor(Encounter encounter) {

		return entries.stream()
				.filter(it -> it.containsEncounterWith(encounter.getContact()))
				.findFirst();
	}

	public Optional<DiaryEntry> getEntryFor(DiaryEntryIdentifier identifier) {

		return entries.stream()
				.filter(it -> it.hasId(identifier))
				.findFirst();
	}

	public Stream<DiaryEntryDay> toEntryDays(LocalDate startDate) {

		var byDate = entries.stream()
				.collect(groupingBy(DiaryEntry::getSlotDate));

		return Stream.iterate(LocalDate.now(),
				it -> it.isAfter(startDate) || it.equals(startDate),
				it -> it.minusDays(1))
				.map(it -> {

					var entries = byDate.get(it);
					var morningEntry = getSlotEntryFrom(Slot.morningOf(it), entries);
					var eveningEntry = getSlotEntryFrom(Slot.eveningOf(it), entries);

					return DiaryEntryDay.of(it, morningEntry, eveningEntry);
				});
	}

	public boolean containsCurrentEntry() {

		return entries.stream()
				.anyMatch(it -> it.hasSlot(Slot.now()));
	}

	public Optional<LocalDate> getDateOfFirstEncounterWith(ContactPerson contact) {

		return entries.stream()
				.filter(it -> it.containsEncounterWith(contact))
				.map(DiaryEntry::getSlotDate)
				.sorted()
				.findFirst();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<DiaryEntry> iterator() {
		return entries.iterator();
	}

	private static Optional<DiaryEntry> getSlotEntryFrom(Slot slot, @Nullable Collection<DiaryEntry> sources) {

		if (sources == null) {
			return Optional.empty();
		}

		return sources
				.stream()
				.filter(entry -> entry.hasSlot(slot))
				.findFirst();
	}

	@ToString
	@RequiredArgsConstructor(staticName = "of")
	public static class DiaryEntryDay {

		private final @Getter LocalDate date;
		private final @Getter Optional<DiaryEntry> morning, evening;
	}
}
