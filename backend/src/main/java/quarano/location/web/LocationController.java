package quarano.location.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.core.web.LoggedIn;
import quarano.core.web.MappedPayloads;
import quarano.core.web.MapperWrapper;
import quarano.location.Location.LocationIdentifier;
import quarano.location.LocationRepository;
import quarano.tracking.TrackedPerson;

@Transactional
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

	private final @NonNull MapperWrapper mapper;
	private final @NonNull LocationRepository locations;
	private final @NonNull LocationRepresentations representations;

	@PostMapping
	HttpEntity<?> createLocation(@Valid @RequestBody LocationDto dto, Errors errors, @LoggedIn TrackedPerson person) {

		var location = representations.from(dto, errors);

		var result = MappedPayloads.of(location, errors)
				.map(locations::save)
				.concludeIfValid(it -> {
					var resource = on(LocationController.class).getLocation(it.getId(), person);
					return ResponseEntity
							.created(URI.create(fromMethodCall(resource).toUriString()))
							.body(representations.toRepresentation(it));
				});
		return result;
	}

	@GetMapping("/{identifier}")
	public HttpEntity<?> getLocation(@PathVariable LocationIdentifier identifier, @LoggedIn TrackedPerson person) {

		var dto = locations.findById(identifier)
				.map(it -> mapper.map(it, LocationDto.class));

		return ResponseEntity.of(dto);
	}

}
