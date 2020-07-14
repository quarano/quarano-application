package quarano.actions;

import quarano.actions.ActionItem.ActionItemIdentifier;
import quarano.core.QuaranoRepository;
import quarano.department.TrackedCase;
import quarano.diary.Slot;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import org.springframework.data.jpa.repository.Query;

/**
 * @author Oliver Drotbohm
 * @author Felix Schultze
 */
public interface ActionItemRepository extends QuaranoRepository<ActionItem, ActionItemIdentifier> {

	default ActionItems findByCase(TrackedCase trackedCase) {
		return findByTrackedPerson(trackedCase.getTrackedPerson().getId());
	}

	default ActionItems findUnresolvedByActiveCase(TrackedCase trackedCase) {
		return findUnresolvedByActiveCaseByPersonIdentifier(trackedCase.getTrackedPerson().getId());
	}

	@Query("select i from ActionItem i where i.personIdentifier = :identifier")
	ActionItems findByTrackedPerson(TrackedPersonIdentifier identifier);

	@Query("select i from ActionItem i"
			+ " where i.resolved = false"
			+ " and i.personIdentifier = :identifier")
	ActionItems findUnresolvedByTrackedPerson(TrackedPersonIdentifier identifier);

	@Query("select i from ActionItem i, TrackedCase t"
			+ " where i.resolved = false"
			+ " and t.trackedPerson.id = i.personIdentifier" + " and t.status <> 'CONCLUDED'"
			+ " and i.personIdentifier = :identifier")
	ActionItems findUnresolvedByActiveCaseByPersonIdentifier(TrackedPersonIdentifier identifier);

	@Query("select i from ActionItem i where i.personIdentifier = :identifier and i.description.code = :code")
	ActionItems findByDescriptionCode(TrackedPersonIdentifier identifier, DescriptionCode code);

	@Query("select i from ActionItem i where i.personIdentifier = :identifier and i.description.code = :code and i.resolved = false")
	ActionItems findUnresolvedByDescriptionCode(TrackedPersonIdentifier identifier, DescriptionCode code);

	@Query("select i from DiaryEntryMissingActionItem i"
			+ " where i.slot = :slot"
			+ " and i.personIdentifier = :personIdentifier")
	ActionItems findDiaryEntryMissingActionItemsFor(TrackedPersonIdentifier personIdentifier, Slot slot);

	@Query("select i from TrackedCaseActionItem i"
			+ " where i.personIdentifier = :personIdentifier"
			+ "   and i.description.code = 'QUARANTINE_ENDING'")
	ActionItems findQuarantineEndingActionItemsFor(TrackedPersonIdentifier personIdentifier);

	default ActionItems findQuarantineEndingActionItemsFor(TrackedCase trackedCase) {
		return findQuarantineEndingActionItemsFor(trackedCase.getTrackedPerson().getId());
	}
}
