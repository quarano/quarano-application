package quarano.department;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import io.vavr.control.Try;
import quarano.QuaranoUnitTest;
import quarano.account.Department;
import quarano.core.EmailAddress;
import quarano.core.EmailSender;
import quarano.department.TrackedCase.CaseCreated;
import quarano.department.TrackedCase.RegistrationInitiated;
import quarano.department.TrackedCaseEventListener.EmailSendingEvents;
import quarano.department.activation.ActivationCode;
import quarano.department.activation.ActivationCodeService;
import quarano.tracking.TrackedPerson;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
@QuaranoUnitTest
@TestInstance(Lifecycle.PER_METHOD)
class CreateRegistrationForNewContactCaseUnitTests {

	@Mock EmailSender emailSender;
	@Mock RegistrationManagement registration;
	@Mock TrackedCaseRepository cases;
	@Mock MessageSourceAccessor messages;
	@Mock ActivationCodeService activationCodes;
	@Mock RegistrationProperties activationProperties;
	@Mock CommentFactory comments;
	@Mock TrackedCaseEmails emails;

	@InjectMocks TrackedCaseEventListener listener;
	@InjectMocks EmailSendingEvents events;

	@BeforeEach
	public void initTests() {

		when(emailSender.testConnection()).thenReturn(Try.success(null));

		events.onApplicationReady(mock(ApplicationReadyEvent.class));
	}

	@Test // CORE-618
	void withoutAutomaticCreationOfActivationCode() {

		var trackedCase = trackedCase();

		when(cases.findById(trackedCase.getId())).thenReturn(Optional.of(trackedCase));
		when(activationProperties.isAutomaticallyInitiateRegistrationForContactCases()).thenReturn(false);

		events.onCaseCreated(CaseCreated.of(trackedCase.getId()));

		verify(registration, never()).initiateRegistration(trackedCase);
		verify(emails).sendContactCaseInformationEmail(any(), any());
	}

	@Test // CORE-618
	void initiatesRegistrationOnCaseCreationIfConfigured() {

		var trackedCase = trackedCase();

		when(cases.findById(trackedCase.getId())).thenReturn(Optional.of(trackedCase));
		when(activationProperties.isAutomaticallyInitiateRegistrationForContactCases()).thenReturn(true);

		listener.onCaseCreated(CaseCreated.of(trackedCase.getId()));

		verify(registration).initiateRegistration(trackedCase);
	}

	@Test // CORE-618
	void sendsOutRegistrationEmailOnInitiation() {

		var trackedCase = trackedCase();
		var code = new ActivationCode(LocalDateTime.now(), trackedCase.getTrackedPerson().getId(),
				trackedCase.getDepartment().getId());

		when(cases.findById(trackedCase.getId())).thenReturn(Optional.of(trackedCase));
		when(activationCodes.getPendingActivationCode(trackedCase.getTrackedPerson().getId()))
				.thenReturn(Optional.of(code));

		events.onRegistrationInitiated(RegistrationInitiated.of(trackedCase.getId()));

		verify(emails).sendRegistrationEmail(eq(trackedCase), eq(code), any());
	}

	private TrackedCase trackedCase() {

		var person = new TrackedPerson("Max", "Muster");
		var department = mock(Department.class);

		person.setEmailAddress(EmailAddress.of("max@muster-test.de"));

		return new TrackedCase(person, CaseType.CONTACT, department);
	}
}
