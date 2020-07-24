package quarano.department;

import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.EmailSender;
import quarano.core.EmailTemplates.Keys;
import quarano.department.TrackedCase.CaseCreated;

import java.util.Map;
import java.util.function.Supplier;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * This is an <code>ApplicationRunner</code> to register the end of the initial phase
 * 
 * @author Jens Kutzsche
 */
@Slf4j
@Service()
@RequiredArgsConstructor
@Order()
class MailForNewContactCaseProcessor implements ApplicationRunner {

	private final @NonNull EmailSender emailSender;
	private final @NonNull RegistrationManagement registration;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull MessageSourceAccessor messages;

	private boolean initializationFinished = false;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		initializationFinished = true;

		emailSender.testConnection().onFailure(it -> log.warn("Quarano can't connect the mail server!", it));
	}

	@TransactionalEventListener
	void on(CaseCreated event) {

		if (!initializationFinished) {
			return;
		}

		var trackedCase = event.getTrackedCase();
		var trackedPerson = trackedCase.getTrackedPerson();
		var subject = messages.getMessage("NewContactCaseMail.subject",
				new Object[] { trackedCase.getDepartment().getName() });
		var textTemplate = Keys.NEW_CONTACT_CASE;
		var logArgs = new Object[] { trackedPerson.getFullName(), String.valueOf(trackedPerson.getEmailAddress()),
				trackedCase.getId().toString() };

		Supplier<Try<Map<String, Object>>> activationPlaceholders = () -> registration.initiateRegistration(trackedCase)
				.map(it -> Map.of("activationCode", it.getId().toString()));

		var processedCase = emailSender.sendMail(trackedCase, subject, textTemplate, activationPlaceholders)
				.onSuccess(it -> log.info("Contact case creation mail sended to {{}; {}; Case-ID {}}", logArgs))
				.onFailure(e -> log.info("Can't send contact case creation mail to {{}; {}; Case-ID {}}", logArgs))
				.onFailure(e -> log.info("Exception", e))
				.fold(it -> trackedCase.markNewContactCaseMailCantSent(), it -> trackedCase.markNewContactCaseMailCantSented());

		cases.save(processedCase);
	}
}
