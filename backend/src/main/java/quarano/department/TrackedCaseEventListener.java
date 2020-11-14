package quarano.department;

import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.EmailSender;
import quarano.core.EmailTemplates.Keys;
import quarano.department.TrackedCase.CaseConvertedToIndex;
import quarano.department.TrackedCase.CaseCreated;
import quarano.department.activation.ActivationCode;
import quarano.department.activation.ActivationCodeService;

import java.util.Map;
import java.util.Optional;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
@Slf4j
@Component("department.TrackedCaseEventListener")
@RequiredArgsConstructor
class TrackedCaseEventListener {

	private final @NonNull TrackedCaseRepository cases;

	@EventListener
	void on(CaseConvertedToIndex event) {

		Optional.of(event.getTrackedCase())
				.filter(TrackedCase::isIndexCase)
				.stream()
				.flatMap(CaseSource::forAllContacts)
				.filter(it -> !cases.existsByOriginContacts(it.getPerson()))
				.peek(it -> log.info("Created automatic contact case from contact " + it.getPerson().getId()))
				.map(TrackedCase::of)
				.forEach(cases::save);
	}

	@Component
	@RequiredArgsConstructor
	static class EmailSendingEvents {

		private final @NonNull EmailSender emailSender;
		private final @NonNull RegistrationManagement registration;
		private final @NonNull TrackedCaseRepository cases;
		private final @NonNull MessageSourceAccessor messages;
		private final @NonNull ActivationCodeService activationCodes;
		private final @NonNull RegistrationProperties registrationProperties;

		private boolean initializationFinished = false;

		@EventListener
		void on(ApplicationReadyEvent event) {

			initializationFinished = true;

			emailSender.testConnection()
					.onSuccess(it -> log.info("Successfully connected to mail server at {}!", it))
					.onFailure(it -> {

						if (log.isTraceEnabled()) {
							log.warn("Quarano can't connect the mail server!", it);
						} else {
							log.warn("Quarano can't connect the mail server! {}", it.getMessage());
						}
					});
		}

		@TransactionalEventListener
		void on(CaseCreated event) {

			if (!initializationFinished || !event.getTrackedCase().isContactCase()) {
				return;
			}

			var trackedCase = event.getTrackedCase();

			cases.save(emailSender.testConnection()
					.flatMap(__ -> {
						return registrationProperties.isAutomaticallyInitiateRegistrationForContactCases()
								? registration.initiateRegistration(trackedCase)
								: Try.success(null);
					})
					.flatMap(code -> sendActivationMailFor(trackedCase, Optional.ofNullable(code)))
					.onFailure(__ -> trackedCase.markRegistrationCanceled())
					.fold(__ -> trackedCase.markNewContactCaseMailCantSent(),
							__ -> trackedCase.markNewContactCaseMailSent()));
		}

		private Try<Void> sendActivationMailFor(TrackedCase trackedCase, Optional<ActivationCode> code) {

			var trackedPerson = trackedCase.getTrackedPerson();
			var subject = messages.getMessage("NewContactCaseMail.subject",
					new Object[] { trackedCase.getDepartment().getName() }, trackedPerson.getLocale());
			var logArgs = new Object[] { trackedPerson.getFullName(), String.valueOf(trackedPerson.getEmailAddress()),
					trackedCase.getId().toString() };

			Map<String, String> placeholders = code.map(ActivationCode::getId)
					.map(Object::toString)
					.map(it -> Map.of("activationCode", it))
					.orElseGet(Map::of);

			return emailSender.sendMail(new TrackedCaseEmail(trackedCase, subject, Keys.NEW_CONTACT_CASE, placeholders))
					.onSuccess(__ -> activationCodes.codeMailed(code.getId()))
					.onSuccess(__ -> log.info("Contact case creation mail sent to {{}; {}; Case-ID {}}", logArgs))
					.onFailure(__ -> code.map(ActivationCode::getId).map(activationCodes::cancelCode))
					.onFailure(e -> log.info("Can't send contact case creation mail to {{}; {}; Case-ID {}}", logArgs))
					.onFailure(e -> log.info("Exception", e));
		}
	}
}
