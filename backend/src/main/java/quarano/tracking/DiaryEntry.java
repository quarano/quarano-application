package quarano.tracking;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import quarano.core.QuaranoAggregate;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.CaseUpdated;
import quarano.reference.Symptom;
import quarano.tracking.DiaryEntry.DiaryEntryIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.jddd.core.types.Identifier;
import org.jddd.event.types.DomainEvent;
import org.springframework.util.Assert;

/**
 * A bi-daily diary entry capturing a report of medical conditions and potential {@link ContactPerson}s.
 *
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 */
@EqualsAndHashCode(callSuper = true, of = {})
@Entity
@Table(name = "diary_entries")
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class DiaryEntry extends QuaranoAggregate<DiaryEntry, DiaryEntryIdentifier> implements Comparable<DiaryEntry> {

	private static final Comparator<DiaryEntry> BY_DATE = Comparator.comparing(DiaryEntry::getDateTime).reversed();

	private LocalDateTime reportedAt;
	private @Column(unique = true) Slot slot;
	private @ManyToMany List<ContactPerson> contacts = new ArrayList<>();
	private @ManyToMany List<Symptom> symptoms = new ArrayList<>();
	private String note;
	private BodyTemperature bodyTemperature;

	@AttributeOverride(name = "trackedPersonId", column = @Column(name = "tracked_person_id")) //
	private TrackedPersonIdentifier trackedPersonId;

	DiaryEntry(Slot slot, TrackedPersonIdentifier id) {
		this(DiaryEntryIdentifier.of(UUID.randomUUID()), id, LocalDateTime.now(), slot);
	}

	DiaryEntry(DiaryEntryIdentifier id, TrackedPersonIdentifier person, LocalDateTime reportedAt, Slot slot) {

		this.id = id;
		this.reportedAt = reportedAt;
		this.slot = slot;
		this.trackedPersonId = person;

		registerEvent(DiaryEntryAdded.of(this));
	}

	public static DiaryEntry of(Slot slot, TrackedPerson person) {
		return DiaryEntry.of(slot, person.getId());
	}

	public static DiaryEntry of(Slot slot, TrackedPersonIdentifier id) {
		return new DiaryEntry(slot, id);
	}

	public LocalDate getSlotDate() {
		return getSlot().getDate();
	}

	public LocalDate getDate() {
		return reportedAt.toLocalDate();
	}

	public LocalDateTime getDateTime() {
		return reportedAt;
	}

	public DiaryEntry add(Symptom symptom) {

		Assert.notNull(symptom, "Symptom must not be null!");

		this.symptoms.add(symptom);
		return this;
	}

	public Symptoms getSymptoms() {
		return Symptoms.of(symptoms);
	}

	List<Encounter> toEncounters() {

		return contacts.stream() //
				.map(it -> Encounter.with(it, reportedAt.toLocalDate())) //
				.collect(Collectors.toList());
	}

	boolean containsEncounterWith(ContactPerson contactPerson) {
		return contacts.contains(contactPerson);
	}

	@Override
	public boolean hasId(DiaryEntryIdentifier identifier) {
		return this.id.equals(identifier);
	}

	boolean hasSlot(Slot slot) {
		return this.slot.equals(slot);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DiaryEntry that) {
		return BY_DATE.compare(this, that);
	}

	public DiaryEntry markEdited() {

		registerEvent(DiaryEntryUpdated.of(this));

		return this;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
	public static class DiaryEntryIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -8938479214117686141L;

		private final UUID diaryEntryId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return diaryEntryId.toString();
		}
	}

	@Value(staticConstructor = "of")
	public static class DiaryEntryAdded implements DomainEvent {
		DiaryEntry entry;
	}
	
	@Value(staticConstructor = "of")
	public static class DiaryEntryUpdated implements DomainEvent {
		DiaryEntry entry;
	}
}
