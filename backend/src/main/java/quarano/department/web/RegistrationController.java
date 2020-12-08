package quarano.department.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.web.AccountRepresentations;
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

import org.springframework.http.HttpEntity;
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

@RestController
@RequiredArgsConstructor
public class RegistrationController {

	private final @NonNull RegistrationManagement registration;
	private final @NonNull ActivationCodeService activationCodes;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull AccountRepresentations accountRepresentations;
	private final @NonNull RegistrationRepresentations representations;

	@PostMapping("/registration")
	public HttpEntity<?> registerClient(@Valid @RequestBody RegistrationDto payload, Errors errors,
			Locale locale) {

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

		return registration.initiateRegistration(trackedCase).toTry()
				.map(it -> representations.toRepresentation(it, trackedCase))
				.fold(it -> ResponseEntity.badRequest().body(it.getMessage()),
						it -> ResponseEntity.ok(it));
	}

	@GetMapping("/hd/cases/{id}/registration")
	HttpEntity<?> getRegistrationDetails(@PathVariable TrackedCaseIdentifier id, @LoggedIn Account account) {

		return cases.findById(id)
				.filter(it -> it.belongsTo(account.getDepartmentId()))
				.map(this::toPendingActivationCode)
				.orElseGet(() -> ResponseEntity.notFound().build());
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

	private HttpEntity<?> toPendingActivationCode(TrackedCase trackedCase) {

		return getPendingActivationCode(trackedCase)
				.<HttpEntity<?>> map(it -> ResponseEntity.ok(representations.toRepresentation(it, trackedCase)))
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
}
