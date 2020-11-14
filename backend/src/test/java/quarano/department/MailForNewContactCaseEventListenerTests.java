package quarano.department;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.account.DepartmentDataInitializer;
import quarano.account.DepartmentRepository;
import quarano.department.TrackedCase.CaseCreated;
import quarano.department.TrackedCase.MailStatus;
import quarano.department.TrackedCase.Status;
import quarano.department.TrackedCaseEventListener.EmailSendingEvents;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;
import quarano.util.TestEmailServer;

import java.util.Locale;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.icegreen.greenmail.util.GreenMailUtil;

@RequiredArgsConstructor
@QuaranoIntegrationTest
class MailForNewContactCaseEventListenerTests {

	final EmailSendingEvents events;
	final TrackedPersonRepository persons;
	final DepartmentRepository departments;
	final TestEmailServer mailServer;

	@BeforeEach
	void setUp() throws Exception {
		mailServer.reset();
	}

	@Test // CORE-61
	void sendMailCorrect() throws MessagingException {

		var trackedCase = trackedCase(null);

		assertThat(trackedCase.getNewContactCaseMailStatus()).isEqualTo(MailStatus.NOT_SENT);
		assertThat(trackedCase.getStatus()).isEqualTo(Status.OPEN);

		events.on(CaseCreated.of(trackedCase));

		// wait for max 5s for 1 email to arrive

		mailServer.assertEmailSent(message -> {

			assertThat(message.getSubject()).isEqualTo("GA Mannheim: Information für gemeldeten Kontakt");
			assertThat(GreenMailUtil.getBody(message)).startsWith("Sehr geehrte/geehrter Frau/Herr Mueller,");
			assertThat(message.getRecipients(RecipientType.TO)[0].toString())
					.isEqualTo("Tanja Mueller <tanja.mueller@testtest.de>");
			assertThat(message.getFrom()[0].toString()).isEqualTo("GA Mannheim <contact-email@gesundheitsamt.de>");
			assertThat(trackedCase.getNewContactCaseMailStatus()).isEqualTo(MailStatus.SENT);
			assertThat(trackedCase.getStatus()).isEqualTo(Status.OPEN);
		});
	}

	@Test // CORE-375
	void multiLanguageMails() throws Exception {

		// without saved language
		var trackedCase = trackedCase(null);

		events.on(CaseCreated.of(trackedCase));

		mailServer.assertEmailSentWithBody(it -> {
			assertThat(it)
					.startsWith("Sehr geehrte/geehrter Frau/Herr Mueller,");
			// (commented out because of CORE-550) .doesNotContain("=3D".repeat(10)); // is ==========
		}).reset();

		// with default language as saved language
		trackedCase = trackedCase(Locale.GERMANY);

		events.on(CaseCreated.of(trackedCase));

		mailServer.assertEmailSentWithBody(it -> {
			assertThat(it)
					.startsWith("Sehr geehrte/geehrter Frau/Herr Mueller,");
			// (commented out because of CORE-550) .doesNotContain("=3D".repeat(10)); // is ==========
		}).reset();

		// with saved language
		trackedCase = trackedCase(Locale.ENGLISH);

		events.on(CaseCreated.of(trackedCase));

		mailServer.assertEmailSentWithBody(it -> {
			assertThat(it).startsWith("Dear Mrs/Mr Mueller,")
					.contains("\r\n\r\n" + "=3D".repeat(10) + "\r\n\r\n") // is ==========
					.contains("Sehr geehrte/geehrter Frau/Herr Mueller,");
		}).reset();
	}

	@Test
	void noMailAddress() throws MessagingException {

		var trackedCase = trackedCase(null);
		trackedCase.getTrackedPerson().setEmailAddress(null);

		assertThat(trackedCase.getNewContactCaseMailStatus()).isEqualTo(MailStatus.NOT_SENT);
		assertThat(trackedCase.getStatus()).isEqualTo(Status.OPEN);

		events.on(CaseCreated.of(trackedCase));

		mailServer.assertNoEmailSent();

		assertThat(trackedCase.getNewContactCaseMailStatus()).isEqualTo(MailStatus.CANT_SENT);
		assertThat(trackedCase.getStatus()).isEqualTo(Status.OPEN);
	}

	private TrackedCase trackedCase(Locale locale) {

		var person = persons.findRequiredById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.setLocale(locale);
		var department = departments.findById(DepartmentDataInitializer.DEPARTMENT_ID_DEP1).orElseThrow();

		return new TrackedCase(person, CaseType.CONTACT, department);
	}
}
