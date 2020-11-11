package quarano.diary;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;

@RequiredArgsConstructor
@QuaranoIntegrationTest
class DiaryEntryReminderMailJobTests {

	private final DiaryEntryReminderMailJob job;
	private final GreenMail greenMail;

	@BeforeEach
	void resetGreenMail() throws Exception {
		greenMail.purgeEmailFromAllMailboxes();
	}

	@Test // CORE-61
	void runsCorrect() throws MessagingException {

		job.checkForReminderMail();

		// wait for max 5s for 1 email to arrive
		assertTrue(greenMail.waitForIncomingEmail(3), "not all emails sent");

		Message[] messages = greenMail.getReceivedMessages();

		assertThat(messages).hasSize(3);

		assertThat(messages).extracting(it -> it.getSubject())
				.containsOnly("Erinnerung an Covid-19 Symptomtagebuch");
		assertThat(messages).extracting(it -> GreenMailUtil.getBody(it))
				.allMatch(it -> it.startsWith("Sehr geehrte/geehrter Frau/Herr"));

		assertThat(messages).extracting(it -> it.getRecipients(RecipientType.TO)[0].toString())
				.containsOnly("Siggi Seufert <siggi@testtest.de>",
						"Sandra Schubert <sandra.schubert@testtest.de>",
						"Gustav Meier <gustav.meier@testtest.de>");

		assertThat(messages).extracting(it -> it.getFrom()[0].toString()).containsOnly(
				"GA Mannheim <index-email@gesundheitsamt.de>",
				"GA Darmstadt <index-email@gadarmstadt.de>");
	}
}
