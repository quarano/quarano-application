package quarano.tracking;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.Address;
import quarano.core.DataInitializer;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.ZipCode;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author David Bauknecht
 */
@Order(500)
@Component
@RequiredArgsConstructor
@Slf4j
public class LocationDataInitializer implements DataInitializer {

    private final @NonNull LocationRepository locations;
    private final @NonNull TrackedPersonRepository trackedPeople;

    public static Location createFussballplatzLocation() {
        Address address = new Address("Am Sportplatz", Address.HouseNumber.of("1"), "Musterstadt", ZipCode.of("12345"));
        return new Location("Michael Mustermann", EmailAddress.of("praesident@sportvereinnulleins.de"), PhoneNumber.of("0918272711"), "Auf dem Fussballplatz").setAddress(address);
    }

    @Override
    public void initialize() {
        var person1 = trackedPeople.findRequiredById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1);
        locations.save(createFussballplatzLocation().setOwnerId(person1.getId()));
    }
}
