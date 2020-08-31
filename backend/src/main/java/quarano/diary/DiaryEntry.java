package quarano.diary;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jddd.core.types.Identifier;
import org.jddd.event.types.DomainEvent;
import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;
import quarano.actions.ActionItem;
import quarano.core.QuaranoAggregate;
import quarano.diary.DiaryEntry.DiaryEntryIdentifier;
import quarano.reference.Symptom;
import quarano.tracking.BodyTemperature;
import quarano.tracking.ContactPerson;
import quarano.tracking.Symptoms;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

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

    private @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "diary_entries_contacts",
            joinColumns = @JoinColumn(name = "diary_entry_diary_entry_id", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "contacts_contact_person_id")
    )
    List<ContactPerson> contacts = new ArrayList<>();

    private @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "diary_entries_symptoms",
            joinColumns = @JoinColumn(name = "diary_entry_diary_entry_id", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "symptoms_symptom_id")
    )
    List<Symptom> symptoms = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tracked_person_id", referencedColumnName="tracked_person_id", insertable = false, updatable = false)
    private TrackedPerson trackedPerson;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="diaryEntry")
    private @Getter @Setter List<ActionItem> actions = new ArrayList<>();

    private String note;
    private BodyTemperature bodyTemperature;

    @AttributeOverride(name = "trackedPersonId", column = @Column(name = "tracked_person_id"))
    private TrackedPersonIdentifier trackedPersonId;

	DiaryEntry(Slot slot, TrackedPerson person) {
		this(DiaryEntryIdentifier.of(UUID.randomUUID()), person, LocalDateTime.now(), slot);
	}

    DiaryEntry(DiaryEntryIdentifier id, TrackedPerson person, LocalDateTime reportedAt, Slot slot) {

        this.id = id;
        this.reportedAt = reportedAt;
        this.slot = slot;
        this.trackedPersonId = person.getId();
        this.trackedPerson = person;

        registerEvent(DiaryEntryAdded.of(this));
    }

    public static DiaryEntry of(Slot slot, TrackedPerson person) {
        return new DiaryEntry(slot, person);
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

    public DiaryEntry markEdited() {
        registerEvent(DiaryEntryUpdated.of(this));
        return this;
    }

    public boolean isAfter(DiaryEntry entry) {
        return this.slot.isAfter(entry.slot);
    }

    public boolean isBefore(DiaryEntry entry) {
        return this.slot.isBefore(entry.slot);
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

        @Column(name = "diary_entry_id")
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
