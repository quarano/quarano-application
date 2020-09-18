package quarano.location.web;

import java.util.Optional;

import javax.validation.constraints.Pattern;

import org.springframework.lang.Nullable;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import quarano.core.validation.Strings;
import quarano.location.Location;
import quarano.tracking.Address;
import quarano.tracking.ZipCode;

@Data
@Getter(onMethod = @__(@Nullable))
@NoArgsConstructor
public class LocationDto {
    private @Pattern(regexp = Strings.STREET) String street;
    private @Pattern(regexp = Strings.HOUSE_NUMBER) String houseNumber;
    private @Pattern(regexp = Strings.CITY) String city;
    private @Pattern(regexp = ZipCode.PATTERN) String zipCode;
    private String contactInfo;
    private String organization;
    private double longitude;
    private double latitude;
    private String plusCode;
    private String id;

    public LocationDto(Location location) {
        contactInfo = location.getContactInfo();
        organization = location.getOrganization();
        Address address = location.getAddress();
        street = address.getStreet();
        city = address.getCity();
        plusCode = location.getPlusCode();
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        Optional.ofNullable(address.getHouseNumber()).ifPresent(it -> houseNumber = it.toString());
        Optional.ofNullable(address.getZipCode()).ifPresent(it -> zipCode = it.toString());
        id = location.getId().toString();
    }

}
