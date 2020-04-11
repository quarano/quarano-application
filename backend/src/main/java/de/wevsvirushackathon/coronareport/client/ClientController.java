package de.wevsvirushackathon.coronareport.client;

import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.wevsvirushackathon.coronareport.infrastructure.errorhandling.ArgumentType;
import de.wevsvirushackathon.coronareport.infrastructure.errorhandling.InvalidArgumentException;
import de.wevsvirushackathon.coronareport.registration.AccountRegistrationDetails;
import de.wevsvirushackathon.coronareport.user.UserDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/client")
public class ClientController {

	private ClientRepository clientRepository;
	private ClientService clientService;
	private ModelMapper modelMapper;

	@Autowired
	public ClientController(ClientRepository clientRepository, ClientService clientService, ModelMapper modelMapper) {
		this.clientService = clientService;
		this.clientRepository = clientRepository;
		this.modelMapper = modelMapper;
	}

	@PostMapping("/{clientId}/register")
	public ResponseEntity<String> registerClient(@RequestBody AccountRegistrationDto registrationDto, @PathVariable Long clientId) {
		
		AccountRegistrationDetails details = new AccountRegistrationDetails();
		modelMapper.map(registrationDto, details) ;
		details.setClientId(clientId);
		
		clientService.registerAccountForClient(details);

		return ResponseEntity.ok("");
	}

	/**
	 * Checks if the given code exists
	 * 
	 * @return true if the code exists, error-json otherwise
	 * @throws EntityNotFoundException
	 * @throws InvalidArgumentException
	 */
	@ApiOperation(value = "Get information about the logged in user", response = UserDto.class)
	@ApiResponses(value = { 
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "Clientcode does not exist or empty"),
		@ApiResponse(code = 500, message = "Internal Server error") })
	@GetMapping("/checkcode/{clientCode}")
	public ResponseEntity<Boolean> doesClientexist(@PathVariable String clientCode) throws InvalidArgumentException {
		
		if(null == clientCode) {
			throw new InvalidArgumentException("clientCode", "ClientCode '"+ clientCode + "' does not exist.", ArgumentType.PATH_VARIABLE, "");
		}
		
		boolean exists = this.clientRepository.existsByClientCode(clientCode);
		if (!exists) {
			throw new EntityNotFoundException("ClientCode '"+ clientCode + "' does not exist.");
		}
		return ResponseEntity.ok(true);
	}

	
	/**
	 * Creates a new unique client id
	 * 
	 * @return
	 */
	private String createNewClientId() {
		Client possiblyExistingClient;
		String newClientCode;
		do {
			newClientCode = UUID.randomUUID().toString();
			possiblyExistingClient = this.clientRepository.findByClientCode(newClientCode);
		} while (possiblyExistingClient != null);
		return newClientCode;
	}
}
