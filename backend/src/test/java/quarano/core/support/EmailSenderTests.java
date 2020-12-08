package quarano.core.support;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.vavr.control.Try.Success;
import quarano.account.Department;
import quarano.account.DepartmentContact;
import quarano.account.DepartmentContact.ContactType;
import quarano.core.CoreProperties;
import quarano.core.EmailAddress;
import quarano.core.EmailSender;
import quarano.core.EmailTemplates;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonEmail;

import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
class EmailSenderTests {

	@Mock JavaMailSenderImpl emailSender;
	@Mock EmailTemplates templates;
	@Mock CoreProperties coreProps;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS) MailProperties mailProps;

	@Mock TrackedPerson trackedPerson;
	@Mock Department department;
	@Mock DepartmentContact depContact;

	@Captor ArgumentCaptor<SimpleMailMessage> captor;

	@BeforeEach
	public void openMocks() {

		MockitoAnnotations.initMocks(this);

		when(depContact.getEmailAddress()).thenReturn(EmailAddress.of("info@ga.de"));
		when(department.getContact(ContactType.INDEX)).thenReturn(Optional.of(depContact));
		when(trackedPerson.getEmailAddress()).thenReturn(EmailAddress.of("test@test.de"));
		when(trackedPerson.getFullName()).thenReturn("X Muster");
	}

	@Test // CORE-579
	void protectionAgainstUnwantedMails() throws MessagingException {

		var sender = createSender();
		var email = createEmail();

		when(mailProps.getProperties().get("fix-recipient")).thenReturn("testmailbox@quarano.de");

		var result = sender.sendMail(email);

		assertThat(result).isInstanceOf(Success.class);

		verify(emailSender).send(captor.capture());

		assertThat(captor.getValue().getTo()).contains("X Muster - test {at} test.de <testmailbox@quarano.de>");
	}

	@Test // CORE-579
	void withoutProtectionAgainstUnwantedMailsInDevelopAndProdProfile() throws MessagingException {

		var sender = createSender();
		var email = createEmail();

		when(mailProps.getProperties().get("fix-recipient")).thenReturn("", null);

		var result = sender.sendMail(email);

		assertThat(result).isInstanceOf(Success.class);

		result = sender.sendMail(email);

		assertThat(result).isInstanceOf(Success.class);

		verify(emailSender, times(2)).send(captor.capture());

		assertThat(captor.getAllValues())
				.extracting(SimpleMailMessage::getTo)
				.extracting(it -> it[0])
				.contains("X Muster <test@test.de>");
	}

	private EmailSender createSender() {

		var sender = new EmailSender(emailSender, templates, coreProps, mailProps);

		sender.onApplicationEvent(mock(ApplicationReadyEvent.class));

		return sender;
	}

	private TrackedPersonEmail createEmail() {
		return new TrackedPersonEmail(trackedPerson, department, ContactType.INDEX, "Test",
				EmailTemplates.Keys.DIARY_REMINDER, Map.of());
	}
}
