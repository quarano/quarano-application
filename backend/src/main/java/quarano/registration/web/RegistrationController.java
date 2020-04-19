package quarano.registration.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.AccountRegistrationDetails;
import quarano.auth.AccountService;
import quarano.core.web.ErrorsDto;
import quarano.core.web.MapperWrapper;
import quarano.registration.ActivationCode.ActivationCodeIdentifier;
import quarano.registration.ActivationCodeService;
import quarano.user.UserDto;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

	private final @NonNull MapperWrapper mapper;
	private final @NonNull AccountService accounts;
	private final @NonNull ActivationCodeService activationCodes;
	private final @NonNull MessageSourceAccessor messages;

	@PostMapping("api/registration")
	public HttpEntity<?> registerClient(@Valid @RequestBody AccountRegistrationDto registrationDto, Errors errors) {

		if (registrationDto.validate(errors).hasErrors()) {
			return ErrorsDto.toBadRequest(errors, messages);
		}

		return accounts.registerAccountForClient(mapper.map(registrationDto, AccountRegistrationDetails.class)) //
				.fold(it -> ResponseEntity.badRequest().body(it.getMessage()),
						__ -> ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).build());
	}

	/**
	 * Checks if the given code exists
	 *
	 * @return true if the code exists, error-json otherwise
	 */
	@ApiOperation(value = "Check if the given activation code is valid", response = UserDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Clientcode does not exist or empty"),
			@ApiResponse(code = 500, message = "Internal Server error") })
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
	@ApiOperation(value = "Check if the given activation code is valid", response = UserDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Clientcode does not exist or empty"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@GetMapping("api/registration/checkusername/{userName}")
	public ResponseEntity<Boolean> checkUserName(@PathVariable String userName) {
		return ResponseEntity.ok(accounts.isUsernameAvailable(userName));
	}
}
