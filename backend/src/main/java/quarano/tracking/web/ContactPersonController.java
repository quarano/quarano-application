package quarano.tracking.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.core.web.LoggedIn;
import quarano.core.web.MappedPayloads;
import quarano.core.web.MapperWrapper;
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.ContactPersonRepository;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonManagement;

import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Oliver Drotbohm
 */
@Transactional
@RestController("newContactPersonController")
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactPersonController {

	private final @NonNull MapperWrapper mapper;
	private final @NonNull TrackedPersonManagement people;
	private final @NonNull ContactPersonRepository contacts;
	private final @NonNull MessageSourceAccessor messages;

	@GetMapping
	Stream<ContactPersonDto> allContacts(@LoggedIn TrackedPerson person) {

		return contacts.findByOwnerId(person.getId())
				.map(it -> mapper.map(it, ContactPersonDto.class))
				.stream();
	}

	@PostMapping
	HttpEntity<?> createContactPerson(@Valid @RequestBody ContactPersonDto dto, Errors errors,
			@LoggedIn TrackedPerson person) {

		return MappedPayloads.of(dto, errors)
				.alwaysMap(ContactPersonDto::validate)
				.map(it -> mapper.map(it, ContactPerson.class))
				.map(it -> it.assignOwner(person))
				.map(contacts::save)
				.concludeIfValid(it -> {
					var uri = fromMethodCall(on(this.getClass()).getContact(person, it.getId())).build().toUri();

					return ResponseEntity.created(uri)
							.body(mapper.map(it, ContactPersonDto.class));
				});
	}

	@GetMapping("/{identifier}")
	public HttpEntity<?> getContact(@LoggedIn TrackedPerson person, @PathVariable ContactPersonIdentifier identifier) {

		var dto = contacts.findById(identifier)
				.filter(it -> it.belongsTo(person))
				.map(it -> mapper.map(it, ContactPersonDto.class));

		return ResponseEntity.of(dto);
	}

	@PutMapping("/{identifier}")
	HttpEntity<?> updateContact(@LoggedIn TrackedPerson person,
			@PathVariable ContactPersonIdentifier identifier,
			@Valid @RequestBody ContactPersonDto payload,
			Errors errors) {

		return MappedPayloads.of(payload, errors)
				.alwaysMap(ContactPersonDto::validate)
				.map(dto -> {

					return contacts.findById(identifier)
							.filter(it -> it.belongsTo(person))
							.map(it -> mapper.map(dto, it))
							.map(contacts::save)
							.map(it -> mapper.map(it, ContactPersonDto.class));

				}).concludeIfValid(ResponseEntity::of);
	}
}
