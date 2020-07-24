package quarano.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Arrays;

import javax.mail.Flags.Flag;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;

/**
 * <p>
 * Starts a SMTPS-Server. Use the following properties to use this with Spring Mail:
 * <code>spring.mail.host=127.0.0.1 spring.mail.port=3465 spring.mail.properties.mail.smtp.socketFactory.class = quarano.core.GreenMailSSLSocketFactory</code>
 * </p>
 * <p>
 * This starts also an IMAPS-Server. You can use it with an e-mail program to view the received e-mails:
 * <code>host=127.0.0.1 port=3993</code> For each new e-mail address of the received mails, a new account is created
 * from which the mails can be retrieved. You can also see this in the log outputs. Username and password are the mail
 * address.
 * </p>
 * 
 * @author Jens Kutzsche
 */
@Slf4j
@Service()
@RequiredArgsConstructor
@Order(10)
@Profile("!prod & !junit")
public class EmailServerMock implements ApplicationRunner {

	private static final int DELETE_AFTER_SECONDS = 600;

	private GreenMail greenMail;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		greenMail = new GreenMail(ServerSetup.verbose(ServerSetupTest.SMTPS_IMAPS));
		greenMail.start();
	}

	@EventListener
	public void on(ContextClosedEvent event) {

		if (greenMail != null) {
			greenMail.stop();
		}
	}

	@Scheduled(fixedDelay = 15000)
	public void receiveAndLogMessages() {

		if (greenMail != null) {

			var deleteThreshold = Instant.now().minusSeconds(DELETE_AFTER_SECONDS);
			var messages = greenMail.getReceivedMessages();

			Arrays.stream(messages)
					.filter(it -> deleteOldMessages(it, deleteThreshold))
					.filter(EmailServerMock::isMailNew)
					.peek(EmailServerMock::markSeen)
					.forEach(EmailServerMock::logMessage);
		}
	}

	private static boolean deleteOldMessages(MimeMessage message, Instant deleteThreshold) {

		try {

			if (message.getReceivedDate().toInstant().isBefore(deleteThreshold)) {

				message.setFlag(Flag.DELETED, true);
				return false;
			}

		} catch (MessagingException e) {}

		return true;
	}

	private static boolean isMailNew(MimeMessage message) {

		try {
			return !message.isSet(Flag.SEEN);
		} catch (MessagingException e) {
			return false;
		}
	}

	private static void markSeen(MimeMessage message) {

		try {
			message.setFlag(Flag.SEEN, true);
		} catch (MessagingException e) {}
	}

	private static void logMessage(MimeMessage message) {

		try {

			var from = Arrays.toString(message.getFrom());
			var to = Arrays.toString(message.getAllRecipients());
			var date = message.getReceivedDate() == null
					? ""
					: DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(message.getReceivedDate());
			var subject = String.valueOf(message.getSubject());

			log.debug("Mail received {from {}; to {}; at {}; subject {}}", from, to, date, subject);

		} catch (MessagingException e) {
			log.debug("Mail received but an exception occurred during reception.");
		}
	}
}
