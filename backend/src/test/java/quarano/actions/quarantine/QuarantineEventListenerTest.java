package quarano.actions.quarantine;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quarano.actions.ActionItemRepository;
import quarano.actions.ActionItems;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPerson;

@ExtendWith(MockitoExtension.class)
class QuarantineEventListenerTest {

	private static final TrackedCase.TrackedCaseIdentifier CASE_ID =
			TrackedCase.TrackedCaseIdentifier.of(UUID.randomUUID());

	private static final TrackedPerson.TrackedPersonIdentifier PERSON_ID =
			TrackedPerson.TrackedPersonIdentifier.of(UUID.randomUUID());

	private static final TrackedCase.CaseConcluded EVENT =
			TrackedCase.CaseConcluded.of(CASE_ID);
	private static final int NEVER = 0;

	@Mock private ActionItemRepository items;

	@Mock private TrackedCaseRepository cases;

	@InjectMocks private QuarantineEventListener quarantineEventListener;

	@Mock private TrackedCase trackedCase;

	@Mock private TrackedPerson trackedPerson;
	@Mock private QuarantineEndingActionItem actionItem;

	@Test
	void expectToDeleteExistingActionItems() {
		when(cases.findById(CASE_ID)).thenReturn(Optional.of(trackedCase));
		when(trackedCase.getTrackedPerson()).thenReturn(trackedPerson);
		when(trackedPerson.getId()).thenReturn(PERSON_ID);
		when(items.findQuarantineEndingActionItemsFor(PERSON_ID))
				.thenReturn(ActionItems.of(actionItem));

		quarantineEventListener.on(EVENT);

		verify(items).delete(actionItem);
	}

	@Test
	void expectToDeleteExecuteNoDeleteCall() {
		when(cases.findById(CASE_ID)).thenReturn(Optional.of(trackedCase));
		when(trackedCase.getTrackedPerson()).thenReturn(trackedPerson);
		when(trackedPerson.getId()).thenReturn(PERSON_ID);
		when(items.findQuarantineEndingActionItemsFor(PERSON_ID)).thenReturn(ActionItems.empty());

		quarantineEventListener.on(EVENT);

		verify(items, times(NEVER)).delete(actionItem);
	}

	@Test
	void expectNoInteractionDueToNonExistingCase() {
		when(cases.findById(CASE_ID)).thenReturn(Optional.empty());

		quarantineEventListener.on(EVENT);

		verifyNoInteractions(items, trackedCase);
	}
}
