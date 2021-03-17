package quarano.tracking.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Department;
import quarano.core.web.LoggedIn;
import quarano.core.web.MappedPayloads;
import quarano.core.web.MapperWrapper;
import quarano.department.TrackedCase;
import quarano.department.web.ExternalTrackedCaseRepresentations;
import quarano.tracking.ContactPerson;
import quarano.tracking.Location;
import quarano.tracking.LocationRepository;
import quarano.tracking.TrackedPerson;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController("newLocationController")
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final @NonNull MapperWrapper mapper;
    private final @NonNull LocationRepository locations;

    @PostMapping
    public HttpEntity<?> createLocation(@Valid @RequestBody LocationDto dto, Errors errors,
                                      @LoggedIn TrackedPerson person) {

        return MappedPayloads.of(dto, errors)
                .alwaysMap(LocationDto::validate)
                .map(it -> mapper.map(it, Location.class))
                .map(it -> it.assignOwner(person))
                .map(locations::save)
                .concludeIfValid(it -> {
                    var uri = fromMethodCall(on(this.getClass()).getLocation(person, it.getId())).build().toUri();

                    return ResponseEntity.created(uri)
                            .body(mapper.map(it, LocationDto.class));
                });
    }

    @GetMapping("/{identifier}")
    public HttpEntity<?> getLocation(@LoggedIn TrackedPerson person, @PathVariable Location.LocationIdentifier identifier) {

        var dto = locations.findById(identifier)
                .filter(it -> it.belongsTo(person))
                .map(it -> mapper.map(it, LocationDto.class));

        return ResponseEntity.of(dto);
    }

    @GetMapping()
    public HttpEntity<?> getLocationsForUser(@LoggedIn TrackedPerson person) {

        var dto = locations.findByOwnerId(person.getId())
                .map(it -> mapper.map(it, LocationDto.class))
                .toList();

        return ResponseEntity.ok(dto);
    }

}
