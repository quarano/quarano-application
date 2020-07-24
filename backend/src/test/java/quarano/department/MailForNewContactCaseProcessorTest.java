package quarano.department;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import quarano.QuaranoIntegrationTest;
import quarano.account.DepartmentDataInitializer;
import quarano.account.DepartmentRepository;
import quarano.department.TrackedCase.CaseCreated;
import quarano.department.TrackedCase.MailStatus;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.test.context.ActiveProfiles;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;

@QuaranoIntegrationTest
@ActiveProfiles({ "junit", "integrationtest" })
class MailForNewContactCaseProcessorTest {

	static final int PORT_OFFSET = 3100;

	static final ServerSetup setup = new ServerSetup(PORT_OFFSET + ServerSetup.PORT_SMTPS, null,
			ServerSetup.PROTOCOL_SMTPS);
	@RegisterExtension
	static final GreenMailExtension greenMail = new GreenMailExtension(ServerSetup.verbose(new ServerSetup[] { setup }));

	@Inject
	MailForNewContactCaseProcessor mailProcessor;

	@Inject
	TrackedPersonRepository persons;
	@Inject
	DepartmentRepository departments;

	@Test
	void sendMailCorrect() throws MessagingException {

		var trackedCase = trackedCase();

		assertThat(trackedCase.getNewContactCaseMailStatus()).isEqualTo(MailStatus.NOT_SENTED);

		this.mailProcessor.on(CaseCreated.of(trackedCase));

		// wait for max 5s for 1 email to arrive
		assertTrue(greenMail.waitForIncomingEmail(1), "email not sented");

		Message[] messages = greenMail.getReceivedMessages();
		assertThat(messages).hasSize(1);

		Message message = messages[0];
		assertThat(message.getSubject()).isEqualTo("Information vom GA Mannheim");
		assertThat(GreenMailUtil.getBody(message)).startsWith("Sehr geehrte/geehrter Frau/Herr Mueller,")
				.contains("/client/enrollment/landing/contact/");
		assertThat(message.getRecipients(RecipientType.TO)[0].toString())
				.isEqualTo("Tanja Mueller <tanja.mueller@testtest.de>");
		assertThat(message.getFrom()[0].toString()).isEqualTo("GA Mannheim <contact-email@gesundheitsamt.de>");

		assertThat(trackedCase.getNewContactCaseMailStatus()).isEqualTo(MailStatus.SENTED);
	}

	@Test
	void noMailAddress() throws MessagingException {

		var trackedCase = trackedCase();
		trackedCase.getTrackedPerson().setEmailAddress(null);

		assertThat(trackedCase.getNewContactCaseMailStatus()).isEqualTo(MailStatus.NOT_SENTED);

		this.mailProcessor.on(CaseCreated.of(trackedCase));

		// wait for email
		assertFalse(greenMail.waitForIncomingEmail(1), "not email should sent");

		Message[] messages = greenMail.getReceivedMessages();
		assertThat(messages).hasSize(0);

		assertThat(trackedCase.getNewContactCaseMailStatus()).isEqualTo(MailStatus.CANT_SENT);
	}

	private TrackedCase trackedCase() {

		var person = persons.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1).orElseThrow();
		var department = departments.findById(DepartmentDataInitializer.DEPARTMENT_ID_DEP1).orElseThrow();

		return new TrackedCase(person, CaseType.CONTACT, department);
	}
}
