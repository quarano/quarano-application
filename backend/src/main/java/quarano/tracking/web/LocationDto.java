package quarano.tracking.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import quarano.core.validation.Strings;
import quarano.core.validation.Textual;
import quarano.tracking.ContactLocation;
import quarano.tracking.Location;
import quarano.tracking.ZipCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
class LocationDto {

	@NotNull @Pattern(regexp = Strings.STREET) String street;
	@NotNull @Pattern(regexp = Strings.HOUSE_NUMBER) String houseNumber;
	@NotNull @Pattern(regexp = Strings.CITY) String city;
	@NotNull @Pattern(regexp = ZipCode.PATTERN) String zipCode;
	// String latitude; TODO for CORE-446
	// String longitude;

	static LocationDto fromLocation(Location location) {
		return new LocationDto(
				location.getAddress().getStreet(),
				location.getAddress().getHouseNumber().toString(),
				location.getAddress().getCity(),
				location.getAddress().getZipCode().toString());
	}

	@Value
	@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
	static class ContactLocationDto {
		@NotNull UUID location;

		@Textual String name;
		@Textual String contactPerson;
		@NotNull @PastOrPresent LocalDateTime startTime;
		@NotNull @PastOrPresent LocalDateTime endTime;
		@Textual String notes;

		static ContactLocationDto fromContactLocation(ContactLocation contactLocation) {
			return new ContactLocationDto(
					contactLocation.getLocation().getId().getLocationId(),
					contactLocation.getName(),
					contactLocation.getContactPerson(),
					contactLocation.getStartTime(),
					contactLocation.getEndTime(),
					contactLocation.getNotes());
		}
	}
}
