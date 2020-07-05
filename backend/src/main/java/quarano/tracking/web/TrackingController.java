package quarano.tracking.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.web.LoggedIn;
import quarano.core.web.MappedPayloads;
import quarano.core.web.MapperWrapper;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.ContactPersonRepository;
import quarano.tracking.Encounter.EncounterIdentifier;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;
import quarano.tracking.ZipCode;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.RepresentationModel;
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

/**
 * @author Oliver Drotbohm
 */
@RestController
@RequiredArgsConstructor
public class TrackingController {

	private final @NonNull TrackedPersonRepository people;
	private final @NonNull ContactPersonRepository contacts;
	private final @NonNull MapperWrapper mapper;
	private final @NonNull MessageSourceAccessor messages;

	@GetMapping("/api/enrollment/details")
	public HttpEntity<?> enrollmentOverview(@LoggedIn TrackedPerson person) {
		return overview(person);
	}

	@GetMapping("/api/details")
	HttpEntity<?> overview(@LoggedIn TrackedPerson person) {
		return ResponseEntity.ok(mapper.map(person, TrackedPersonDto.class));
	}

	@PutMapping("/api/details")
	public HttpEntity<?> updateTrackedPersonDetails(@Validated @RequestBody TrackedPersonDto dto, Errors errors,
			@LoggedIn TrackedPerson user) {

		return MappedPayloads.of(dto, errors)
				.map(it -> mapper.map(it, user))
				.map(people::save)
				.onValidGet(() -> ResponseEntity.ok().build());
	}

	@GetMapping("/api/details/form")
	HttpEntity<?> trackedPersonForm() {

		var properties = Map.of("zipCode", Map.of("regex", ZipCode.PATTERN),
				"mobilePhone", Map.of("regex", PhoneNumber.PATTERN),
				"phone", Map.of("regex", PhoneNumber.PATTERN),
				"email", Map.of("regex", EmailAddress.PATTERN));

		return ResponseEntity.ok(Map.of("properties", properties));
	}

	@GetMapping("/api/encounters")
	public RepresentationModel<?> getEncounters(@LoggedIn TrackedPerson person) {

		var encounters = person.getEncounters().map(it -> EncounterDto.of(it, person))
				.toList();

		return RepresentationModel.of(encounters);
	}

	@PostMapping("/api/encounters")
	HttpEntity<?> addEncounters(@Valid @RequestBody NewEncounter payload, Errors errors, @LoggedIn TrackedPerson person) {

		if (errors.hasErrors() && errors.hasFieldErrors("contact")) {
			return ResponseEntity.badRequest().body(errors);
		}

		return contacts.findById(payload.getContactId())
				.filter(it -> it.belongsTo(person))
				.map(it -> person.reportContactWith(it, payload.date))
				.map(it -> {
					people.save(person);
					return it;
				})
				.<HttpEntity<?>> map(it -> {

					var encounterHandlerMethod = on(TrackingController.class).getEncounter(it.getId(), person);
					var encounterUri = fromMethodCall(encounterHandlerMethod).build().toUri();

					return ResponseEntity.created(encounterUri).body(EncounterDto.of(it, person));

				}).orElseGet(() -> {

					errors.rejectValue("contact", "Invalid.contact", new Object[] { payload.getContact().toString() }, "");

					return ResponseEntity.badRequest().body(errors);
				});
	}

	@GetMapping("/api/encounters/{identifier}")
	HttpEntity<?> getEncounter(@PathVariable EncounterIdentifier identifier, @LoggedIn TrackedPerson person) {

		return ResponseEntity.of(person.getEncounters()
				.havingIdOf(identifier)
				.map(it -> EncounterDto.of(it, person)));
	}

	@DeleteMapping("/api/encounters/{identifier}")
	HttpEntity<?> removeEncounter(@PathVariable EncounterIdentifier identifier, @LoggedIn TrackedPerson person) {

		person.removeEncounter(identifier);

		people.save(person);

		return ResponseEntity.ok().build();
	}

	@Value
	@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
	static class NewEncounter {

		@NotNull UUID contact;
		@NotNull @PastOrPresent LocalDate date;

		ContactPersonIdentifier getContactId() {
			return ContactPersonIdentifier.of(contact);
		}
	}
}
