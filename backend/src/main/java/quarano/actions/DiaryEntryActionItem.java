package quarano.actions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import quarano.department.TrackedCase;
import quarano.diary.DiaryEntry;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

/**
 * @author Oliver Drotbohm
 */
@Data
@EqualsAndHashCode(callSuper = true, of = {})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DiaryEntryActionItem extends ActionItem {

	private @ManyToOne DiaryEntry entry;

	DiaryEntryActionItem(TrackedPerson person, TrackedCase trackedCase, DiaryEntry entry, ItemType type, Description description) {

		super(person, trackedCase, type, description);

		this.entry = entry;
	}

	@SuppressWarnings("unused")
	private DiaryEntryActionItem() {
		super();
	}
}
