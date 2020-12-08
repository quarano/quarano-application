package quarano.department;

import io.jsonwebtoken.lang.Assert;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.CoreProperties;
import quarano.core.EmailSender;
import quarano.core.EmailTemplates.Keys;
import quarano.department.CommentFactory.CommentKey;
import quarano.department.activation.ActivationCode;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/**
 * Component to send out emails for a {@link TrackedCase}.
 *
 * @author Oliver Drotbohm
 * @since 1.4
 */
@Slf4j
@Component
@RequiredArgsConstructor
class TrackedCaseEmails implements RegistrationEmailProvider {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	private final @NonNull EmailSender emailSender;
	private final @NonNull MessageSourceAccessor messages;
	private final @NonNull CommentFactory comments;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull CoreProperties configuration;

	/**
	 * Sends out an information email for new contact cases.
	 *
	 * @param trackedCase must not be {@literal null}.
	 * @param handlers must not be {@literal null}.
	 * @return
	 */
	@SuppressWarnings("null")
	Try<Void> sendContactCaseInformationEmail(TrackedCase trackedCase, ResultHandlers handlers) {

		Assert.isTrue(trackedCase.isContactCase(), "Given case must be a contact case!");
		Assert.notNull(handlers, "ResultHandlers must not be null!");

		var parameters = Collections.<String, Object> emptyMap();
		var subject = messages.getMessage("NewContactCaseMail.subject",
				new Object[] { trackedCase.getDepartment().getName() }, trackedCase.getTrackedPerson().getLocale());

		var email = new TrackedCaseEmail(trackedCase, subject, Keys.NEW_CONTACT_CASE, parameters);

		return sendMail(email, handlers, CommentKey.REGISTRATION__WELCOME_EMAIL_SENT);
	}

	/**
	 * Sends out an email with registration information
	 *
	 * @param trackedCase
	 * @param code
	 * @param handlers
	 * @return
	 */
	Try<Void> sendRegistrationEmail(TrackedCase trackedCase, ActivationCode code, ResultHandlers handlers) {

		var email = getRegistrationEmail(trackedCase, code);

		return sendMail(email, handlers, CommentKey.REGISTRATION__EMAIL_SENT);
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.department.RegistrationEmailProvider#getRegistrationEmail(quarano.department.TrackedCase, quarano.department.activation.ActivationCode)
	 */
	@SuppressWarnings("null")
	public TrackedCaseEmail getRegistrationEmail(TrackedCase trackedCase, ActivationCode code) {

		Assert.notNull(trackedCase, "TrackedCase must not be null!");
		Assert.notNull(code, "ActivationCode must not be null!");

		var trackedPerson = trackedCase.getTrackedPerson();
		var parameters = createRegistrationMailPlaceholders(trackedCase, code);
		var mailKey = trackedCase.isIndexCase() ? Keys.REGISTRATION_INDEX : Keys.REGISTRATION_CONTACT;
		var subject = messages.getMessage("RegistriationMail.subject",
				new Object[] { trackedCase.getDepartment().getName() }, trackedPerson.getLocale());

		return new TrackedCaseEmail(trackedCase, subject, mailKey, parameters);
	}

	private Try<Void> sendMail(TrackedCaseEmail email, ResultHandlers handlers, CommentKey key) {

		var trackedCase = email.getTrackedCase();
		var trackedPerson = trackedCase.getTrackedPerson();
		var logArgs = new Object[] { email.getTemplate(),
				trackedPerson.getFullName(),
				String.valueOf(trackedPerson.getEmailAddress()),
				trackedCase.getId().toString() };

		Consumer<TrackedCase> defaultSuccess = it -> {

			log.info("Mail {} sent to {{}; {}; Case-ID {}}", logArgs);

			it.addComment(comments.successComment(key));
		};

		BiConsumer<TrackedCase, Throwable> defaultFailure = (it, ex) -> {

			log.info("Can't send mail {} to {{}; {}; Case-ID {}}", logArgs);
			log.info("Exception", ex);

			it.addComment(comments.failureComment(key));
		};

		return emailSender.sendMail(email)
				.onSuccess(__ -> handlers.andOnSuccess(defaultSuccess).successHandler.accept(trackedCase))
				.onFailure(e -> handlers.andOnFailure(defaultFailure).failureHandler.accept(trackedCase, e))
				.andFinally(() -> cases.save(trackedCase));
	}

	private Map<String, Object> createRegistrationMailPlaceholders(TrackedCase trackedCase, ActivationCode code) {

		var placeholders = new HashMap<String, Object>();

		placeholders.put("lastName", trackedCase.getTrackedPerson().getLastName());
		placeholders.put("departmentName", trackedCase.getDepartment().getName());
		placeholders.put("host", configuration.getHost());
		placeholders.put("activationCode", code.getId().toString());

		var quarantine = trackedCase.getQuarantine();

		if (quarantine != null) {
			placeholders.put("quarantineEndDate", quarantine.getTo().format(FORMATTER));
		}

		return placeholders;
	}

	/**
	 * Collects success and failure handlers to apply them all on a {@link TrackedCase}.
	 *
	 * @author Oliver Drotbohm
	 */
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	static class ResultHandlers {

		final Consumer<TrackedCase> successHandler;
		final BiConsumer<TrackedCase, Throwable> failureHandler;

		public static ResultHandlers none() {
			return new ResultHandlers(__ -> {}, (l, r) -> {});
		}

		public ResultHandlers andOnSuccess(Consumer<TrackedCase> handler) {
			return new ResultHandlers(successHandler.andThen(handler), failureHandler);
		}

		public ResultHandlers andOnFailure(Consumer<TrackedCase> handler) {
			return andOnFailure((BiConsumer<TrackedCase, Throwable>) (l, __) -> handler.accept(l));
		}

		public ResultHandlers andOnFailure(BiConsumer<TrackedCase, Throwable> handler) {
			return new ResultHandlers(successHandler, failureHandler.andThen(handler));
		}
	}
}
