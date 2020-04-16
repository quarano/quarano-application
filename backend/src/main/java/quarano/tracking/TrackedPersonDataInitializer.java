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
import quarano.reference.Symptom;
import quarano.reference.SymptomRepository;
import quarano.tracking.Address.HouseNumber;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 * @author Patrick Otto
 */
@Order(600)
@Component
@RequiredArgsConstructor
@Slf4j
public class TrackedPersonDataInitializer implements DataInitializer {

	private final TrackedPersonRepository trackedPeople;
	private final ContactPersonRepository contacts;
	private final SymptomRepository symptoms;

	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON1_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("738d3d1f-a9f1-4619-9896-2b5cb3a89c22"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON2_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("0c434624-7dbe-11ea-bc55-0242ac130003"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON3_ID_DEP2 = TrackedPersonIdentifier
			.of(UUID.fromString("1d5ce370-7dbe-11ea-bc55-0242ac130003"));

	// people
	public static TrackedPerson INDEX_PERSON1_NOT_REGISTERED = new TrackedPerson(VALID_TRACKED_PERSON1_ID_DEP1, "Tanja",
			"Mueller", EmailAddress.of("tanja.mueller@testtest.de"), PhoneNumber.of("0621111155"), LocalDate.of(1975, 8, 3));

	public static TrackedPerson INDEX_PERSON2_IN_ENROLLMENT = new TrackedPerson(VALID_TRACKED_PERSON2_ID_DEP1, "Markus",
			"Hanser", EmailAddress.of("markus.hanser@testtest.de"), PhoneNumber.of("0621222255"), LocalDate.of(1990, 1, 1));

	public static TrackedPerson INDEX_PERSON3_WITH_ACTIVE_TRACKING = new TrackedPerson(VALID_TRACKED_PERSON3_ID_DEP2,
			"Sandra", "Schubert", EmailAddress.of("sandra.schubert@testtest.de"), PhoneNumber.of("0621222255"),
			LocalDate.of(1990, 1, 1));

	static {
		// add address data to person 3
		INDEX_PERSON3_WITH_ACTIVE_TRACKING
				.setAddress(new Address("Hauptstr. 4", HouseNumber.of("3"), "Mannheim", ZipCode.of("68259")));

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

		log.info("Test data: Generating 3 tracked Persons");

		// generate 2 contacts for Person 3
		List<ContactPerson> contactsOfPerson3 = new ArrayList<>();

		ContactPerson contact1OfPerson3 = new ContactPerson("Lora", "Laurer");
		contact1OfPerson3.assignOwner(INDEX_PERSON3_WITH_ACTIVE_TRACKING);
		contact1OfPerson3.setEmailAddress(EmailAddress.of("lora@testtest.de"));
		contactsOfPerson3.add(contact1OfPerson3);

		ContactPerson contact2OfPerson3 = new ContactPerson("Harry", "Huber");
		contact2OfPerson3.setEmailAddress(EmailAddress.of("harry@testtest.de"));
		contact2OfPerson3.assignOwner(INDEX_PERSON3_WITH_ACTIVE_TRACKING);
		contactsOfPerson3.add(contact2OfPerson3);

		contacts.saveAll(contactsOfPerson3);

		// generate diary entries for person 3

		DiaryEntry entry1 = DiaryEntry.of("", LocalDateTime.now().minusDays(3));
		entry1.setContacts(contactsOfPerson3);
		// add 'husten'
		List<Symptom> symptomsE1 = new ArrayList<>();
		Symptom cough = symptoms.findById(UUID.fromString("e5cea3b0-c8f4-4e03-a24e-89213f3f6637")).orElse(null);
		symptomsE1.add(cough);
		entry1.setSymptoms(symptomsE1);
		entry1.setBodyTemperature(BodyTemperature.of(37.5f));
		INDEX_PERSON3_WITH_ACTIVE_TRACKING.addDiaryEntry(entry1);

		DiaryEntry entry2 = DiaryEntry.of("", LocalDateTime.now().minusDays(2));
		// add 'husten' and 'Nackenschmerzen'
		List<Symptom> symptomsE2 = new ArrayList<>();
		Symptom neckProblems = symptoms.findById(UUID.fromString("e5cea3b0-c8f4-4e03-a24e-89213f3f6637")).orElse(null);
		symptomsE2.add(neckProblems);
		symptomsE2.add(cough);
		entry2.setSymptoms(symptomsE2);
		entry2.setContacts(contactsOfPerson3.subList(0, 0));
		entry2.setBodyTemperature(BodyTemperature.of(37.8f));
		INDEX_PERSON3_WITH_ACTIVE_TRACKING.addDiaryEntry(entry2);

		DiaryEntry entry3 = DiaryEntry.of("", LocalDateTime.now().minusDays(1));
		// add 'husten' and 'Nackenschmerzen'
		List<Symptom> symptomsE3 = new ArrayList<>();
		symptomsE3.add(cough);
		entry3.setSymptoms(symptomsE3);
		entry3.setContacts(contactsOfPerson3.subList(1, 1));
		entry3.setBodyTemperature(BodyTemperature.of(39.7f));
		INDEX_PERSON3_WITH_ACTIVE_TRACKING.addDiaryEntry(entry3);

		trackedPeople.save(INDEX_PERSON1_NOT_REGISTERED);
		trackedPeople.save(INDEX_PERSON2_IN_ENROLLMENT);
		trackedPeople.save(INDEX_PERSON3_WITH_ACTIVE_TRACKING);
	}
}
