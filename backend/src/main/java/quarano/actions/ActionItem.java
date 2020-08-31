package quarano.actions;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.jddd.core.types.Identifier;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItem.ActionItemIdentifier;
import quarano.core.QuaranoAggregate;
import quarano.department.TrackedCase;
import quarano.diary.DiaryEntry;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

/**
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 * @author Felis Schultze
 */
@Entity
@Table(name = "action_items")
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public abstract class ActionItem extends QuaranoAggregate<ActionItem, ActionItemIdentifier> {

	private @Getter TrackedCase.TrackedCaseIdentifier caseIdentifier;
	private TrackedPersonIdentifier personIdentifier;

	private @Column(name = "item_type") @Enumerated(value = EnumType.STRING) ItemType type;
	private Description description;
	private boolean resolved;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "tracked_case_id", referencedColumnName="tracked_case_id", insertable = false, updatable = false)
	private TrackedCase trackedCase;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "tracked_person_id", referencedColumnName="tracked_person_id", insertable = false, updatable = false)
	private TrackedPerson trackedPerson;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "entry_diary_entry_id", referencedColumnName="diary_entry_id", insertable = false, updatable = false)
	private DiaryEntry diaryEntry;

	protected ActionItem(TrackedPerson person, TrackedCase trackedCase, ItemType type, Description description) {

		this.id = ActionItemIdentifier.of(UUID.randomUUID());
		this.personIdentifier = person.getId();
		this.trackedPerson = person;
		this.type = type;
		this.description = description;
		this.trackedCase = trackedCase;
		this.caseIdentifier = trackedCase.getId();
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

	public boolean isManuallyResolvable() {
		return getDescription().getCode().isManuallyResolvable();
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

		@Column(name = "action_item_id")
		final UUID actionItemId;
	}
}
