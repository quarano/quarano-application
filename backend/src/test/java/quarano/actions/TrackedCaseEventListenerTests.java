package quarano.actions;

import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.account.Department;
import quarano.department.CaseType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.CaseCreated;
import quarano.department.TrackedCase.CaseStatusUpdated;
import quarano.department.TrackedCase.CaseUpdated;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPerson;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@QuaranoUnitTest
class TrackedCaseEventListenerTests {

	@Mock InitialCallHandler initialCallHandler;
	@Mock MissingDetailsHandler missingDetailsHandler;
	@Mock TrackedCaseRepository cases;
	@Mock ActionItemRepository items;

	TrackedCaseEventListener listener;

	@BeforeEach
	void setup() {
		listener = new TrackedCaseEventListener(initialCallHandler, missingDetailsHandler, cases, items);
	}

	@Test
	void testTrackedCaseCreated() {

		TrackedCase trackedCase = trackedCase(CaseType.INDEX);
		when(cases.findById(trackedCase.getId())).thenReturn(Optional.of(trackedCase));

		listener.on(CaseCreated.of(trackedCase.getId()));

		verify(initialCallHandler, times(1)).handleInitialCallOpen(trackedCase);
		verify(missingDetailsHandler, times(1)).handleTrackedCaseMissingDetails(trackedCase);
	}

	@Test
	void testTrackedCaseUpdated() {

		TrackedCase trackedCase = trackedCase(CaseType.INDEX);
		when(cases.findById(trackedCase.getId())).thenReturn(Optional.of(trackedCase));

		listener.on(CaseUpdated.of(trackedCase.getId()));

		verify(initialCallHandler, times(1)).handleInitialCallOpen(trackedCase);
		verify(missingDetailsHandler, times(1)).handleTrackedCaseMissingDetails(trackedCase);
	}

	@Test
	void testTrackedCaseStatusUpdated() {

		TrackedCase trackedCase = trackedCase(CaseType.INDEX);
		when(cases.findById(trackedCase.getId())).thenReturn(Optional.of(trackedCase));

		listener.on(CaseStatusUpdated.of(trackedCase.getId()));

		verify(initialCallHandler, times(1)).handleInitialCallOpen(trackedCase);
		verify(missingDetailsHandler, times(1)).handleTrackedCaseMissingDetails(trackedCase);
	}

	private static TrackedCase trackedCase(CaseType caseType) {
		return new TrackedCase(new TrackedPerson("firstName", "lastName"), CaseType.INDEX, new Department("test"));
	}
}
