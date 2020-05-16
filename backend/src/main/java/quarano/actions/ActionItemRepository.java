package quarano.actions;

import quarano.actions.ActionItem.ActionItemIdentifier;
import quarano.core.QuaranoRepository;
import quarano.department.TrackedCase;
import quarano.tracking.Slot;
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

	default ActionItems findUnresolvedByCase(TrackedCase trackedCase) {
		return findUnresolvedByTrackedPerson(trackedCase.getTrackedPerson().getId());
	}

	@Query("select i from ActionItem i where i.personIdentifier = :identifier")
	ActionItems findByTrackedPerson(TrackedPersonIdentifier identifier);

	@Query("select i from ActionItem i" //
			+ " where i.resolved = false" //
			+ " and i.personIdentifier = :identifier")
	ActionItems findUnresolvedByTrackedPerson(TrackedPersonIdentifier identifier);

	@Query("select i from ActionItem i where i.personIdentifier = :identifier and i.description.code = :code")
	ActionItems findByDescriptionCode(TrackedPersonIdentifier identifier, DescriptionCode code);

	@Query("select i from DiaryEntryMissingActionItem i" //
			+ " where i.slot = :slot" //
			+ " and i.personIdentifier = :personIdentifier")
	ActionItems findDiaryEntryMissingActionItemsFor(TrackedPersonIdentifier personIdentifier, Slot slot);

	@Query("select i from QuarantineEndingActionItem i" //
			+ " where i.personIdentifier = :personIdentifier")
	ActionItems findQuarantineEndingActionItemsFor(TrackedPersonIdentifier personIdentifier);

}
