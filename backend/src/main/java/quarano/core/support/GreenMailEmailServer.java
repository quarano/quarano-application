package quarano.core.support;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Arrays;

import javax.mail.Flags.Flag;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.SocketUtils;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

/**
 * <p>
 * Starts a SMTPS-Server with a random port. Use the following properties to use this with Spring Mail:
 * <code>spring.mail.host=127.0.0.1 spring.mail.port={will be set automatically} spring.mail.properties.mail.smtp.socketFactory.class = quarano.core.GreenMailSSLSocketFactory</code>
 * </p>
 * <p>
 * This starts also an IMAPS-Server with port 3993 if this available or a random port. You can use it with an e-mail
 * program to view the received e-mails:
 * <code>host=127.0.0.1 port={3993 or random - you will find in the log outputs}</code>. For each new e-mail address of
 * the received mails, a new account is created from which the mails can be retrieved. Username and password are the
 * mail address. You can also see this in the log outputs.
 * </p>
 * <p>
 * for every received mail the basic data (from, to, date and subject) are logged.
 * </p>
 *
 * @author Jens Kutzsche
 */
@Slf4j
@Service
@Profile({ "integrationtest" })
class GreenMailEmailServer implements FactoryBean<GreenMail>, InitializingBean, DisposableBean {

	private static final int DELETE_AFTER_SECONDS = 600;

	private final Integer smtpPort;
	private GreenMail greenMail;

	GreenMailEmailServer(MailProperties properties) {

		this.smtpPort = SocketUtils.findAvailableTcpPort();

		properties.setPort(smtpPort);
	}

	@Scheduled(fixedDelay = 15000)
	public void receiveAndLogMessages() {

		if (greenMail != null) {

			var deleteThreshold = Instant.now().minusSeconds(DELETE_AFTER_SECONDS);
			var messages = greenMail.getReceivedMessages();

			Arrays.stream(messages)
					.filter(it -> deleteOldMessages(it, deleteThreshold))
					.filter(GreenMailEmailServer::isMailNew)
					.peek(GreenMailEmailServer::markSeen)
					.forEach(GreenMailEmailServer::logMessage);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return GreenMail.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public GreenMail getObject() throws Exception {
		return greenMail;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		int imapPort;
		try {
			imapPort = SocketUtils.findAvailableTcpPort(3993, 3993);
		} catch (Exception e) {
			imapPort = SocketUtils.findAvailableTcpPort();
		}

		var smtp = new ServerSetup(smtpPort, null, ServerSetup.PROTOCOL_SMTPS);
		var imap = new ServerSetup(imapPort, null, ServerSetup.PROTOCOL_IMAPS);

		greenMail = new GreenMail(ServerSetup.verbose(new ServerSetup[] { smtp, imap }));

		log.info("Starting GreenMail mail server on localhost:{} (SMPTS)/{} (IMAPS)…", smtpPort, imapPort);

		greenMail.start();

		log.info("GreenMail mail server started successfully!");
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() {

		if (greenMail != null) {

			log.info("Stopped GreenMail mail server…");

			greenMail.stop();

			log.info("GreenMail mail server stopped successfully!");
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
