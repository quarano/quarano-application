package quarano.actions;

import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.account.Department;
import quarano.department.CaseType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.CaseCreated;
import quarano.department.TrackedCase.CaseUpdated;
import quarano.tracking.TrackedPerson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@QuaranoUnitTest
class TrackedCaseEventListenerTests {

	@Mock InitialCallHandler initialCallHandler;
	@Mock MissingDetailsHandler missingDetailsHandler;

	TrackedCaseEventListener listener;

	@BeforeEach
	void setup() {
		listener = new TrackedCaseEventListener(initialCallHandler, missingDetailsHandler);
	}

	@Test
	void testTrackedCaseCreated() {
		TrackedCase trackedCase = trackedCase(CaseType.INDEX);
		listener.on(CaseCreated.of(trackedCase));

		verify(initialCallHandler, times(1)).handleInitialCallOpen(trackedCase);
		verify(missingDetailsHandler, times(1)).handleTrackedCaseMissingDetails(trackedCase);
	}

	@Test
	void testTrackedCaseUpdated() {
		TrackedCase trackedCase = trackedCase(CaseType.INDEX);
		listener.on(CaseUpdated.of(trackedCase));

		verify(initialCallHandler, times(1)).handleInitialCallOpen(trackedCase);
		verify(missingDetailsHandler, times(1)).handleTrackedCaseMissingDetails(trackedCase);
	}

	private TrackedCase trackedCase(CaseType caseType) {
		return new TrackedCase(new TrackedPerson("firstName", "lastName"), CaseType.INDEX, new Department("test"));
	}

}
