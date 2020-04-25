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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 * @author Patrick Otto
 */
@Order(670)
@Component
@RequiredArgsConstructor
@Slf4j
public class TrackedPersonDataReferencesInitializer implements DataInitializer {

	private final TrackedPersonRepository trackedPeople;
	private final ContactPersonRepository contacts;
	private final SymptomRepository symptoms;

	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON1_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("738d3d1f-a9f1-4619-9896-2b5cb3a89c22"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON2_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("0c434624-7dbe-11ea-bc55-0242ac130003"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON4_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("a8bd1d2d-b824-4989-ad9f-73be224654d6"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON5_ID_DEP1 = TrackedPersonIdentifier
			.of(UUID.fromString("29206992-84f0-4a0e-9267-ed0a2b5b7507"));
	public final static TrackedPersonIdentifier VALID_TRACKED_PERSON3_ID_DEP2 = TrackedPersonIdentifier
			.of(UUID.fromString("1d5ce370-7dbe-11ea-bc55-0242ac130003"));


	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		// get existing people that have already been initialized
		var tanja = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1).orElseThrow();
		var markus = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON2_ID_DEP1).orElseThrow();
		var sandra = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2).orElseThrow();
		var gustav = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON4_ID_DEP1).orElseThrow();
		var nadine = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON5_ID_DEP1).orElseThrow();

		log.info("Start creating diary and contacts for test persons");

		// generate 2 contacts for Person 3
		
	// ==================== SANDRA ==================
		List<ContactPerson> contactsOfPerson3 = new ArrayList<>();
		
		var contact1OfPerson3 = new ContactPerson("Lora", "Laurer", ContactWays.ofEmailAddress("lora@testtest.de")); //
		contact1OfPerson3.assignOwner(sandra);
		contactsOfPerson3.add(contact1OfPerson3);

		var contact2OfPerson3 = new ContactPerson("Harry", "Huber", ContactWays.ofEmailAddress("harry@testtest.de"));
		contact2OfPerson3.assignOwner(sandra);
		contactsOfPerson3.add(contact2OfPerson3);

		contacts.saveAll(contactsOfPerson3);

		// generate diary entries for person 3
		Slot sameSlotYesterday = Slot.now().previous().previous();

		DiaryEntry entry1 = DiaryEntry.of(sameSlotYesterday);
		entry1.setContacts(contactsOfPerson3);
		// add 'husten'
		List<Symptom> symptomsE1 = new ArrayList<>();
		Symptom cough = symptoms.findById(UUID.fromString("e5cea3b0-c8f4-4e03-a24e-89213f3f6637")).orElse(null);
		symptomsE1.add(cough);
		entry1.setSymptoms(symptomsE1);
		entry1.setBodyTemperature(BodyTemperature.of(37.5f));

		trackedPeople.save(sandra.addDiaryEntry(entry1));

		DiaryEntry entry2 = DiaryEntry.of(sameSlotYesterday.previous());
		// add 'husten' and 'Nackenschmerzen'
		List<Symptom> symptomsE2 = new ArrayList<>();
		Symptom neckProblems = symptoms.findById(UUID.fromString("571a03cd-173c-4499-995c-d6a003e8c032")).orElse(null);
		symptomsE2.add(neckProblems);
		symptomsE2.add(cough);
		entry2.setSymptoms(symptomsE2);
		entry2.setContacts(contactsOfPerson3.subList(0, 0));
		entry2.setBodyTemperature(BodyTemperature.of(37.8f));
		trackedPeople.save(sandra.addDiaryEntry(entry2));

		DiaryEntry entry3 = DiaryEntry.of(sameSlotYesterday.previous().previous());
		// add 'husten' and 'Nackenschmerzen'
		List<Symptom> symptomsE3 = new ArrayList<>();
		symptomsE3.add(cough);
		entry3.setSymptoms(symptomsE3);
		entry3.setContacts(contactsOfPerson3.subList(1, 1));
		entry3.setBodyTemperature(BodyTemperature.of(39.7f));
		trackedPeople.save(sandra.addDiaryEntry(entry3));
		
		
		// ==================== GUSTAV ==================
		List<ContactPerson> contactsOfPerson4 = new ArrayList<>();
		
		var contact1OfPerson4 = new ContactPerson("Bert", "Baum", ContactWays.ofEmailAddress("bertbaum@testtest.de")); //
		contact1OfPerson4.assignOwner(gustav);
		contactsOfPerson4.add(contact1OfPerson3);

		var contact2OfPerson4 = new ContactPerson("Susi", "Söller", ContactWays.ofEmailAddress("susisoeller@testtest.de"));
		contact2OfPerson4.assignOwner(gustav);
		contactsOfPerson4.add(contact2OfPerson4);

		contacts.saveAll(contactsOfPerson4);

		// generate diary entries for person 3
		Slot sameSlotYesterdayGustav = Slot.now().previous().previous();

		DiaryEntry entry1G = DiaryEntry.of(sameSlotYesterdayGustav);
		entry1G.setContacts(contactsOfPerson4);
		// add 'husten'
		List<Symptom> symptomsE1G = new ArrayList<>();
		symptomsE1G.add(cough);
		entry1G.setSymptoms(symptomsE1G);
		entry1G.setBodyTemperature(BodyTemperature.of(36.5f));

		trackedPeople.save(gustav.addDiaryEntry(entry1G));

		DiaryEntry entry2G = DiaryEntry.of(sameSlotYesterdayGustav.previous());
		// add 'husten' and 'Nackenschmerzen'
		List<Symptom> symptomsE2G = new ArrayList<>();
		symptomsE2G.add(neckProblems);
		symptomsE2G.add(cough);
		entry2G.setSymptoms(symptomsE2G);
		entry2G.setContacts(contactsOfPerson4.subList(0, 0));
		entry2G.setBodyTemperature(BodyTemperature.of(37.8f));
		trackedPeople.save(gustav.addDiaryEntry(entry2G));
		
		
		// ==================== NADINE  ==================
		List<ContactPerson> contactsOfPerson5 = new ArrayList<>();
		
		var contact1OfPerson5 = new ContactPerson("Sonja", "Sortig", ContactWays.ofEmailAddress("sonja.sortig@testtest.de")); //
		contact1OfPerson5.assignOwner(nadine);
		contactsOfPerson5.add(contact1OfPerson5);

		var contact2OfPerson5 = new ContactPerson("Manuel", "Mertens", ContactWays.ofEmailAddress("manuelmertens@testtest.de"));
		contact2OfPerson5.assignOwner(nadine);
		contactsOfPerson5.add(contact2OfPerson5);

		contacts.saveAll(contactsOfPerson5);

		// generate diary entries for person 3
		Slot sameSlotYesterdayN = Slot.now().previous().previous();

		DiaryEntry entry1N = DiaryEntry.of(sameSlotYesterdayN);
		entry1N.setContacts(contactsOfPerson5);
		// add 'husten'
		List<Symptom> symptomsE1N = new ArrayList<>();
		symptomsE1G.add(cough);
		entry1N.setSymptoms(symptomsE1N);
		entry1N.setBodyTemperature(BodyTemperature.of(36.5f));

		trackedPeople.save(nadine.addDiaryEntry(entry1N));

		DiaryEntry entry2N = DiaryEntry.of(sameSlotYesterdayN.previous());
		// add 'husten' and 'Nackenschmerzen'
		List<Symptom> symptomsE2N = new ArrayList<>();
		symptomsE2N.add(neckProblems);
		symptomsE2N.add(cough);
		entry2N.setSymptoms(symptomsE2N);
		entry2N.setContacts(contactsOfPerson5.subList(0, 0));
		entry2N.setBodyTemperature(BodyTemperature.of(35.8f));
		trackedPeople.save(gustav.addDiaryEntry(entry2N));

		
	}
}
