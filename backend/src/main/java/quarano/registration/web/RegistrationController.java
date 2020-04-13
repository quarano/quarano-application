package quarano.registration.web;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import de.wevsvirushackathon.coronareport.infrastructure.errorhandling.InconsistentDataException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.registration.AccountRegistrationDetails;
import quarano.registration.AccountRegistry;
import quarano.registration.ActivationCodeExpiredException;
import quarano.registration.ActivationNotActiveException;
import quarano.registration.CodeNotFoundException;
import quarano.registration.InvalidAuthentificationDataException;

@Component
@RequiredArgsConstructor
public class RegistrationController {

	private final @NonNull ModelMapper modelMapper;
	private final @NonNull AccountRegistry registry;

	@PostMapping("client/register")
	public ResponseEntity<String> registerClient(@RequestBody AccountRegistrationDto registrationDto)
			throws CodeNotFoundException, ActivationCodeExpiredException, ActivationNotActiveException,
			InconsistentDataException, InvalidAuthentificationDataException {

		AccountRegistrationDetails details = new AccountRegistrationDetails();
		modelMapper.map(registrationDto, details);

		registry.registerAccountForClient(details);

		return ResponseEntity.ok("");
	}
}
