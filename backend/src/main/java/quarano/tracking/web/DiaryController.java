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
import quarano.tracking.DiaryEntry;
import quarano.tracking.DiaryEntry.DiaryEntryIdentifier;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;
import quarano.tracking.web.DiaryRepresentations.DiaryEntryInput;
import quarano.tracking.web.DiaryRepresentations.DiarySummary;

import javax.validation.Valid;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Oliver Drotbohm
 */
@RestController
@RequiredArgsConstructor
public class DiaryController {

	private final @NonNull TrackedPersonRepository people;
	private final @NonNull MessageSourceAccessor messages;
	private final @NonNull DiaryRepresentations representations;

	@GetMapping("/api/diary")
	DiarySummary getDiary(@LoggedIn TrackedPerson person) {
		return getSlottedDiary(person);
	}

	@GetMapping("/api/slotted")
	DiarySummary getSlottedDiary(@LoggedIn TrackedPerson person) {
		return representations.toSummary(person.getDiary(), person.getAccountRegistrationDate());
	}

	@PostMapping("/api/diary")
	HttpEntity<?> addDiaryEntry(@Valid @RequestBody DiaryEntryInput payload, Errors errors,
			@LoggedIn TrackedPerson person) {

		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ErrorsDto.of(errors, messages));
		}

		return representations.from(payload, errors) //
				.fold(entry -> handle(entry, person), //
						error -> ErrorsDto.of(errors, messages).toBadRequest());
	}

	@GetMapping("/api/diary/{identifier}")
	public ResponseEntity<?> getDiaryEntry(@PathVariable DiaryEntryIdentifier identifier,
			@LoggedIn TrackedPerson person) {

		var dto = person.getDiary() //
				.getEntryFor(identifier) //
				.map(it -> representations.toRepresentation(it));

		return ResponseEntity.of(dto);
	}

	@PutMapping("/api/diary/{identifier}")
	HttpEntity<?> addDiaryEntry(@PathVariable DiaryEntryIdentifier identifier, //
			@Valid @RequestBody DiaryEntryInput payload, Errors errors, //
			@LoggedIn TrackedPerson person) {

		var entry = person.getDiary().getEntryFor(identifier).orElse(null);

		if (entry == null) {
			return ResponseEntity.notFound().build();
		}

		if (errors.hasErrors()) {
			return ErrorsDto.of(errors, messages).toBadRequest();
		}

		return representations.from(payload, entry, errors) //
				.peekLeft(__ -> people.save(person)) //
				.mapLeft(representations::toRepresentation) //
				.<HttpEntity<?>> fold(ResponseEntity.ok()::body, //
						__ -> ErrorsDto.of(errors, messages).toBadRequest());
	}

	private HttpEntity<?> handle(DiaryEntry entry, TrackedPerson person) {

		people.save(person.addDiaryEntry(entry));

		var handlerMethod = fromMethodCall(on(DiaryController.class).getDiaryEntry(entry.getId(), person));

		return ResponseEntity.created(handlerMethod.build().toUri()) //
				.body(representations.toRepresentation(entry));
	}
}
