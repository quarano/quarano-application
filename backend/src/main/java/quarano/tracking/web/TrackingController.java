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
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.ContactPersonRepository;
import quarano.tracking.Encounter;
import quarano.tracking.Encounter.EncounterIdentifier;
import quarano.tracking.Location;
import quarano.tracking.LocationRepository;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;
import quarano.core.ZipCode;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import javax.validation.Valid;
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
	private final @NonNull LocationRepository locations;
	private final @NonNull MapperWrapper mapper;
	private final @NonNull MessageSourceAccessor messages;

	@GetMapping("/enrollment/details")
	public HttpEntity<?> enrollmentOverview(@LoggedIn TrackedPerson person) {
		return overview(person);
	}

	@GetMapping("/details")
	HttpEntity<?> overview(@LoggedIn TrackedPerson person) {
		return ResponseEntity.ok(mapper.map(person, TrackedPersonDto.class));
	}

	@PutMapping("/details")
	public HttpEntity<?> updateTrackedPersonDetails(@Validated @RequestBody TrackedPersonDto dto, Errors errors,
			@LoggedIn TrackedPerson user) {

		return MappedPayloads.of(dto, errors)
				.map(it -> mapper.map(it, user))
				.map(people::save)
				.onValidGet(() -> ResponseEntity.ok().build());
	}

	@GetMapping("/details/form")
	HttpEntity<?> trackedPersonForm() {

		var properties = Map.of("zipCode", Map.of("regex", ZipCode.PATTERN),
				"mobilePhone", Map.of("regex", PhoneNumber.PATTERN),
				"phone", Map.of("regex", PhoneNumber.PATTERN),
				"email", Map.of("regex", EmailAddress.PATTERN));

		return ResponseEntity.ok(Map.of("properties", properties));
	}

	@GetMapping("/encounters")
	public RepresentationModel<?> getEncounters(@LoggedIn TrackedPerson person) {

		var encounters = person.getEncounters().map(it -> EncounterDto.of(it, person))
				.toList();

		return RepresentationModel.of(encounters);
	}

	@PostMapping("/encounters")
	HttpEntity<?> addEncounters(@Valid @RequestBody NewEncounter payload, Errors errors, @LoggedIn TrackedPerson person) {

		if (errors.hasErrors() && (errors.hasFieldErrors("contact") || errors.hasFieldErrors("location"))) {
			return ResponseEntity.badRequest().body(errors);
		}

		Optional<Location> location = payload.getLocationId()
				.flatMap(locations::findById)
				.filter(it -> it.belongsTo(person));

		if(payload.getLocationId().isPresent() && location.isEmpty()){
			errors.rejectValue("location", "Invalid.location", new Object[] { payload.getLocation().toString() }, "");

			return ResponseEntity.badRequest().body(errors);
		}

		Optional<ContactPerson> contactPerson = payload.getContactId()
				.flatMap(contacts::findById)
				.filter(it -> it.belongsTo(person));

		if(payload.getContactId().isPresent() && contactPerson.isEmpty()){
			errors.rejectValue("contact", "Invalid.contact", new Object[] { payload.getContact() }, "");

			return ResponseEntity.badRequest().body(errors);
		}

		if (location.isEmpty() && contactPerson.isEmpty()) {
			errors.rejectValue("location", "Invalid.location", new Object[]{payload.getLocation().toString()}, "");
			errors.rejectValue("contact", "Invalid.contact", new Object[]{payload.getContact().toString()}, "");

			return ResponseEntity.badRequest().body(errors);
		}

		return Optional.of(contactPerson.map(c -> location.map(l -> person.reportContactWithAtLocation(c, l, payload.date))
				.orElse(person.reportContactWith(c, payload.date)))
				.orElse(location.map(l -> person.reportAtLocation(l, payload.date)).orElse(null)))
				.map(it -> {
					people.save(person);
					return it;
				})
				.<HttpEntity<?>> map(it -> {

					var encounterHandlerMethod = on(TrackingController.class).getEncounter(it.getId(), person);
					var encounterUri = fromMethodCall(encounterHandlerMethod).build().toUri();

					return ResponseEntity.created(encounterUri).body(EncounterDto.of(it, person));

				}).orElseGet(() -> {

					errors.rejectValue("contact", "Invalid.contact", new Object[] { payload.getContact() }, "");

					return ResponseEntity.badRequest().body(errors);
				});
	}

	@GetMapping("/encounters/{identifier}")
	HttpEntity<?> getEncounter(@PathVariable EncounterIdentifier identifier, @LoggedIn TrackedPerson person) {

		return ResponseEntity.of(person.getEncounters()
				.havingIdOf(identifier)
				.map(it -> EncounterDto.of(it, person)));
	}

	@DeleteMapping("/encounters/{identifier}")
	HttpEntity<?> removeEncounter(@PathVariable EncounterIdentifier identifier, @LoggedIn TrackedPerson person) {

		person.removeEncounter(identifier);

		people.save(person);

		return ResponseEntity.ok().build();
	}

	@Value
	@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
	static class NewEncounter {

		UUID contact;
		UUID location;
		@PastOrPresent LocalDate date;

		Optional<ContactPersonIdentifier> getContactId() {
			return contact != null ? Optional.of(ContactPersonIdentifier.of(contact)) : Optional.empty();
		}

		Optional<Location.LocationIdentifier> getLocationId() {
			return location!=null ? Optional.of(Location.LocationIdentifier.of(location)) : Optional.empty();
		}
	}
}
