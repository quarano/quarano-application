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

import java.util.stream.Stream;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Oliver Drotbohm
 */
@RestController
@RequiredArgsConstructor
class DiaryController {

	private final @NonNull ModelMapper mapper;
	private final TrackedPersonRepository people;
	private final MessageSourceAccessor messages;

	@GetMapping("/api/diary")
	Stream<?> getDiary(@LoggedIn TrackedPerson person) {

		return person.getDiary() //
				.map(it -> mapper.map(it, DiaryEntryDto.class)) //
				.stream();
	}

	@PostMapping("/api/diary")
	HttpEntity<?> addDiaryEntry(@Valid @RequestBody DiaryEntryDto payload, Errors errors,
			@LoggedIn TrackedPerson person) {

		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ErrorsDto.of(errors, messages));
		}

		DiaryEntry entry = mapper.map(payload, DiaryEntry.class);

		people.save(person.addDiaryEntry(entry));

		var handlerMethod = fromMethodCall(on(DiaryController.class).getDiaryEntry(entry.getId(), person));

		return ResponseEntity.created(handlerMethod.build().toUri()) //
				.body(mapper.map(entry, DiaryEntryDto.class));
	}

	@GetMapping("/api/diary/{identifier}")
	HttpEntity<?> getDiaryEntry(@PathVariable DiaryEntryIdentifier identifier, @LoggedIn TrackedPerson person) {

		var dto = person.getDiary() //
				.getEntryFor(identifier) //
				.map(it -> mapper.map(it, DiaryEntryDto.class));

		return ResponseEntity.of(dto);
	}
}
