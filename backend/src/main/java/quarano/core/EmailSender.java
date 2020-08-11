package quarano.core;

import static org.apache.commons.lang3.StringUtils.*;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.core.EmailTemplates.Keys;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

/**
 * Generic email sender for Quarano that uses the templates of <code>EmailTemplates</code> as body. The data for a email
 * are given with an instance of <code>EmailData</code>.
 * <p>
 * The EmailSender uses <strong>lastName = <code>getToLastName()</code> und host = {host from configuration}</strong> as
 * default placeholders with the template.
 * </p>
 * 
 * @author Jens Kutzsche
 */
@Service
@RequiredArgsConstructor
public class EmailSender {

	private final @NonNull JavaMailSenderImpl emailSender;
	private final @NonNull EmailTemplates templates;
	private final @NonNull CoreProperties configuration;

	public Try<Void> testConnection() {
		return Try.run(() -> emailSender.testConnection());
	}

	public Try<Void> sendMail(EmailData emailData) {

		return testConnection()
				.flatMap(x -> createMail(emailData))
				.flatMap(this::sendMail);
	}

	private Try<SimpleMailMessage> createMail(EmailData emailData) {

		if (emailData.getFrom().isEmpty()) {
			return Try.failure(new IllegalArgumentException("Email address of department is missing!"));
		} else if (emailData.getTo().isEmpty()) {
			return Try.failure(new IllegalArgumentException("Email address of recipient is missing!"));
		} else {

			return emailData.getPlaceholders().get()
					.map(it -> getMailTemplateFor(emailData, it))
					.map(text -> createMail(emailData.getFrom().get(), emailData.getTo().get(), emailData.getSubject(), text));
		}
	}

	private String getMailTemplateFor(EmailData emailData, Map<String, Object> additionPlaceholders) {

		Map<String, Object> placeholders = new HashMap<>();

		placeholders.put("lastName", defaultString(emailData.getToLastName()));
		placeholders.put("host", configuration.getHost());

		placeholders.putAll(additionPlaceholders);

		return templates.getTemplate(emailData.getTemplate(), placeholders);
	}

	private SimpleMailMessage createMail(String from, String to, String subject, String text) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		message.setSentDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

		return message;
	}

	private Try<Void> sendMail(SimpleMailMessage mail) {
		return Try.runRunnable(() -> emailSender.send(mail));
	}

	/**
	 * The data for a email.
	 * 
	 * @author Jens Kutzsche
	 */
	public static interface EmailData {

		static final String ADRESS_FORMAT = "%s <%s>";

		/**
		 * @return The complete FROM string (... <...@...>) for the email or <code>Empty</code> if there no address
		 *         (<code>getFromEmailAddress()</code> is Null or blank).
		 */
		default Option<String> getFrom() {

			var address = getFromEmailAddress();

			if (address == null) {
				return Option.none();
			}

			return Option.of(String.format(ADRESS_FORMAT, defaultString(getFromFullName()), address));
		}

		/**
		 * @return The complete TO string (... <...@...>) for the email or <code>Empty</code> if there no address
		 *         (<code>getToEmailAddress()</code> is Null or blank).
		 */
		default Option<String> getTo() {

			var address = getToEmailAddress();

			if (address == null) {
				return Option.none();
			}

			return Option.of(String.format(ADRESS_FORMAT, defaultString(getToFullName()), address));
		}

		/**
		 * @return The last name of the recipient. This will used for default a placeholder of the template.
		 */
		String getToLastName();

		/**
		 * @return The email address of the recipient.
		 */
		EmailAddress getToEmailAddress();

		/**
		 * @return The full name of the recipient.
		 */
		String getToFullName();

		/**
		 * @return The email address of the sender.
		 */
		EmailAddress getFromEmailAddress();

		/**
		 * @return The full name of the sender.
		 */
		String getFromFullName();

		/**
		 * @return The key of the template which should be used for the body.
		 */
		Keys getTemplate();

		/**
		 * @return The email subject.
		 */
		String getSubject();

		/**
		 * lastName and host are default placeholders (see also <code>EmailSender</code>).
		 * 
		 * @return The supplier of the placeholders used in template. The supplier will used lazy and can signal an error
		 *         with returns a <code>Try.Failure</code>.
		 */
		Supplier<Try<Map<String, Object>>> getPlaceholders();
	}

	@AllArgsConstructor
	public static abstract class BasicEmailData implements EmailData {

		private @Getter String subject;
		private @Getter Keys template;
		private @Getter Supplier<Try<Map<String, Object>>> placeholders;
	}
}
