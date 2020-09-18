package quarano.location.web;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.core.web.MapperWrapper;
import quarano.location.Location;
import quarano.tracking.Address;

@Component
@RequiredArgsConstructor
public class LocationRepresentations {

    private final @NonNull MapperWrapper mapper;

    public Location from(LocationDto location, Errors errors) {

        validateForCreation(location, errors);
        return  mapper.map(location, Location.class).setAddress(mapper.map(location, Address.class));
    }

    private void validateForCreation(LocationDto location, Errors errors) {
    }

    public LocationDto toRepresentation(Location location) {
        return new LocationDto(location);
    }
}
