package quarano.tracking.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.web.LoggedIn;
import quarano.core.web.MappedPayloads;
import quarano.core.web.MapperWrapper;
import quarano.tracking.*;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.Encounter.EncounterIdentifier;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

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
	private final @NonNull LocationRepository locationRepository;

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

	@PostMapping("/api/contact-locations")
	HttpEntity<?> addAndAttachContactLocation(@Valid @RequestBody LocationDto.ContactLocationDto contactLocation,
			Errors errors, @LoggedIn TrackedPerson person) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors);
		}

		var maybeLocation = locationRepository
				.findById(Location.LocationId.of(contactLocation.getLocation()));
		if (maybeLocation.isEmpty()) {
			errors.rejectValue("location", "location not found");
			return ResponseEntity.badRequest().body(errors);
		} else {
			var location = maybeLocation.get();
			var newContactLocation = new ContactLocation(
					contactLocation.getName(),
					location,
					contactLocation.getContactPerson(),
					contactLocation.getStartTime(),
					contactLocation.getEndTime(),
					contactLocation.getNotes());
			person.getContactLocations().add(newContactLocation);
			var uri = fromMethodCall(on(this.getClass()).getContactLocation(newContactLocation.getId(), person)).build()
					.toUri();
			return ResponseEntity.created(uri).body(LocationDto.ContactLocationDto.fromContactLocation(newContactLocation));
		}
	}

	@GetMapping("/api/contact-locations/{id}")
	HttpEntity<LocationDto.ContactLocationDto> getContactLocation(@PathVariable ContactLocation.ContactLocationId id,
			@LoggedIn TrackedPerson person) {
		return ResponseEntity.of(person.getContactLocations()
				.stream()
				.filter(contactLocation -> contactLocation.getId().equals(id))
				.findFirst()
				.map(LocationDto.ContactLocationDto::fromContactLocation));
	}

	@GetMapping("/api/contact-locations")
	HttpEntity<List<LocationDto.ContactLocationDto>> getAllContactLocations(@LoggedIn TrackedPerson person) {
		return ResponseEntity.ok(person.getContactLocations()
				.stream()
				.map(LocationDto.ContactLocationDto::fromContactLocation)
				.collect(Collectors.toUnmodifiableList()));
	}

	@DeleteMapping("/api/contact-locations/{id}")
	HttpEntity<?> detachAndDeleteContactLocation(@PathVariable ContactLocation.ContactLocationId id,
			@LoggedIn TrackedPerson person) {
		var maybeLocation = person.getContactLocations()
				.stream()
				.filter(contactLocation -> contactLocation.getId().equals(id))
				.findFirst();
		if (maybeLocation.isEmpty()) {
			return ResponseEntity.badRequest()
					.body("Id not found");
		} else {
			person.getContactLocations().remove(maybeLocation.get());
			people.save(person);
			return ResponseEntity.ok().build();
		}
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
