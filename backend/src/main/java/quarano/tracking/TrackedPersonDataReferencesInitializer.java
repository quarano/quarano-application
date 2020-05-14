package quarano.tracking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.DataInitializer;
import quarano.reference.Symptom;
import quarano.reference.SymptomRepository;

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
	private final DiaryEntryRepository entries;
	private final ContactPersonRepository contacts;
	private final SymptomRepository symptoms;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		// get existing people that have already been initialized
		var sandra = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2).orElseThrow();
		var gustav = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON4_ID_DEP1).orElseThrow();
		var nadine = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON5_ID_DEP1).orElseThrow();
		var siggi = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_SEC1_ID_DEP1).orElseThrow();

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

		DiaryEntry entry1 = DiaryEntry.of(sameSlotYesterday, sandra) //
				.setContacts(contactsOfPerson3);
		// add 'husten'
		List<Symptom> symptomsE1 = new ArrayList<>();
		Symptom cough = symptoms.findById(UUID.fromString("e5cea3b0-c8f4-4e03-a24e-89213f3f6637")).orElse(null);
		symptomsE1.add(cough);
		entry1.setSymptoms(symptomsE1);
		entry1.setBodyTemperature(BodyTemperature.of(37.5f));

		entries.save(entry1);

		DiaryEntry entry2 = DiaryEntry.of(sameSlotYesterday.previous(), sandra) //
				.setContacts(contactsOfPerson3.subList(0, 0)) //
				.setBodyTemperature(BodyTemperature.of(37.8f));

		// add 'husten' and 'Nackenschmerzen'
		List<Symptom> symptomsE2 = new ArrayList<>();
		Symptom neckProblems = symptoms.findById(UUID.fromString("571a03cd-173c-4499-995c-d6a003e8c032")).orElse(null);
		symptomsE2.add(neckProblems);
		symptomsE2.add(cough);
		entry2.setSymptoms(symptomsE2);

		entries.save(entry2);

		DiaryEntry entry3 = DiaryEntry.of(sameSlotYesterday.previous().previous(), sandra) //
				.setContacts(contactsOfPerson3.subList(1, 1)) //
				.setBodyTemperature(BodyTemperature.of(39.7f));

		// add 'husten' and 'Nackenschmerzen'
		List<Symptom> symptomsE3 = new ArrayList<>();
		symptomsE3.add(cough);
		entry3.setSymptoms(symptomsE3);

		entries.save(entry3);

		// ==================== SIGGI ==================
		List<ContactPerson> contactsOfSiggi = new ArrayList<>();

		var contactOfSiggi = new ContactPerson("Melanie", "Maurer", ContactWays.ofEmailAddress("malanie@testtest.de")); //
		contactOfSiggi.assignOwner(siggi);
		contactsOfSiggi.add(contactOfSiggi);

		var contactOfSiggi2 = new ContactPerson("Dorothea", "Drogler", ContactWays.ofEmailAddress("doro@testtest.de")) //
				.setIsHealthStaff(true);
		contactOfSiggi2.assignOwner(siggi);
		contactsOfSiggi.add(contactOfSiggi2);

		contacts.saveAll(contactsOfSiggi);

		// generate diary entries for person 3
		Slot sameSlotYesterdaySiggi = Slot.now().previous().previous();

		DiaryEntry entry1Siggi = DiaryEntry.of(sameSlotYesterdaySiggi, siggi) //
				.setContacts(contactsOfSiggi);
		// add 'husten'
		List<Symptom> symptomsS1 = new ArrayList<>();
		symptomsS1.add(cough);
		entry1Siggi.setSymptoms(symptomsS1);
		entry1Siggi.setBodyTemperature(BodyTemperature.of(38.5f));

		entries.save(entry1Siggi);

		DiaryEntry entry2Siggi = DiaryEntry.of(sameSlotYesterdaySiggi.previous(), siggi) //
				.setContacts(contactsOfSiggi.subList(0, 0)) //
				.setBodyTemperature(BodyTemperature.of(37.8f));

		// add 'husten' and 'Nackenschmerzen'
		List<Symptom> symptomsS2 = new ArrayList<>();
		symptomsS2.add(neckProblems);
		symptomsS2.add(cough);
		entry2Siggi.setSymptoms(symptomsS2);

		entries.save(entry2Siggi);

		DiaryEntry entry3Siggi = DiaryEntry.of(sameSlotYesterdaySiggi.previous().previous(), siggi) //
				.setContacts(contactsOfSiggi.subList(1, 1)) //
				.setBodyTemperature(BodyTemperature.of(39.7f));

		// add 'husten' and 'Nackenschmerzen'
		List<Symptom> symptomsS3 = new ArrayList<>();
		symptomsE3.add(cough);
		entry3Siggi.setSymptoms(symptomsS3);

		entries.save(entry3Siggi);

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

		DiaryEntry entry1G = DiaryEntry.of(sameSlotYesterdayGustav, gustav);
		entry1G.setContacts(contactsOfPerson4);
		// add 'husten'
		List<Symptom> symptomsE1G = new ArrayList<>();
		symptomsE1G.add(cough);
		entry1G.setSymptoms(symptomsE1G);
		entry1G.setBodyTemperature(BodyTemperature.of(36.5f));

		entries.save(entry1G);

		DiaryEntry entry2G = DiaryEntry.of(sameSlotYesterdayGustav.previous(), gustav) //
				.setContacts(contactsOfPerson4.subList(0, 0)) //
				.setBodyTemperature(BodyTemperature.of(37.8f));

		// add 'husten' and 'Nackenschmerzen'
		List<Symptom> symptomsE2G = new ArrayList<>();
		symptomsE2G.add(neckProblems);
		symptomsE2G.add(cough);
		entry2G.setSymptoms(symptomsE2G);

		entries.save(entry2G);

		// ==================== NADINE ==================
		List<ContactPerson> contactsOfPerson5 = new ArrayList<>();

		var contact1OfPerson5 = new ContactPerson("Sonja", "Sortig",
				ContactWays.ofEmailAddress("sonja.sortig@testtest.de")); //
		contact1OfPerson5.assignOwner(nadine);
		contactsOfPerson5.add(contact1OfPerson5);

		var contact2OfPerson5 = new ContactPerson("Manuel", "Mertens",
				ContactWays.ofEmailAddress("manuelmertens@testtest.de"));
		contact2OfPerson5.assignOwner(nadine);
		contactsOfPerson5.add(contact2OfPerson5);

		contacts.saveAll(contactsOfPerson5);

		// generate diary entries for person 3
		Slot sameSlotYesterdayN = Slot.now().previous().previous();

		DiaryEntry entry1N = DiaryEntry.of(sameSlotYesterdayN, nadine) //
				.setContacts(contactsOfPerson5) //
				.setBodyTemperature(BodyTemperature.of(36.5f));

		// add 'husten'
		List<Symptom> symptomsE1N = new ArrayList<>();
		symptomsE1G.add(cough);
		entry1N.setSymptoms(symptomsE1N);

		entries.save(entry1N);

		DiaryEntry entry2N = DiaryEntry.of(sameSlotYesterdayN.previous(), nadine) //
				.setContacts(contactsOfPerson5.subList(0, 0)) //
				.setBodyTemperature(BodyTemperature.of(35.8f));

		// add 'husten' and 'Nackenschmerzen'
		List<Symptom> symptomsE2N = new ArrayList<>();
		symptomsE2N.add(neckProblems);
		symptomsE2N.add(cough);
		entry2N.setSymptoms(symptomsE2N);

		entries.save(entry2N);

		Slot samelastSlot = Slot.now().previous();

		DiaryEntry entry3N = DiaryEntry.of(samelastSlot, nadine) //
				.setContacts(contactsOfPerson5) //
				.setBodyTemperature(BodyTemperature.of(36.5f));

		// add 'husten'
		List<Symptom> symptomsE3N = new ArrayList<>();
		symptomsE3N.add(cough);
		entry3N.setSymptoms(symptomsE3N);

		entries.save(entry3N);
	}
}
