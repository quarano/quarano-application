package quarano.actions;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.CaseConcluded;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseRepository;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/**
 * @author Lennart Blom
 * @author Oliver Drotbohm
 */
@QuaranoUnitTest
@MockitoSettings(strictness = Strictness.LENIENT)
class QuarantineEventListenerTest {

	private static final TrackedCaseIdentifier CASE_ID = TrackedCaseIdentifier.of(UUID.randomUUID());
	private static final CaseConcluded EVENT = CaseConcluded.of(CASE_ID);
	private static final int NEVER = 0;

	@InjectMocks TrackedCaseEventListener quarantineEventListener;

	@Mock InitialCallHandler initialCallHander;
	@Mock MissingDetailsHandler missingDetailsHandler;
	@Mock ActionItemRepository items;
	@Mock TrackedCaseRepository cases;

	@Mock TrackedCase trackedCase;
	@Mock ActionItems actionItems;

	@Test // CORE-70
	void caseResolutionResolvesOpenActionItems() {

		when(cases.findById(CASE_ID)).thenReturn(Optional.of(trackedCase));
		when(items.findQuarantineEndingActionItemsFor(trackedCase)).thenReturn(actionItems);
		when(actionItems.hasUnresolvedItems()).thenReturn(true);

		quarantineEventListener.on(EVENT);

		verify(actionItems).resolveManually(any());
	}

	@Test // CORE-70
	void doesNotResolveAlreadyResolvedActionItems() {

		when(cases.findById(CASE_ID)).thenReturn(Optional.of(trackedCase));
		when(items.findQuarantineEndingActionItemsFor(trackedCase)).thenReturn(actionItems);
		when(actionItems.hasUnresolvedItems()).thenReturn(false);

		quarantineEventListener.on(EVENT);

		verify(actionItems, times(NEVER)).resolveManually(any());
	}

	@Test
	void doesNotResolveAnyItemsIfCaseDoesNotExist() {

		when(cases.findById(CASE_ID)).thenReturn(Optional.empty());

		quarantineEventListener.on(EVENT);

		verifyNoInteractions(items, trackedCase);
	}
}
