/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.tracking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.DataInitializer;
import quarano.tracking.Address.HouseNumber;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.time.LocalDate;
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

	private final TrackedPersonRepository trackedPeople;

	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON1_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("738d3d1f-a9f1-4619-9896-2b5cb3a89c22"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON2_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("0c434624-7dbe-11ea-bc55-0242ac130003"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON4_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("a8bd1d2d-b824-4989-ad9f-73be224654d6"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON5_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("29206992-84f0-4a0e-9267-ed0a2b5b7507"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON6_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("e5e71192-a007-43e6-851a-c53bb0c90d3b"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON3_ID_DEP2 = TrackedPersonIdentifier
			.of(UUID.fromString("1d5ce370-7dbe-11ea-bc55-0242ac130003"));

	// Persons for security
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
						.setAddress(new Address("Hauptstr. 4", HouseNumber.of("3"), "Mannheim", ZipCode.of("68259")));
	}

	/**
	 * A persona without contact details completed.
	 *
	 * @return
	 */
	public static TrackedPerson createMarkus() {
		return new TrackedPerson(VALID_TRACKED_PERSON2_ID_DEP1, "Markus", "Hanser",
				EmailAddress.of("markus.hanser@testtest.de"), PhoneNumber.of("0621222255"), LocalDate.of(1990, 1, 1));
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
	 * A persona without account created and no invite link generated
	 *
	 * @return
	 */
	public static TrackedPerson createHarry() {
		return new TrackedPerson(VALID_TRACKED_PERSON6_ID_DEP1, "Harry", "Hirsch", EmailAddress.of("harry@hirsch.de"), PhoneNumber.of("0621 115545"),
				null);
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
		return new TrackedPerson(VALID_TRACKED_SEC2_ID_DEP1, "Sarah", "Siebel", EmailAddress.of("sarah@testtest.de"), PhoneNumber.of("0621883322"),
				LocalDate.of(1980, 1, 1));
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

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		// create a fixed set of tracked people for testing and integration if it is not present yet
		if (this.trackedPeople.findById(VALID_TRACKED_PERSON1_ID_DEP1).isPresent()) {
			log.info("Initial client data already exists, skipping test data generation");
			return;
		}

		trackedPeople.save(createTanja());
		trackedPeople.save(createMarkus());
		trackedPeople.save(createGustav());
		trackedPeople.save(createNadine());
		trackedPeople.save(createSandra());
		trackedPeople.save(createHarry());
		trackedPeople.save(createSiggi());
		trackedPeople.save(createSarah());
		trackedPeople.save(createSonja());
		trackedPeople.save(createSamuel());
		trackedPeople.save(createSylvia());
		trackedPeople.save(createSteven());
		trackedPeople.save(createSunny());
		trackedPeople.save(createSteffen());

		log.info("Test data: Generated 14 tracked persons.");
	}
}
