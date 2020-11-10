package quarano.diary.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.core.web.LoggedIn;
import quarano.core.web.MappedPayloads;
import quarano.diary.DiaryEntry;
import quarano.diary.DiaryEntry.DiaryEntryIdentifier;
import quarano.diary.DiaryManagement;
import quarano.diary.web.DiaryRepresentations.DiaryEntryInput;
import quarano.diary.web.DiaryRepresentations.DiarySummary;
import quarano.tracking.ContactPersonRepository;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;

import javax.validation.Valid;

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

	private final @NonNull DiaryRepresentations representations;
	private final @NonNull DiaryManagement diaries;

	private final @NonNull TrackedPersonRepository people;
	private final @NonNull ContactPersonRepository contacts;

	@GetMapping("/diary")
	DiarySummary getDiary(@LoggedIn TrackedPerson person) {

		var diary = diaries.findDiaryFor(person);

		return representations.toSummary(diary, person.getAccountRegistrationDate());
	}

	@PostMapping("/diary")
	HttpEntity<?> addDiaryEntry(@Valid @RequestBody DiaryEntryInput payload, Errors errors,
			@LoggedIn TrackedPerson person) {

		return MappedPayloads.of(errors).onValidGet(() -> {

			return representations.from(payload, person, errors)
					.mapLeft(it -> diaries.updateDiaryEntry(it))
					.fold(entry -> handle(entry, person), MappedPayloads::toBadRequest);
		});
	}

	@GetMapping("/diary/{identifier}")
	public ResponseEntity<?> getDiaryEntry(@PathVariable DiaryEntryIdentifier identifier,
			@LoggedIn TrackedPerson person) {

		var dto = diaries.findDiaryFor(person)
				.getEntryFor(identifier)
				.map(it -> representations.toRepresentation(it));

		return ResponseEntity.of(dto);
	}

	@PutMapping("/diary/{identifier}")
	HttpEntity<?> updateDiaryEntry(@PathVariable DiaryEntryIdentifier identifier,
			@Valid @RequestBody DiaryEntryInput payload, Errors errors,
			@LoggedIn TrackedPerson person) {

		return MappedPayloads.of(person, errors)
				.alwaysMap(diaries::findDiaryFor)
				.alwaysFlatMap(it -> it.getEntryFor(identifier))
				.concludeIfValid(it -> {

					return representations.from(payload, it, errors)
							.mapLeft(diaries::updateDiaryEntry)
							.mapLeft(representations::toRepresentation)
							.fold(ResponseEntity.ok()::body, MappedPayloads::toBadRequest);
				});
	}

	private HttpEntity<?> handle(DiaryEntry entry, TrackedPerson person) {

		var handlerMethod = fromMethodCall(on(DiaryController.class).getDiaryEntry(entry.getId(), person));

		return ResponseEntity.created(handlerMethod.build().toUri())
				.body(representations.toRepresentation(entry));
	}
}
