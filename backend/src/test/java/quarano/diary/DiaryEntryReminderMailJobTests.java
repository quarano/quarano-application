package quarano.diary;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import quarano.QuaranoIntegrationTest;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;

import java.util.Locale;

@RequiredArgsConstructor
@QuaranoIntegrationTest
class DiaryEntryReminderMailJobTests {

	private final DiaryEntryReminderMailJob job;
	private final GreenMail greenMail;
	private final MessageSourceAccessor messages;

	@BeforeEach
	void resetGreenMail() throws Exception {
		greenMail.purgeEmailFromAllMailboxes();
	}

	@Test // CORE-61
	void runsCorrect() throws MessagingException {

		job.checkForReminderMail();

		// wait for max 5s for 1 email to arrive
		assertTrue(greenMail.waitForIncomingEmail(3), "not all emails sented");

		var subjectGerman = messages.getMessage("DiaryEntryReminderMail.subject", Locale.GERMAN);
		var subjectEnglish = messages.getMessage("DiaryEntryReminderMail.subject", Locale.ENGLISH);

		Message[] mails = greenMail.getReceivedMessages();
		assertThat(mails).hasSize(3);

		assertThat(mails).extracting(it -> it.getSubject())
				.allMatch(it -> it.equals(subjectEnglish) || it.equals(subjectGerman));
		assertThat(mails).extracting(it -> GreenMailUtil.getBody(it))
				.allMatch(it -> it.contains(" record your regular diary") || it.contains(" Tagebucheintrag"));

		assertThat(mails).extracting(it -> it.getRecipients(RecipientType.TO)[0].toString())
				.containsOnly("Siggi Seufert <siggi@testtest.de>",
						"Sandra Schubert <sandra.schubert@testtest.de>",
						"Gustav Meier <gustav.meier@testtest.de>");

		assertThat(mails).extracting(it -> it.getFrom()[0].toString()).containsOnly(
				"GA Mannheim <index-email@gesundheitsamt.de>",
				"GA Darmstadt <index-email@gadarmstadt.de>");
	}
}
