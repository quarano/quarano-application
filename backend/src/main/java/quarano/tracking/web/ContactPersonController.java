/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.tracking.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.web.LoggedIn;
import quarano.core.web.ErrorsDto;
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.ContactPersonRepository;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;

import java.util.UUID;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
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
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
class ContactPersonController {

	private final @NonNull ModelMapper mapper;
	private final @NonNull TrackedPersonRepository people;
	private final @NonNull ContactPersonRepository contacts;
	private final @NonNull MessageSourceAccessor messages;

	@GetMapping
	Stream<ContactPersonDto> allContacts(@LoggedIn TrackedPerson person) {

		return contacts.findByOwnerId(person.getId()) //
				.map(it -> mapper.map(it, ContactPersonDto.class)) //
				.stream();
	}

	@PostMapping
	HttpEntity<?> createContactPerson(@Valid @RequestBody ContactPersonDto dto, Errors errors,
			@LoggedIn TrackedPerson person) {

		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ErrorsDto.of(errors, messages));
		}

		var contact = contacts.save(mapper.map(dto, ContactPerson.class).assignOwner(person));
		var uri = fromMethodCall(on(this.getClass()).getContact(person, contact.getId().toString())).build().toUri();

		return ResponseEntity.created(uri).body(mapper.map(contact, ContactPersonDto.class));
	}

	@GetMapping("/{id}")
	HttpEntity<?> getContact(@LoggedIn TrackedPerson person, @PathVariable String id) {

		var identifier = ContactPersonIdentifier.of(UUID.fromString(id));
		var dto = contacts.findById(identifier) //
				.filter(it -> it.belongsTo(person)) //
				.map(it -> mapper.map(it, ContactPersonDto.class));

		return ResponseEntity.of(dto);
	}

	@PutMapping("/{id}")
	HttpEntity<?> updateContact(@LoggedIn TrackedPerson person, //
			@PathVariable String id, //
			@Valid @RequestBody ContactPersonDto payload, //
			Errors errors) {

		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ErrorsDto.of(errors, messages));
		}

		var identifier = ContactPersonIdentifier.of(UUID.fromString(id));

		return ResponseEntity.of(contacts.findById(identifier) //
				.filter(it -> it.belongsTo(person)) //
				.map(it -> {
					mapper.map(payload, it);
					return it;
				}) //
				.map(contacts::save) //
				.map(it -> mapper.map(it, ContactPersonDto.class)));
	}
}
