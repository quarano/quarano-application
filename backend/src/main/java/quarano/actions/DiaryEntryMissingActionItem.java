package quarano.actions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import quarano.diary.Slot;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;

/**
 * @author Oliver Drotbohm
 */
@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class DiaryEntryMissingActionItem extends ActionItem {

	private final @Getter Slot slot;

	DiaryEntryMissingActionItem(TrackedPersonIdentifier person, Slot slot) {

		super(person, ItemType.PROCESS_INCIDENT, getDescription(slot));

		this.slot = slot;
	}

	private static Description getDescription(Slot slot) {

		var date = slot.getDate().format(DateTimeFormatter.ofPattern("dd.MM.YYYY"));
		return Description.of(DescriptionCode.DIARY_ENTRY_MISSING, date);
	}
}
