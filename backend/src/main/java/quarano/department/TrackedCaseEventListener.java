package quarano.department;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.AsyncTransactionalEventListener;
import quarano.core.EmailSender;
import quarano.department.TrackedCase.CaseConvertedToIndex;
import quarano.department.TrackedCase.CaseCreated;
import quarano.department.TrackedCase.RegistrationInitiated;
import quarano.department.TrackedCaseEmails.ResultHandlers;
import quarano.department.activation.ActivationCodeService;

import java.util.Optional;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
@Slf4j
@Component("department.TrackedCaseEventListener")
@RequiredArgsConstructor
class TrackedCaseEventListener {

	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull RegistrationManagement registration;
	private final @NonNull RegistrationProperties configuration;
	private final @NonNull CommentFactory comments;

	/**
	 * Creates new contact cases instances for all origin cases of an index case upon case conversion (contact case ->
	 * index case).
	 *
	 * @param event will never be {@literal null}.
	 */
	@EventListener
	void onCaseConvertedToIndex(CaseConvertedToIndex event) {

		Optional.of(event.getTrackedCase())
				.filter(TrackedCase::isIndexCase)
				.stream()
				.flatMap(CaseSource::forAllContacts)
				.filter(it -> !cases.existsByOriginContacts(it.getPerson()))
				.peek(it -> log.info("Created automatic contact case from contact " + it.getPerson().getId()))
				.map(TrackedCase::of)
				.forEach(cases::save);
	}

	/**
	 * Initiates the registration when a contact case was created and the relevant flag was enabled on the configuration.
	 *
	 * @param event must not be {@literal null}.
	 * @see RegistrationProperties#isAutomaticallyInitiateRegistrationForContactCases()
	 */
	@EventListener
	void onCaseCreated(CaseCreated event) {

		var trackedCase = cases.findById(event.getTrackedCase()).orElseThrow();

		if (!trackedCase.isContactCase()) {
			return;
		}

		if (configuration.isAutomaticallyInitiateRegistrationForContactCases()) {
			registration.initiateRegistration(trackedCase);
		}
	}

	@Component
	@RequiredArgsConstructor
	static class EmailSendingEvents {

		private final @NonNull EmailSender emailSender;
		private final @NonNull TrackedCaseRepository cases;
		private final @NonNull RegistrationProperties registrationProperties;
		private final @NonNull ActivationCodeService codes;
		private final @NonNull TrackedCaseEmails emails;

		/**
		 * Initializes email handling after application startup. This is in place so that data setup triggered during
		 * application start doesn't trigger emails being sent.
		 *
		 * @param event will never be {@literal null}.
		 */
		@EventListener
		void onApplicationReady(ApplicationReadyEvent event) {

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

		/**
		 * Issues the registration email being set out containing the generated activation code. Failure to send an email
		 * will cause the case be marked as registration canceled.
		 *
		 * @param event will never be {@literal null}.
		 */
		@AsyncTransactionalEventListener
		void onRegistrationInitiated(RegistrationInitiated event) {

			var trackedCase = cases.findById(event.getCaseIdentifier()).orElseThrow();
			var code = codes.getPendingActivationCode(trackedCase.getTrackedPerson().getId()).orElseThrow();
			var handlers = ResultHandlers.none()
					.andOnFailure(it -> it.markRegistrationCanceled());

			emails.sendRegistrationEmail(trackedCase, code, handlers);
		}

		/**
		 * Sends out an information email for contact cases unless the configuration option to immediately start the
		 * registration process has been enabled.
		 *
		 * @param event will never be {@literal null}.
		 * @see RegistrationProperties#isAutomaticallyInitiateRegistrationForContactCases()
		 */
		@AsyncTransactionalEventListener
		void onCaseCreated(CaseCreated event) {
			onInternal(event);
		}

		void onInternal(CaseCreated event) {

			var trackedCase = cases.findById(event.getTrackedCase()).orElseThrow();

			if (!trackedCase.isContactCase() || !trackedCase.isEmailAvailable()) {
				return;
			}

			if (!registrationProperties.isAutomaticallyInitiateRegistrationForContactCases()) {
				emails.sendContactCaseInformationEmail(trackedCase, ResultHandlers.none());
			}
		}
	}
}
