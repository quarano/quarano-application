package quarano.diary;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import quarano.QuaranoIntegrationTest;
import quarano.account.DepartmentRepository;
import quarano.tracking.TrackedPersonRepository;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;

@QuaranoIntegrationTest
public class DiaryEntryReminderMailProcessorTest {

	static final int PORT_OFFSET = 3100;

	static final ServerSetup setup = new ServerSetup(PORT_OFFSET + ServerSetup.PORT_SMTPS, null,
			ServerSetup.PROTOCOL_SMTPS);
	@RegisterExtension
	static final GreenMailExtension greenMail = new GreenMailExtension(ServerSetup.verbose(new ServerSetup[] { setup }));

	@Inject
	DiaryEntryReminderMailProcessor reminderProcessor;

	@Inject
	TrackedPersonRepository persons;
	@Inject
	DepartmentRepository departments;

	@Test
	void runsCorrect() throws MessagingException {
		reminderProcessor.checkForReminderMail();

		// wait for max 5s for 1 email to arrive
		assertTrue(greenMail.waitForIncomingEmail(3), "not all emails sented");

		Message[] messages = greenMail.getReceivedMessages();
		assertThat(messages).hasSize(3);

		assertThat(messages).extracting(it -> it.getSubject()).containsOnly("Erinnerung an Covid-19 Symptomtagebuch");
		assertThat(messages).extracting(it -> GreenMailUtil.getBody(it))
				.allMatch(it -> it.startsWith("Sehr geehrte/geehrter Frau/Herr"));
		assertThat(messages).extracting(it -> it.getRecipients(RecipientType.TO)[0].toString())
				.containsOnly("Siggi Seufert <siggi@testtest.de>", "Sandra Schubert <sandra.schubert@testtest.de>",
						"Gustav Meier <gustav.meier@testtest.de>");
		assertThat(messages).extracting(it -> it.getFrom()[0].toString()).containsOnly(
				"GA Mannheim <contact-email@gesundheitsamt.de>", "GA Darmstadt <contact-email@gadarmstadt.de>");
	}
}
