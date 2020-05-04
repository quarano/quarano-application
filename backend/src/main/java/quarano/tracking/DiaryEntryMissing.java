package quarano.tracking;

import lombok.Value;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.List;

@Value(staticConstructor = "of")
public class DiaryEntryMissing {
	List<Slot> missingSlots;
	TrackedPersonIdentifier trackedPersonIdentifier;
}
