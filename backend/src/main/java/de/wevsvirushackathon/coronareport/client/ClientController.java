package de.wevsvirushackathon.coronareport.client;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.wevsvirushackathon.coronareport.infrastructure.errorhandling.ArgumentType;
import de.wevsvirushackathon.coronareport.infrastructure.errorhandling.InvalidArgumentException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import quarano.user.UserDto;

@RestController
@RequestMapping("/client")
public class ClientController {

	private ClientRepository clientRepository;
	private ModelMapper modelMapper;

	@Autowired
	public ClientController(ClientRepository clientRepository, ModelMapper modelMapper) {
		this.clientRepository = clientRepository;
		this.modelMapper = modelMapper;
	}
	

}
