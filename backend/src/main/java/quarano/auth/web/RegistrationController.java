package quarano.auth.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.Account;
import quarano.auth.AccountRegistrationDetails;
import quarano.auth.AccountRegistrationException;
import quarano.auth.AccountService;
import quarano.auth.ActivationCode.ActivationCodeIdentifier;
import quarano.auth.ActivationCodeException;
import quarano.auth.ActivationCodeService;
import quarano.core.web.ErrorsDto;
import quarano.core.web.MapperWrapper;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseRepository;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegistrationController {

	private final @NonNull MapperWrapper mapper;
	private final @NonNull AccountService accounts;
	private final @NonNull ActivationCodeService activationCodes;
	private final @NonNull MessageSourceAccessor messages;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull RegistrationRepresentations representations;

	@PostMapping("/api/registration")
	public HttpEntity<?> registerClient(@Valid @RequestBody AccountRegistrationDto registrationDto, Errors errors) {

		if (registrationDto.validate(errors).hasErrors()) {
			return ErrorsDto.of(errors, messages).toBadRequest();
		}

		var details = mapper.map(registrationDto, AccountRegistrationDetails.class);

		ErrorsDto dto = ErrorsDto.of(errors, messages);

		return accounts.createTrackedPersonAccount(details) //
				.<HttpEntity<?>> map(__ -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build())
				.recover(AccountRegistrationException.class, it -> map(dto, it).toBadRequest()) //
				.recover(ActivationCodeException.class, it -> dto //
						.rejectField("clientCode", "Invalid", it.getMessage()) //
						.toBadRequest())
				.getOrElseGet(it -> ResponseEntity.badRequest().body(it.getMessage()));
	}

	@PutMapping("/api/hd/cases/{id}/registration")
	HttpEntity<?> createRegistration(@PathVariable TrackedCaseIdentifier id, @LoggedIn Account account) {

		var trackedCase = cases.findById(id).orElse(null);
		var departmentId = account.getDepartmentId();

		if (trackedCase == null || !trackedCase.belongsTo(departmentId)) {
			return ResponseEntity.notFound().build();
		}

		return activationCodes.createActivationCode(trackedCase.getTrackedPerson().getId(), departmentId) //
				.map(it -> representations.toRepresentation(it, trackedCase)) //
				.fold(it -> ResponseEntity.badRequest().body(it.getMessage()), //
						it -> ResponseEntity.ok(it));
	}

	@GetMapping("/api/hd/cases/{id}/registration")
	HttpEntity<?> getRegistrationDetails(@PathVariable TrackedCaseIdentifier id, @LoggedIn Account account) {

		return cases.findById(id) //
				.filter(it -> it.belongsTo(account.getDepartmentId())) //
				.map(it -> {

					return activationCodes.getPendingActivationCode(it.getTrackedPerson().getId()) //
							.<HttpEntity<?>> map(code -> ResponseEntity.ok(representations.toRepresentation(code, it))) //
							.orElseGet(() -> ResponseEntity.ok(representations.toNoRegistration(it)));

				}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	private ErrorsDto map(ErrorsDto errors, AccountRegistrationException o_O) {

		switch (o_O.getProblem()) {
			case INVALID_BIRTHDAY:
				return errors.rejectField("birthDate", "Invalid");
			case INVALID_USERNAME:
				return errors.rejectField("username", "Invalid");
			default:
				return errors;
		}
	}

	/**
	 * Checks if the given code exists
	 *
	 * @return true if the code exists, error-json otherwise
	 */
	@GetMapping("api/registration/checkcode/{activationCode}")
	public ResponseEntity<Boolean> doesClientexist(@PathVariable UUID activationCode) {

		return ResponseEntity.ok(activationCodes.getValidCode(ActivationCodeIdentifier.of(activationCode)) //
				.isSuccess());
	}

	/**
	 * Checks if the given username is available and complient to username conventions
	 *
	 * @return true if the username is available and valid, error json otherwise
	 */
	@GetMapping("api/registration/checkusername/{userName}")
	public ResponseEntity<Boolean> checkUserName(@PathVariable String userName) {
		return ResponseEntity.ok(accounts.isUsernameAvailable(userName));
	}
}
