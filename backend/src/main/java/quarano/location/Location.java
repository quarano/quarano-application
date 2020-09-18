package quarano.location;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jddd.core.types.Identifier;

import lombok.*;
import quarano.core.QuaranoAggregate;
import quarano.tracking.Address;
import quarano.tracking.ZipCode;

@Entity
@Table(name = "locations")
@Getter
@Setter(AccessLevel.PACKAGE)
public class Location extends QuaranoAggregate<Location, Location.LocationIdentifier> {

    private @Getter @Setter Address address = new Address();
    private @Getter @Setter String contactInfo;
    private @Getter @Setter String organization;
    private @Getter @Setter double longitude;
    private @Getter @Setter double latitude;
    private @Getter @Setter String plusCode;

	Location() {
		this.id = LocationIdentifier.of(UUID.randomUUID());
	}

	public static Location of(String zipCode, String city, String street, String number) {
		Address address = new Address();
		address.setZipCode(ZipCode.of(zipCode));
		address.setCity(city);
		address.setStreet(street);
		address.setHouseNumber(Address.HouseNumber.of(number));
		Location location = new Location();
		location.setAddress(address);
		return location;
	}



	/*
	* 	private String street;
	private HouseNumber houseNumber = HouseNumber.NONE;
	private String city;
	private ZipCode zipCode;
	*
	* */
	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
	public static class LocationIdentifier implements Identifier, Serializable {

        private static final long serialVersionUID = 102094650705209943L;
        private final UUID locationId;

		@Override
		public String toString() {
			return locationId.toString();
		}
	}
}
