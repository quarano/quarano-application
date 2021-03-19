package quarano.tracking;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.Address;
import quarano.core.DataInitializer;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.ZipCode;
import quarano.core.Address.HouseNumber;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 * @author Patrick Otto
 */
@Order(400)
@Component
@RequiredArgsConstructor
@Slf4j
public class TrackedPersonDataInitializer implements DataInitializer {

	private final @NonNull TrackedPersonRepository trackedPeople;

	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON1_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("738d3d1f-a9f1-4619-9896-2b5cb3a89c22"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON2_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("0c434624-7dbe-11ea-bc55-0242ac130003"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON3_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("705dc63d-fddf-47fe-8679-2c8c59ea23ae"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON4_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("a8bd1d2d-b824-4989-ad9f-73be224654d6"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON5_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("29206992-84f0-4a0e-9267-ed0a2b5b7507"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON6_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("e5e71192-a007-43e6-851a-c53bb0c90d3b"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON7_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("c53bb0c9-a007-43e6-851a-e5e711920d3c"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON8_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("2105d200-e331-1dea-87d0-0242ac13ad71"));		
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON3_ID_DEP2 = TrackedPersonIdentifier
			.of(UUID.fromString("1d5ce370-7dbe-11ea-bc55-0242ac130003"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON4_ID_DEP2 = TrackedPersonIdentifier
			.of(UUID.fromString("ee35d200-e221-11ea-87d0-0242ac130003"));

	
	// Persons for security tests
	public final static TrackedPersonIdentifier VALID_TRACKED_SEC1_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("4bcb1da9-1e03-4ca0-9da1-f86d80aaa1ab"));

	public final static TrackedPersonIdentifier VALID_TRACKED_SEC2_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("e69c29f4-93c8-42dc-8066-c721b7227b39"));

	public final static TrackedPersonIdentifier VALID_TRACKED_SEC3_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("0bc8cd7a-60ed-4f4c-8360-22c9c711174d"));

	public final static TrackedPersonIdentifier VALID_TRACKED_SEC4_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("7e570cff-7528-45e8-ae1a-38d89b9911f8"));

	public final static TrackedPersonIdentifier VALID_TRACKED_SEC5_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("5380173a-f1b5-44da-a7c1-91c7aa19d71d"));

	public final static TrackedPersonIdentifier VALID_TRACKED_SEC6_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("ab146a01-09d1-4ce7-8c28-aae293833754"));

	public final static TrackedPersonIdentifier VALID_TRACKED_SEC7_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("3caa29c6-f3b5-4201-8f23-5ca2d5d1714a"));

	public final static TrackedPersonIdentifier VALID_TRACKED_SEC8_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("ca5f631e-c7ba-495f-9a54-77e90a71100f"));

	/**
	 * A persona with all contact details submitted, ready to take the next steps in enrollment.
	 *
	 * @return
	 */
	public static TrackedPerson createTanja() {

		return new TrackedPerson(VALID_TRACKED_PERSON1_ID_DEP1, "Tanja", "Mueller",
				EmailAddress.of("tanja.mueller@testtest.de"), PhoneNumber.of("0621111155"), LocalDate.of(1975, 8, 3))
						.setAddress(new Address("Hauptstr. 4", HouseNumber.of("3"), "Mannheim", ZipCode.of("68259")))
						.setLocale(Locale.GERMANY);
	}

	/**
	 * A persona without contact details completed.
	 *
	 * @return
	 */
	public static TrackedPerson createMarkus() {
		return new TrackedPerson(VALID_TRACKED_PERSON2_ID_DEP1, "Markus", "Hanser",
				EmailAddress.of("markus.hanser@testtest.de"), PhoneNumber.of("0621222255"), LocalDate.of(1990, 1, 1))
						.setLocale(Locale.UK);
	}

