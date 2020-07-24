package quarano.core;

import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.DepartmentContact;
import quarano.account.DepartmentContact.ContactType;
import quarano.core.EmailTemplates.Keys;
import quarano.department.TrackedCase;

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
 * @author Jens Kutzsche
 */
@Service
@RequiredArgsConstructor
public class EmailSender {

	private static final String ADRESS_FORMAT = "%s <%s>";

	private final @NonNull JavaMailSenderImpl emailSender;
	private final @NonNull EmailTemplates templates;
	private final @NonNull CoreProperties configuration;

	public Try<Void> testConnection() {
		return Try.run(() -> emailSender.testConnection());
	}

	public Try<Void> sendMail(TrackedCase trackedCase, String subject, Keys template,
			Supplier<Try<Map<String, Object>>> placeholders) {

		return testConnection()
				.flatMap(it -> createMail(trackedCase, subject, template, placeholders))
				.flatMap(this::sendMail);
	}

	private Try<SimpleMailMessage> createMail(TrackedCase trackedCase, String subject, Keys template,
			Supplier<Try<Map<String, Object>>> placeholders) {

		var trackedPerson = trackedCase.getTrackedPerson();
		var department = trackedCase.getDepartment();
		var emailAddress = trackedPerson.getEmailAddress();

		if (emailAddress == null) {
			return Try.failure(new IllegalArgumentException("Email address of contact person is missing!"));
		}

		var departmentMailAddress = department.getContact(ContactType.CONTACT)
				.or(() -> department.getContact(ContactType.INDEX))
				.map(DepartmentContact::getEmailAddress);

		if (departmentMailAddress.isPresent()) {

			var from = String.format(ADRESS_FORMAT, department.getName(), departmentMailAddress.get());
			var to = String.format(ADRESS_FORMAT, trackedPerson.getFullName(), emailAddress);

			return placeholders.get()
					.map(it -> getMailTemplateFor(template, trackedCase, it))
					.map(text -> createMail(from, to, subject, text));
		}

		return Try.failure(new IllegalArgumentException("Email address of department is missing!"));
	}

	private String getMailTemplateFor(Keys template, TrackedCase trackedCase, Map<String, Object> additionPlaceholders) {

		Map<String, Object> placeholders = new HashMap<>();

		placeholders.put("lastName", trackedCase.getTrackedPerson().getLastName());
		placeholders.put("host", configuration.getHost());

		placeholders.putAll(additionPlaceholders);

		return templates.getTemplate(template, placeholders);
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
}
