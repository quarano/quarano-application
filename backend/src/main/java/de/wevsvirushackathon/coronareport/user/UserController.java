package de.wevsvirushackathon.coronareport.user;

import java.text.ParseException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.wevsvirushackathon.coronareport.authentication.Account;
import de.wevsvirushackathon.coronareport.authentication.AccountRepository;
import de.wevsvirushackathon.coronareport.client.Client;
import de.wevsvirushackathon.coronareport.client.ClientDto;
import de.wevsvirushackathon.coronareport.client.ClientRepository;
import de.wevsvirushackathon.coronareport.client.MissingClientException;
import de.wevsvirushackathon.coronareport.healthdepartment.HealthDepartment;
import de.wevsvirushackathon.coronareport.healthdepartment.HealthDepartmentRepository;
import de.wevsvirushackathon.coronareport.infrastructure.errorhandling.InconsistentDataException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/user")
public class UserController {

	private ClientRepository clientRepository;
	private AccountRepository accountRepository;
	private HealthDepartmentRepository hdRepository;
	private ModelMapper modelMapper;

	@Autowired
	public UserController(ClientRepository clientRepository, AccountRepository accountRepository,
			HealthDepartmentRepository hdRepository, ModelMapper modelMapper) {
		this.clientRepository = clientRepository;
		this.accountRepository = accountRepository;
		this.hdRepository = hdRepository;
		this.modelMapper= modelMapper;
	}

	/**
	 * Retrieves information of the tracked case, that is currently logged in
	 * 
	 * @return a clientDto of the authenticated user
	 * @throws UserNotFoundException
	 * @throws InconsistentDataException
	 * @throws ParseException
	 */
	@ApiOperation(value = "Get information about the logged in user", response = UserDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 403, message = "Not authorized, if there is no session"),
			@ApiResponse(code = 404, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@GetMapping("/me")
	public ResponseEntity<UserDto> getMe(Authentication authentication)
			throws UserNotFoundException, InconsistentDataException {

		final String usernameFromAutentication = authentication.getPrincipal().toString();
		UserDto userDto = new UserDto().withUsername(usernameFromAutentication);

		// if user is a client
		if (authentication.getDetails() != null) {

			// fetch client of authenticated account
			long clientId = Long.parseLong(authentication.getDetails().toString());

			final Client client = this.clientRepository.findById(clientId)
					.orElseThrow(() -> new MissingClientException(
							"No Client found for username '" + authentication.getPrincipal(),
							authentication.getPrincipal().toString() + "'"));

			userDto.setClient(modelMapper.map(client, ClientDto.class));
			userDto.setFirstname(client.getFirstname());
			userDto.setSurename(client.getSurename());
			userDto.setHealthDepartment(client.getHealthDepartment());
		} else {
			// user is no client, it is a employee of the health department
			Account account = accountRepository.findOneByUsername(usernameFromAutentication)
					.orElseThrow(() -> new UserNotFoundException(usernameFromAutentication));

			HealthDepartment hd = hdRepository.findById(account.getHdId())
					.orElseThrow(() -> new InconsistentDataException(
							"No healthdepartment found for hdid from account : '" + account.getHdId() + "'"));
			userDto.setHealthDepartment(hd);

			userDto.setFirstname(account.getFirstname());
			userDto.setSurename(account.getLastname());
		}

		return ResponseEntity.ok(userDto);
	}

}