	/**
	 * A persona without contact details completed, locale GERMANY.
	 *
	 * @return
	 */
	public static TrackedPerson createPeter() {
			return new TrackedPerson(VALID_TRACKED_PERSON3_ID_DEP1, "Peter", "Aalen",
					EmailAddress.of("peter.aalen@abc.de"), PhoneNumber.of("0621222255"), LocalDate.of(1990, 1, 1))
					.setLocale(Locale.GERMANY);
	}

	/**
	 * A persona fully enrolled.
	 *
	 * @return
	 */
	public static TrackedPerson createSandra() {

		return new TrackedPerson(VALID_TRACKED_PERSON3_ID_DEP2, "Sandra", "Schubert",
				EmailAddress.of("sandra.schubert@testtest.de"), PhoneNumber.of("0621222255"), LocalDate.of(1990, 1, 1))
						.setAddress(new Address("Wingertstraße", HouseNumber.of("71"), "Mannheim", ZipCode.of("68199")));
	}

	public static TrackedPerson createJessica() {

		return new TrackedPerson(VALID_TRACKED_PERSON4_ID_DEP2, "Jessica", "Wagner",
				EmailAddress.of("jessica.wagner@testtest.de"), PhoneNumber.of("0621222256"), LocalDate.of(1989, 1, 1))
						.setAddress(new Address("Wingertstraße", HouseNumber.of("70"), "Mannheim", ZipCode.of("68199")));
	}

	/**
	 * A persona without contact details completed.
	 *
	 * @return
	 */
	public static TrackedPerson createGustav() {
		return new TrackedPerson(VALID_TRACKED_PERSON4_ID_DEP1, "Gustav", "Meier",
				EmailAddress.of("gustav.meier@testtest.de"), PhoneNumber.of("06214455684"), LocalDate.of(1980, 1, 1))
						.setAddress(new Address("Am Aubuckel", HouseNumber.of("12"), "Mannheim", ZipCode.of("68259")));
	}

	/**
	 * A persona without contact details completed.
	 *
	 * @return
	 */
	public static TrackedPerson createNadine() {
		return new TrackedPerson(VALID_TRACKED_PERSON5_ID_DEP1, "Nadine", "Ebert",
				EmailAddress.of("nadine.ebert@testtest.de"), PhoneNumber.of("0621 88 334"), LocalDate.of(1980, 1, 1))
						.setAddress(new Address("Seckenheimer Landstr.", HouseNumber.of("50"), "Mannheim", ZipCode.of("68199")));
	}

	/**
	 * A persona without account created and missing basic data.
	 *
	 * @return
	 */
	public static TrackedPerson createHarry() {
		return new TrackedPerson(VALID_TRACKED_PERSON6_ID_DEP1, "Harry", "Hirsch", EmailAddress.of("harry@hirsch.de"),
				PhoneNumber.of("0621 115545"), null).setLocale(Locale.US);
	}

	/**
	 * A persona without account created but all basic data available.
	 *
	 * @return
	 */
	public static TrackedPerson createHarriette() {
		return new TrackedPerson(VALID_TRACKED_PERSON7_ID_DEP1, "Harriette", "Hirsch",
				EmailAddress.of("harriette@hirsch.de"), PhoneNumber.of("0621 115545"), LocalDate.of(1991, 3, 15));
	}

	/**
	 * A registered and in the middle of tracking
	 *
	 * @return
	 */
	public static TrackedPerson createSiggi() {

		return new TrackedPerson(VALID_TRACKED_SEC1_ID_DEP1, "Siggi", "Seufert", EmailAddress.of("siggi@testtest.de"),
				PhoneNumber.of("0621883322"), LocalDate.of(1980, 1, 1))
						.setAddress(new Address("Baumstr.", HouseNumber.of("6"), "Mannheim", ZipCode.of("68309")));
	}

	/**
	 * A persona ready to start the enrolement
	 *
	 * @return
	 */
	public static TrackedPerson createSarah() {
		return new TrackedPerson(VALID_TRACKED_SEC2_ID_DEP1, "Sarah", "Siebel", EmailAddress.of("sarah@testtest.de"),
				PhoneNumber.of("0621883322"), LocalDate.of(1980, 1, 1));
	}

