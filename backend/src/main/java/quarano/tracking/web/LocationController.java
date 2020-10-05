package quarano.tracking.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import quarano.core.web.LoggedIn;
import quarano.tracking.*;

import javax.validation.Valid;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@Transactional
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

	private final @NonNull LocationRepository locationRepository;

	@PostMapping
	public HttpEntity<?> createNewLocation(@Valid @RequestBody LocationDto dto, Errors errors,
			@LoggedIn TrackedPerson person) {

		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors);
		}

		var address = new Address(
				dto.getStreet(),
				Address.HouseNumber.of(dto.getHouseNumber()),
				dto.getCity(),
				ZipCode.of(dto.getZipCode()));

		var newLocation = locationRepository.save(new Location(address));
		var uri = fromMethodCall(on(this.getClass()).getLocation(newLocation.getId(), person)).build().toUri();

		return ResponseEntity.created(uri).body(LocationDto.fromLocation(newLocation));
	}

	@GetMapping("/{id}")
	public HttpEntity<LocationDto> getLocation(@PathVariable Location.LocationId id, @LoggedIn TrackedPerson person) {
		return ResponseEntity.of(locationRepository.findById(id).map(LocationDto::fromLocation));
	}
}
