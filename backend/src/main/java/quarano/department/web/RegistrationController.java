package quarano.department.web;

import static java.util.function.Predicate.*;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.Account;
import quarano.account.web.AccountRepresentations;
import quarano.core.EmailSender;
import quarano.core.web.I18nedMessage;
import quarano.core.web.LoggedIn;
import quarano.core.web.MappedPayloads;
import quarano.core.web.MappedPayloads.MappedErrors;
import quarano.department.RegistrationException;
import quarano.department.RegistrationException.Problem;
import quarano.department.RegistrationManagement;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseRepository;
import quarano.department.activation.ActivationCode;
import quarano.department.activation.ActivationCode.ActivationCodeIdentifier;
import quarano.department.activation.ActivationCodeException;
import quarano.department.activation.ActivationCodeService;
import quarano.department.web.RegistrationRepresentations.RegistrationDto;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

	private final @NonNull RegistrationManagement registration;
	private final @NonNull ActivationCodeService activationCodes;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull AccountRepresentations accountRepresentations;
	private final @NonNull RegistrationRepresentations representations;
	private final @NonNull EmailSender emailSender;
	private final @NonNull MessageSourceAccessor messages;

	private final AcceptHeaderLocaleResolver requestLocaleResolver = new AcceptHeaderLocaleResolver();

	@PostMapping("/registration")
	public HttpEntity<?> registerClient(@Valid @RequestBody RegistrationDto payload, Errors errors,
			Locale locale) {

		// Use locale solely from the request to potentially set it as preferred
		// one on the account to be created. A Locale method parameter would
		// Locale locale = requestLocaleResolver.resolveLocale(request);

		return MappedPayloads.of(payload, errors)
				.alwaysMap(RegistrationDto::validate)
				.concludeSelfIfValid((p, e) -> doRegisterClient(p, e, locale));
	}

	@PutMapping("/hd/cases/{id}/registration")
	public HttpEntity<?> createRegistration(@PathVariable TrackedCaseIdentifier id, @LoggedIn Account account) {

		var trackedCase = cases.findById(id).orElse(null);
		var departmentId = account.getDepartmentId();

		if (trackedCase == null || !trackedCase.belongsTo(departmentId)) {
			return ResponseEntity.notFound().build();
		}

		return registration.initiateRegistration(trackedCase)
				.map(it -> representations.toRepresentation(it, trackedCase, account))
				.fold(it -> ResponseEntity.badRequest().body(it.getMessage()),
						it -> ResponseEntity.ok(it));
	}

	@GetMapping("/hd/cases/{id}/registration")
	HttpEntity<?> getRegistrationDetails(@PathVariable TrackedCaseIdentifier id, @LoggedIn Account account) {

		return cases.findById(id)
				.filter(it -> it.belongsTo(account.getDepartmentId()))
				.map(it -> toPendingActivationCode(it, account))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * @param id The ID of the tracked case for which the registration mail is to be sent.
	 * @param account
	 * @since 1.4
	 */
	@PostMapping("/hd/cases/{id}/registration/mail")
	HttpEntity<?> sendMail(@PathVariable TrackedCaseIdentifier id, @LoggedIn Account account) {

		var trackedCase = cases.findById(id)
				.filter(it -> it.belongsTo(account.getDepartmentId()))
				.orElse(null);

		return Option.of(trackedCase)
				.toEither(ResponseEntity.notFound()::build)
				.filterOrElse(TrackedCase::isEmailAvailable,
						__ -> ResponseEntity.unprocessableEntity().body(I18nedMessage.of("NotEmpty.email")))
				.flatMap(it -> Option.ofOptional(getPendingActivationCode(it))
						.toEither(ResponseEntity.notFound()::build))
				.filterOrElse(not(ActivationCode::isMailed),
						__ -> ResponseEntity.unprocessableEntity().body(I18nedMessage.of("activationCode.alreadyMailed")))
				.flatMap(code -> {

					return sendActivationMailFor(trackedCase, code, account)
							.map(__ -> representations.toRepresentation(code, trackedCase, account))
							.toEither(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
									.body(I18nedMessage.of("mailService.unavailable")));
				})
				.fold(it -> it, ResponseEntity::ok);
	}

	/**
	 * Checks if the given code exists
	 *
	 * @return true if the code exists, error-json otherwise
	 */
	@GetMapping("/registration/checkcode/{activationCode}")
	public ResponseEntity<Boolean> doesClientexist(@PathVariable UUID activationCode) {

		return ResponseEntity.ok(activationCodes.getValidCode(ActivationCodeIdentifier.of(activationCode))
				.isSuccess());
	}

	/**
	 * Checks if the given username is available and compliant to username conventions
	 *
	 * @return true if the username is available and valid, error JSON otherwise
	 */
	@GetMapping("/registration/checkusername/{userName}")
	public ResponseEntity<Boolean> checkUserName(@PathVariable String userName) {
		return ResponseEntity.ok(registration.isUsernameAvailable(userName));
	}

	private HttpEntity<?> doRegisterClient(RegistrationDto payload, MappedErrors errors, Locale locale) {

		return registration.createTrackedPersonAccount(representations.from(payload).setLocale(locale))
				.peek(it -> SecurityContextHolder.getContext()
						.setAuthentication(new PreAuthenticatedAuthenticationToken(it, null)))
				.<HttpEntity<?>> map(accountRepresentations::toTokenResponse)
				.recover(RegistrationException.class, it -> recover(it, errors))
				.recover(ActivationCodeException.class, it -> recover(it, errors))
				.getOrElseGet(it -> ResponseEntity.badRequest().body(it.getMessage()));
	}

	private HttpEntity<?> toPendingActivationCode(TrackedCase trackedCase, Account officeStaff) {

		return getPendingActivationCode(trackedCase)
				.<HttpEntity<?>> map(it -> ResponseEntity.ok(representations.toRepresentation(it, trackedCase, officeStaff)))
				.orElseGet(() -> ResponseEntity.ok(representations.toNoRegistration(trackedCase)));
	}

	private Optional<ActivationCode> getPendingActivationCode(TrackedCase trackedCase) {

		var personId = trackedCase.getTrackedPerson().getId();

		return registration.getPendingActivationCode(personId);
	}

	private static HttpEntity<?> recover(RegistrationException it, MappedErrors errors) {

		return errors
				.rejectField(it.getProblem().equals(Problem.INVALID_USERNAME), "username", it.getMessage())
				.rejectField(it.getProblem().equals(Problem.INVALID_BIRTHDAY), "dateOfBirth", it.getMessage())
				.toBadRequest();
	}

	private static HttpEntity<?> recover(ActivationCodeException it, MappedErrors errors) {
		return errors.rejectField("clientCode", it.getMessageKey()).toBadRequest();
	}

	private Try<Void> sendActivationMailFor(TrackedCase trackedCase, ActivationCode code, Account officeStaff) {

		var trackedPerson = trackedCase.getTrackedPerson();
		var logArgs = new Object[] { trackedPerson.getFullName(), String.valueOf(trackedPerson.getEmailAddress()),
				trackedCase.getId().toString() };

		var email = representations.getTemplatedEmailFor(trackedCase, code, officeStaff);

		return emailSender.sendMail(email)
				.onSuccess(__ -> activationCodes.codeMailed(code.getId()))
				.onSuccess(__ -> log.info("Contact registration mail sent to {{}; {}; Case-ID {}}", logArgs))
				.onFailure(e -> log.info("Can't send registration mail to {{}; {}; Case-ID {}}", logArgs))
				.onFailure(e -> log.info("Exception", e));
	}
}
