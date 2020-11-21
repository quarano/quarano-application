package quarano.department;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import io.vavr.control.Try;
import quarano.QuaranoUnitTest;
import quarano.account.Department;
import quarano.account.DepartmentContact;
import quarano.account.DepartmentContact.ContactType;
import quarano.core.EmailAddress;
import quarano.core.EmailSender;
import quarano.core.EmailSender.AbstractTemplatedEmail;
import quarano.department.TrackedCase.CaseCreated;
import quarano.department.TrackedCaseEventListener.EmailSendingEvents;
import quarano.department.activation.ActivationCode;
import quarano.department.activation.ActivationCodeProperties;
import quarano.department.activation.ActivationCodeService;
import quarano.tracking.TrackedPerson;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.support.MessageSourceAccessor;

@QuaranoUnitTest
@TestInstance(Lifecycle.PER_METHOD)
public class CreateActivationCodeForNewContactCaseTests {

	private @Mock EmailSender emailSender;
	private @Mock RegistrationManagement registration;
	private @Mock TrackedCaseRepository cases;
	private @Mock MessageSourceAccessor messages;
	private @Mock ActivationCodeService activationCodes;
	private @Mock ActivationCodeProperties activationProperties;

	private @InjectMocks EmailSendingEvents events;

	@Captor
	ArgumentCaptor<AbstractTemplatedEmail> templateCaptor;

	@BeforeEach
	public void initTests() {

		when(emailSender.testConnection()).thenReturn(Try.success(null));

		events.on(mock(ApplicationReadyEvent.class));
	}

	@Test // CORE-618
	void withoutAutomaticCreationOfActivationCode() {

		var trackedCase = trackedCase();

		when(activationProperties.isCreateAutomaticForNewContacts()).thenReturn(false);

		events.on(CaseCreated.of(trackedCase));

		verify(registration, never()).initiateRegistration(trackedCase);
		verify(emailSender).sendMail(templateCaptor.capture());

		assertThat(templateCaptor.getValue().getPlaceholders()).doesNotContainKey("activationCode");
	}

	@Test // CORE-618
	void withAutomaticCreationOfActivationCode() {

		var trackedCase = trackedCase();

		var code = new ActivationCode(LocalDateTime.now(), trackedCase.getTrackedPerson().getId(),
				trackedCase.getDepartment().getId());

		when(activationProperties.isCreateAutomaticForNewContacts()).thenReturn(true);
		when(registration.initiateRegistration(trackedCase)).thenReturn(Try.success(code));
		when(emailSender.sendMail(any())).thenReturn(Try.success(null));

		events.on(CaseCreated.of(trackedCase));

		verify(registration).initiateRegistration(trackedCase);
		verify(activationCodes).codeMailed(code.getId());

		verify(emailSender).sendMail(templateCaptor.capture());

		assertThat(templateCaptor.getValue().getPlaceholders()).extractingByKey("activationCode")
				.isEqualTo(code.getId().toString());
	}

	private TrackedCase trackedCase() {

		var person = new TrackedPerson("Max", "Muster");
		var department = mock(Department.class);
		var depContact = mock(DepartmentContact.class);

		person.setEmailAddress(EmailAddress.of("max@muster-test.de"));

		when(depContact.getEmailAddress()).thenReturn(EmailAddress.of("email@test-ga.de"));

		when(department.getName()).thenReturn("Test GA");
		when(department.getContact(ContactType.CONTACT)).thenReturn(Optional.of(depContact));

		return new TrackedCase(person, CaseType.CONTACT, department);
	}
}
