package quarano.actions.quarantine;

import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import quarano.actions.ActionItem;
import quarano.actions.Description;
import quarano.actions.DescriptionCode;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

/**
 * @author Oliver Drotbohm
 */
@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class QuarantineEndingActionItem extends ActionItem {

	QuarantineEndingActionItem(TrackedPersonIdentifier person) {
		super(person, ItemType.PROCESS_INCIDENT, Description.of(DescriptionCode.QUARANTINE_ENDING));
	}
}
