package de.wevsvirushackathon.coronareport.contactperson;

import java.util.ArrayList;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.wevsvirushackathon.coronareport.client.Client;
import de.wevsvirushackathon.coronareport.client.ClientRepository;

@RestController
@RequestMapping("/contact")
class ContactPersonController {

	private ClientRepository clientRepository;
	private ContactPersonRepository repo;
	private ModelMapper modelMapper;

	private ContactPersonController(final ContactPersonRepository repo, final ClientRepository clientRepository,
			final ModelMapper modelMapper) {
		this.repo = repo;
		this.clientRepository = clientRepository;
		this.modelMapper = modelMapper;
		this.modelMapper.getConfiguration().setAmbiguityIgnored(true);
	}

	@GetMapping("/")
	public Iterable<ContactPersonDto> getContacts(Authentication authentication) {
		
		Long clientId = Long.parseLong(authentication.getDetails().toString());

		final Iterable<ContactPerson> entries = this.repo.findAllByClientId(clientId);

		ArrayList<ContactPersonDto> dtos = new ArrayList<>();
		entries.forEach(x -> dtos.add(modelMapper.map(x, ContactPersonDto.class)));
		return dtos;
	}

	@PostMapping("/")
	public ContactPersonDto addContactPerson(@RequestBody ContactPersonDto contactPersonDto,
			@RequestHeader("client-code") String clientCode) {
		final Client client = clientRepository.findByClientCode(clientCode);
		final ContactPerson contactPerson = modelMapper.map(contactPersonDto, ContactPerson.class);
		contactPerson.setClient(client);
		this.repo.save(contactPerson);
		contactPersonDto.setId(contactPerson.getId());
		return contactPersonDto;
	}

	@PutMapping("/{contactId}")
	public ContactPersonDto updateContactPerson(@RequestBody ContactPersonDto contactPersonDto,
			@RequestHeader("client-code") String clientCode, @PathVariable("contactId") Long contactId) {
		final Client client = clientRepository.findByClientCode(clientCode);
		final ContactPerson contactPerson = modelMapper.map(contactPersonDto, ContactPerson.class);
		contactPerson.setClient(client);
		contactPerson.setId(contactId);
		this.repo.save(contactPerson);
		return contactPersonDto;
	}

	@GetMapping("/{contactId}")
	public ResponseEntity<ContactPersonDto> getContactById(@PathVariable("contactId") Long contactId) {

		Optional<ContactPerson> contactOptional = this.repo.findById(contactId);

		return contactOptional.map(contact -> ResponseEntity.ok(modelMapper.map(contact, ContactPersonDto.class)))
				.orElseGet(() -> {
					throw new EntityNotFoundException("Contact with id '" + contactId + "' does not exist");
				});
	}
}
