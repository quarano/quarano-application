package quarano.actions;

import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import quarano.department.TrackedCase;

/**
 * @author Oliver Drotbohm
 * @author Felix Schultze
 */
@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class TrackedCaseActionItem extends ActionItem {

	TrackedCaseActionItem(TrackedCase trackedCase, ItemType type, DescriptionCode code) {
		super(trackedCase.getTrackedPerson(), trackedCase, type, Description.of(code));
	}

}
