package quarano.registration.web;

import de.wevsvirushackathon.coronareport.infrastructure.errorhandling.ArgumentType;
import de.wevsvirushackathon.coronareport.infrastructure.errorhandling.InconsistentDataException;
import de.wevsvirushackathon.coronareport.infrastructure.errorhandling.InvalidArgumentException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.registration.AccountRegistrationDetails;
import quarano.registration.AccountRegistry;
import quarano.registration.ActivationCode.ActivationCodeIdentifier;
import quarano.registration.ActivationCodeExpiredException;
import quarano.registration.ActivationCodeService;
import quarano.registration.ActivationNotActiveException;
import quarano.registration.CodeNotFoundException;
import quarano.registration.InvalidAuthentificationDataException;
import quarano.registration.InvalidUsernameException;
import quarano.user.UserDto;

import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

	private final @NonNull ModelMapper modelMapper;
	private final @NonNull AccountRegistry registry;
	private final @NonNull ActivationCodeService codeService;

	@PostMapping("api/registration")
	public ResponseEntity<String> registerClient(@RequestBody AccountRegistrationDto registrationDto, Errors errors)
			throws CodeNotFoundException, ActivationCodeExpiredException, ActivationNotActiveException,
			InconsistentDataException, InvalidAuthentificationDataException {

		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().build();
		}

		AccountRegistrationDetails details = new AccountRegistrationDetails();
		modelMapper.map(registrationDto, details);

		registry.registerAccountForClient(details);

		return ResponseEntity.ok("");
	}

	@ExceptionHandler
	ResponseEntity<?> handle(CodeNotFoundException o_O) {
		return ResponseEntity.badRequest().body(o_O.getMessage());
	}

	@ExceptionHandler
	ResponseEntity<?> handle(InvalidUsernameException o_O) {
		return ResponseEntity.badRequest().body(o_O.getMessage());
	}

	/**
	 * Checks if the given code exists
	 *
	 * @return true if the code exists, error-json otherwise
	 * @throws EntityNotFoundException
	 * @throws InvalidArgumentException
	 * @throws ActivationNotActiveException
	 * @throws ActivationCodeExpiredException
	 * @throws CodeNotFoundException
	 */
	@ApiOperation(value = "Check if the given activation code is valid", response = UserDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Clientcode does not exist or empty"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@GetMapping("api/registration/checkcode/{activationCode}")
	public ResponseEntity<Boolean> doesClientexist(@PathVariable UUID activationCode) throws InvalidArgumentException,
			CodeNotFoundException, ActivationCodeExpiredException, ActivationNotActiveException {

		if (null == activationCode) {
			throw new InvalidArgumentException("activationCode", "ActivationCode '" + activationCode + "' does not exist.",
					ArgumentType.PATH_VARIABLE, "");
		}

		codeService.getCodeIfValid(ActivationCodeIdentifier.of(activationCode));

		return ResponseEntity.ok(true);
	}

	/**
	 * Checks if the given username is available and complient to username conventions
	 *
	 * @return true if the username is available and valid, error json otherwise
	 * @throws InvalidArgumentException
	 * @throws InvalidUsernameException
	 */
	@ApiOperation(value = "Check if the given activation code is valid", response = UserDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Clientcode does not exist or empty"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@GetMapping("api/registration/checkusername/{userName}")
	public ResponseEntity<Boolean> checkUserName(@PathVariable String userName)
			throws InvalidArgumentException, InvalidUsernameException {

		if (null == userName) {
			throw new InvalidArgumentException("userName", "Username '" + userName + "' not given.",
					ArgumentType.PATH_VARIABLE, "");
		}

		registry.checkIfUserNameAvailableAndValid(userName);

		return ResponseEntity.ok(true);
	}
}
