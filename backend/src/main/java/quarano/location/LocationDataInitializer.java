package quarano.location;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.DataInitializer;

@Order(800)
@Component
@RequiredArgsConstructor
@Slf4j
public class LocationDataInitializer implements DataInitializer {
    private final LocationRepository locations;
    @Override
    public void initialize() {

        saveLocation("BARMER", "Steubenstr.", "72-74", "68199", "Mannheim", "Tel. 0800 3 33 10 10");
        saveLocation("AOK - Die Gesundheitskasse KundenCenter Mannheim", "Renzstr.", "11-13", "68161", "Mannheim", "Tel. 0621 97 60 99 72 www.aok.de/bw");
        saveLocation("AOK - Die Gesundheitskasse KundenCenter Mannheim-Neckarau", "Friedrichstr.", "27", "68199", "Mannheim", "Tel. 0621 97 60 99 72 www.aok.de/bw");
        saveLocation("AOK - Die Gesundheitskasse KundenCenter Mannheim-Neckarstadt", "Pettenkoferstr.", "30", "68169", "Mannheim", "Tel. 0621 97 60 99 72 www.aok.de/bw");
        saveLocation("AOK - Die Gesundheitskasse KundenCenter Mannheim-Waldhof", "Waldpforte", "31-37", "68305", "Mannheim", "Tel. 0621 97 60 99 72 www.aok.de/bw");
        saveLocation("BKK Rhein-Neckar", "", "", "68163", "Mannheim", "Tel. 01802 66 77 66");
        saveLocation("Daimler Betriebskrankenkasse (BKK)", "Hanns-Martin-Schleyer-Str.", "21", "68305", "Mannheim", "Tel. 0621 3 93 72 60");
        saveLocation("DAK-Gesundheit", "P 6", "20-21", "68161", "Mannheim", "Tel. 0621 76 44 53-0");
        saveLocation("DAK Mannheim", "Saarburger Ring", "10-12", "68229", "Mannheim", "Tel. 0621 37 09 82-0");
        saveLocation("HEK Hanseatische Krankenkasse", "R 3", "4-5", "68161", "Mannheim", "");
        saveLocation("SBK Siemens-Betriebskrankenkasse", "Landteilstr.", "33", "68163", "Mannheim", "");
        saveLocation("SBK Siemens Betriebskrankenkasse", "Dynamostr.", "4", "68165", "Mannheim", "Tel. 0621 87 20 68-498");
    }

    private void saveLocation(String organization, String street, String house, String zipCode, String city, String contact) {
        Location location = Location.of(zipCode, city, street, house);
        location.setContactInfo(contact);
        location.setOrganization(organization);
        locations.save(location);
    }
}
