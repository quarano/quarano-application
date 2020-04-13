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

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import quarano.auth.web.LoggedIn;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.ContactPersonRepository;
import quarano.tracking.EmailAddress;
import quarano.tracking.PhoneNumber;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;
import quarano.tracking.ZipCode;

/**
 * @author Oliver Drotbohm
 */
@RestController
@RequiredArgsConstructor
public class TrackingController {

	private final @NonNull TrackedPersonRepository repository;
	private final @NonNull ContactPersonRepository contacts;
	private final @NonNull ModelMapper mapper;

	@GetMapping("/api/enrollment/details")
	public HttpEntity<?> enrollmentOverview(@LoggedIn TrackedPerson person) {
		return overview(person);
	}

	@PostMapping("/api/enrollment/encounter")
	HttpEntity<?> addEnrollmentContacts() {
		return null;
	}

	@GetMapping("/api/details")
	HttpEntity<?> overview(@LoggedIn TrackedPerson person) {
		return ResponseEntity.ok(mapper.map(person, TrackedPersonDto.class));
	}

	@PutMapping("/api/details")
	public HttpEntity<?> createTrackedPerson(@Validated @RequestBody TrackedPersonDto dto, Errors errors,
			@LoggedIn TrackedPerson user) {

		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().build();
		}

		mapper.map(dto, user);
		repository.save(user);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/api/details/form")
	HttpEntity<?> trackedPersonForm() {

		var properties = Map.of("zipCode", Map.of("regex", ZipCode.PATTERN), //
				"mobilePhone", Map.of("regex", PhoneNumber.PATTERN), //
				"phone", Map.of("regex", PhoneNumber.PATTERN), //
				"email", Map.of("regex", EmailAddress.PATTERN));

		return ResponseEntity.ok(Map.of("properties", properties));
	}

	@GetMapping("/api/encounters")
	Stream<?> getEncounters(@LoggedIn TrackedPerson person) {

		return person.getEncounters().stream() //
				.map(it -> EncounterDto.of(it, person));
	}

//	@PostMapping("/api/encounters")
//	HttpEntity<?> addEncounters(@Valid @RequestBody NewEncounter payload, @LoggedIn TrackedPerson person) {
//
//		return contacts.findById(payload.getContactId()) //
//				.filter(it -> it.belongsTo(person)) //
//				.map(it -> person.reportContactWith(it, payload.date)) //
//				.map(it -> {
//					repository.save(person);
//					return it;
//				}) //
//				.<HttpEntity<?>> map(it -> {
//
//					var encounterHandlerMethod = on(TrackingController.class).getEncounter(it.getId().toString(), person);
//					var encounterUri = fromMethodCall(encounterHandlerMethod).build().toUri();
//
//					return ResponseEntity.created(encounterUri).body(EncounterDto.of(it, person));
//
//				}).orElseGet(() -> ResponseEntity.badRequest().body("Invalid contact identifier!"));
//	}
//
//	@GetMapping("/api/encounters/{id}")
//	HttpEntity<?> getEncounter(@PathVariable String id, @LoggedIn TrackedPerson person) {
//
//		var identifier = EncounterIdentifier.of(UUID.fromString(id));
//
//		return ResponseEntity.of(person.getEncounters() //
//				.havingIdOf(identifier) //
//				.map(it -> EncounterDto.of(it, person)));
//	}
//
//	@DeleteMapping("/api/encounters/{id}")
//	HttpEntity<?> removeEncounter(@PathVariable String id, @LoggedIn TrackedPerson person) {
//
//		var identifier = EncounterIdentifier.of(UUID.fromString(id));
//
//		person.getEncounters().havingIdOf(null);
//
//		return null;
//	}

	@Value
	@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
	static class NewEncounter {

		String contactId;
		LocalDate date;

		ContactPersonIdentifier getContactId() {
			return ContactPersonIdentifier.of(UUID.fromString(contactId));
		}
	}
}