	/**
	 * A persona without account created.
	 *
	 * @return
	 */
	public static TrackedPerson createSonja() {
		return new TrackedPerson(VALID_TRACKED_SEC3_ID_DEP1, "Sonja", "Straßmann", EmailAddress.of("sonja@testtest.de"),
				PhoneNumber.of("0621556464"), LocalDate.of(1980, 1, 1));
	}

	/**
	 * A persona without account created.
	 *
	 * @return
	 */
	public static TrackedPerson createSamuel() {
		return new TrackedPerson(VALID_TRACKED_SEC4_ID_DEP1, "Samuel", "Soller", EmailAddress.of("samuel@testtest.de"),
				PhoneNumber.of("0621889977"), LocalDate.of(1980, 1, 1));
	}

	/**
	 * A persona without account created.
	 *
	 * @return
	 */
	public static TrackedPerson createSylvia() {
		return new TrackedPerson(VALID_TRACKED_SEC5_ID_DEP1, "Sylvia", "Sander", EmailAddress.of("sylvia@testtest.de"),
				PhoneNumber.of("0621447785"), LocalDate.of(1980, 1, 1));
	}

	/**
	 * A persona without account created.
	 *
	 * @return
	 */
	public static TrackedPerson createSteven() {
		return new TrackedPerson(VALID_TRACKED_SEC6_ID_DEP1, "Steven", "Sperber", EmailAddress.of("steven@testtest.de"),
				PhoneNumber.of("0621779988"), LocalDate.of(1980, 1, 1));
	}

	/**
	 * A persona without account created.
	 *
	 * @return
	 */
	public static TrackedPerson createSteffen() {
		return new TrackedPerson(VALID_TRACKED_SEC7_ID_DEP1, "Steffen", "Schön", EmailAddress.of("steffen@testtest.de"),
				PhoneNumber.of("0621443311"), LocalDate.of(1980, 1, 1));
	}

	/**
	 * A persona without account created.
	 *
	 * @return
	 */
	public static TrackedPerson createSunny() {
		return new TrackedPerson(VALID_TRACKED_SEC8_ID_DEP1, "Sunny", "Schuster", EmailAddress.of("sunny@testtest.de"),
				PhoneNumber.of("0621884466"), LocalDate.of(1980, 1, 1));
	}

	/**
	 * A persona ready for tracking
	 *
	 * @return
	 */
	public static TrackedPerson createJulian() {
		return new TrackedPerson(VALID_TRACKED_PERSON8_ID_DEP1, "Julian", "Jäger", EmailAddress.of("julian@testtest.de"),
			 PhoneNumber.of("0621111155"), LocalDate.of(1975, 8, 3))
						.setAddress(new Address("Nebenstr.", HouseNumber.of("12"), "Mannheim", ZipCode.of("68199")))
						.setLocale(Locale.GERMANY);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		// create a fixed set of tracked people for testing and integration if it is not present yet
		if (this.trackedPeople.findById(VALID_TRACKED_PERSON1_ID_DEP1).isPresent()) {
			log.debug("Initial client data already exists, skipping test data generation");
			return;
		}

		trackedPeople.save(createTanja());
		trackedPeople.save(createMarkus());
		trackedPeople.save(createPeter());
		trackedPeople.save(createGustav());
		trackedPeople.save(createNadine());
		trackedPeople.save(createSandra());
		trackedPeople.save(createJessica());
		trackedPeople.save(createHarry());
		trackedPeople.save(createHarriette());
		trackedPeople.save(createSiggi());
		trackedPeople.save(createSarah());
		trackedPeople.save(createSonja());
		trackedPeople.save(createSamuel());
		trackedPeople.save(createSylvia());
		trackedPeople.save(createSteven());
		trackedPeople.save(createSunny());
		trackedPeople.save(createSteffen());
		trackedPeople.save(createJulian());

		log.debug("Test data: Generated {} tracked persons.", trackedPeople.count());
	}
}
