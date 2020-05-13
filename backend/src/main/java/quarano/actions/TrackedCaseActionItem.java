package quarano.actions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import javax.persistence.Entity;

/**
 * @author Oliver Drotbohm
 * @author Felix Schultze
 */
@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class TrackedCaseActionItem extends ActionItem {

	private final @Getter TrackedCaseIdentifier caseIdentifier;

	TrackedCaseActionItem(TrackedPersonIdentifier person, TrackedCaseIdentifier caseIdentifier, ItemType itemType, DescriptionCode descriptionCode) {

		super(person, itemType, Description.of(descriptionCode));

		this.caseIdentifier = caseIdentifier;
	}
}
