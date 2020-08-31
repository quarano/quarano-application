package quarano.diary;

import java.util.*;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.DataInitializer;
import quarano.reference.Symptom;
import quarano.reference.SymptomRepository;
import quarano.tracking.*;

/**
 * @author Oliver Drotbohm
 * @author Patrick Otto
 */
@Order(670)
@Component
@RequiredArgsConstructor
@Slf4j
class DiaryDataInitializer implements DataInitializer {

	private final TrackedPersonRepository trackedPeople;
	private final DiaryManagement entries;
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
		var jessica = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON4_ID_DEP2).orElseThrow();
		var gustav = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON4_ID_DEP1).orElseThrow();
		var nadine = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON5_ID_DEP1).orElseThrow();
		var siggi = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_SEC1_ID_DEP1).orElseThrow();

		var joel = trackedPeople.findById(TrackedPersonDataInitializer.INDEX_PERSON_DELETE1_DEP1).orElseThrow();
		var carlos = trackedPeople.findById(TrackedPersonDataInitializer.CONTACT_PERSON_DELETE1_DEP1).orElseThrow();

		log.info("Start creating diary and contacts for test persons");

		// generate 2 contacts for Person 3

		// ==================== SANDRA ==================
		List<ContactPerson> contactsOfSandra = new ArrayList<>();

		var contact1OfSandra = new ContactPerson("Lora", "Laurer", ContactWays.ofEmailAddress("lora@testtest.de"));
		contact1OfSandra.assignOwner(sandra);
		contactsOfSandra.add(contact1OfSandra);

		var contact2OfSandra = new ContactPerson("Harry", "Huber", ContactWays.ofEmailAddress("harry@testtest.de"));
		contact2OfSandra.assignOwner(sandra);
		contactsOfSandra.add(contact2OfSandra);

		contacts.saveAll(contactsOfSandra);

		// generate diary entries for person 3
		Slot sameSlotYesterday = Slot.now().previous().previous();

		DiaryEntry entry1 = DiaryEntry.of(sameSlotYesterday, sandra)
				.setContacts(contactsOfSandra);
		// add 'husten'
		Symptom cough = symptoms.findById(UUID.fromString("e5cea3b0-c8f4-4e03-a24e-89213f3f6637")).orElse(null);
		entry1.setSymptoms(List.of(cough));
		entry1.setBodyTemperature(BodyTemperature.of(37.5f));

		entries.updateDiaryEntry(entry1);

		DiaryEntry entry2 = DiaryEntry.of(sameSlotYesterday.previous(), sandra)
				.setContacts(contactsOfSandra.subList(0, 0))
				.setBodyTemperature(BodyTemperature.of(37.8f));

		// add 'husten' and 'Nackenschmerzen'
		Symptom neckProblems = symptoms.findById(UUID.fromString("571a03cd-173c-4499-995c-d6a003e8c032")).orElse(null);
		entry2.setSymptoms(List.of(neckProblems, cough));

		entries.updateDiaryEntry(entry2);

		DiaryEntry entry3 = DiaryEntry.of(sameSlotYesterday.previous().previous(), sandra)
				.setContacts(contactsOfSandra.subList(1, 2))
				.setBodyTemperature(BodyTemperature.of(39.7f));

		// add 'husten' and 'Nackenschmerzen'
		entry3.setSymptoms(List.of(cough));

		entries.updateDiaryEntry(entry3);

		// ==================== SIGGI ==================
		List<ContactPerson> contactsOfSiggi = new ArrayList<>();

		var contactOfSiggi = new ContactPerson("Melanie", "Maurer", ContactWays.ofEmailAddress("malanie@testtest.de"));
		contactOfSiggi.assignOwner(siggi);
		contactsOfSiggi.add(contactOfSiggi);

		var contactOfSiggi2 = new ContactPerson("Dorothea", "Drogler", ContactWays.ofEmailAddress("doro@testtest.de"))
				.setIsHealthStaff(true);
		contactOfSiggi2.assignOwner(siggi);
		contactsOfSiggi.add(contactOfSiggi2);

		contacts.saveAll(contactsOfSiggi);

		// generate diary entries for person 3
		Slot sameSlotYesterdaySiggi = Slot.now().previous().previous();

		DiaryEntry entry1Siggi = DiaryEntry.of(sameSlotYesterdaySiggi, siggi)
				.setContacts(contactsOfSiggi);
		// add 'husten'
		entry1Siggi.setSymptoms(List.of(cough));
		entry1Siggi.setBodyTemperature(BodyTemperature.of(38.5f));

		entries.updateDiaryEntry(entry1Siggi);

		DiaryEntry entry2Siggi = DiaryEntry.of(sameSlotYesterdaySiggi.previous(), siggi)
				.setContacts(contactsOfSiggi.subList(0, 0))
				.setBodyTemperature(BodyTemperature.of(37.8f));

		// add 'husten' and 'Nackenschmerzen'
		entry2Siggi.setSymptoms(List.of(neckProblems, cough));

		entries.updateDiaryEntry(entry2Siggi);

		DiaryEntry entry3Siggi = DiaryEntry.of(sameSlotYesterdaySiggi.previous().previous(), siggi)
				.setContacts(contactsOfSiggi.subList(1, 1))
				.setBodyTemperature(BodyTemperature.of(39.7f));

		// add 'husten' and 'Nackenschmerzen'
		entry3Siggi.setSymptoms(List.of(neckProblems, cough));

		entries.updateDiaryEntry(entry3Siggi);

		// ==================== GUSTAV ==================
		List<ContactPerson> contactsOfGustav = new ArrayList<>();

		var contact1OfPerson4 = new ContactPerson("Bert", "Baum", ContactWays.ofEmailAddress("bertbaum@testtest.de"));
		contact1OfPerson4.assignOwner(gustav);
		contactsOfGustav.add(contact1OfPerson4);

		var contact2OfPerson4 = new ContactPerson("Susi", "Söller", ContactWays.ofEmailAddress("susisoeller@testtest.de"));
		contact2OfPerson4.assignOwner(gustav);
		contactsOfGustav.add(contact2OfPerson4);

		contacts.saveAll(contactsOfGustav);

		// generate diary entries for person 3
		Slot sameSlotYesterdayGustav = Slot.now().previous().previous();

		DiaryEntry entry1G = DiaryEntry.of(sameSlotYesterdayGustav, gustav);
		entry1G.setContacts(contactsOfGustav);
		// add 'husten'
		entry1G.setSymptoms(List.of(cough));
		entry1G.setBodyTemperature(BodyTemperature.of(36.5f));

		entries.updateDiaryEntry(entry1G);

		DiaryEntry entry2G = DiaryEntry.of(sameSlotYesterdayGustav.previous(), gustav)
				.setContacts(contactsOfGustav.subList(0, 0))
				.setBodyTemperature(BodyTemperature.of(37.8f));

		// add 'husten' and 'Nackenschmerzen'
		entry2G.setSymptoms(List.of(neckProblems, cough));

		entries.updateDiaryEntry(entry2G);

		// ==================== NADINE ==================
		List<ContactPerson> contactsOfNadine = new ArrayList<>();

		var contact1OfPerson5 = new ContactPerson("Sonja", "Sortig",
				ContactWays.ofEmailAddress("sonja.sortig@testtest.de"));
		contact1OfPerson5.assignOwner(nadine);
		contactsOfNadine.add(contact1OfPerson5);

		var contact2OfPerson5 = new ContactPerson("Manuel", "Mertens",
				ContactWays.ofEmailAddress("manuelmertens@testtest.de"));
		contact2OfPerson5.assignOwner(nadine);
		contactsOfNadine.add(contact2OfPerson5);

		contacts.saveAll(contactsOfNadine);

		// generate diary entries for person 3
		Slot sameSlotYesterdayN = Slot.now().previous().previous();

		DiaryEntry entry1N = DiaryEntry.of(sameSlotYesterdayN, nadine)
				.setContacts(contactsOfNadine)
				.setBodyTemperature(BodyTemperature.of(36.5f));

		// add 'husten'
		entry1N.setSymptoms(List.of(cough));

		entries.updateDiaryEntry(entry1N);

		DiaryEntry entry2N = DiaryEntry.of(sameSlotYesterdayN.previous(), nadine)
				.setContacts(contactsOfNadine.subList(0, 0))
				.setBodyTemperature(BodyTemperature.of(35.8f));

		// add 'husten' and 'Nackenschmerzen'
		entry2N.setSymptoms(List.of(neckProblems, cough));

		entries.updateDiaryEntry(entry2N);

		Slot samelastSlot = Slot.now().previous();

		DiaryEntry entry3N = DiaryEntry.of(samelastSlot, nadine)
				.setContacts(contactsOfNadine)
				.setBodyTemperature(BodyTemperature.of(36.5f));

		// add 'husten'
		entry3N.setSymptoms(List.of(cough));

		entries.updateDiaryEntry(entry3N);

		// ==================== JESSICA =================
		List<ContactPerson> contactsOfJessica = new ArrayList<>();
		contacts.saveAll(contactsOfJessica);

		// generate diary entries for jessica
		DiaryEntry diary1 = DiaryEntry.of(sameSlotYesterday, jessica)
				.setContacts(contactsOfJessica);
		// add 'husten'
		diary1.setSymptoms(List.of(cough));
		diary1.setBodyTemperature(BodyTemperature.of(37.5f));

		entries.updateDiaryEntry(diary1);
		DiaryEntry diary2 = DiaryEntry.of(sameSlotYesterday.previous(), jessica)
				.setContacts(contactsOfJessica.subList(0, 0))
				.setBodyTemperature(BodyTemperature.of(37.8f));

		// add 'husten' and 'Nackenschmerzen'
		diary2.setSymptoms(List.of(neckProblems, cough));

		entries.updateDiaryEntry(diary2);

		DiaryEntry diary3 = DiaryEntry.of(sameSlotYesterday.previous().previous(), jessica)
				//.setContacts(contactsOfJessica.subList(1, 2))
				.setBodyTemperature(BodyTemperature.of(39.7f));

		// add 'husten' and 'Nackenschmerzen'
		diary3.setSymptoms(List.of(neckProblems, cough));

		entries.updateDiaryEntry(diary3);

		// ==================== JOEL ==================
		List<ContactPerson> contactsOfJoel = new ArrayList<>();

		var contact1OfJoel = new ContactPerson("Marta", "Fabiani", ContactWays.ofEmailAddress("marta.fabiani@testtest.de"));
		contact1OfJoel.assignOwner(joel);
		contactsOfJoel.add(contact1OfJoel);

		var contact2OfJoel = new ContactPerson("Francesca", "Fabiani", ContactWays.ofEmailAddress("francesca.fabiani@testtest.de"));
		contact2OfJoel.assignOwner(joel);
		contactsOfJoel.add(contact2OfJoel);

		contacts.saveAll(contactsOfJoel);

		// generate diary entries for Joel
		Slot sameSlotYesterdayJoel = Slot.now().previous().previous();

		DiaryEntry entry1J = DiaryEntry.of(sameSlotYesterdayJoel, joel)
				.setContacts(contactsOfJoel)
				.setSymptoms(List.of(cough))
				.setBodyTemperature(BodyTemperature.of(36.5f));

		entries.updateDiaryEntry(entry1J);

		DiaryEntry entry2J = DiaryEntry.of(sameSlotYesterdayJoel.previous(), joel)
				.setContacts(contactsOfJoel.subList(0, 0))
				.setBodyTemperature(BodyTemperature.of(37.8f))
				.setSymptoms(List.of(neckProblems, cough));

		entries.updateDiaryEntry(entry2J);

		// ==================== CARLOS ==================
		List<ContactPerson> contactsOfCarlos = new ArrayList<>();

		var contact1OfCarlos = new ContactPerson("Heribert", "Dorn", ContactWays.ofEmailAddress("heribert.dorn@testtest.de"));
		contact1OfCarlos.assignOwner(carlos);
		contactsOfCarlos.add(contact1OfCarlos);

		var contact2OfCarlos = new ContactPerson("Katharina", "Dorn", ContactWays.ofEmailAddress("katharina.dorn@testtest.de"));
		contact2OfCarlos.assignOwner(carlos);
		contactsOfCarlos.add(contact2OfCarlos);

		contacts.saveAll(contactsOfCarlos);

		// generate diary entries for person 3
		Slot sameSlotYesterdayCarlos = Slot.now().previous().previous();

		DiaryEntry entry1C = DiaryEntry.of(sameSlotYesterdayCarlos, carlos);
		entry1C.setContacts(contactsOfCarlos);
		// add 'husten'
		entry1C.setSymptoms(List.of(cough));
		entry1C.setBodyTemperature(BodyTemperature.of(36.5f));

		entries.updateDiaryEntry(entry1C);

		DiaryEntry entry2C = DiaryEntry.of(sameSlotYesterdayCarlos.previous(), carlos)
				.setContacts(contactsOfCarlos.subList(0, 0))
				.setBodyTemperature(BodyTemperature.of(37.8f));

		// add 'husten' and 'Nackenschmerzen'
		entry2C.setSymptoms(List.of(neckProblems, cough));

		entries.updateDiaryEntry(entry2C);
	}
}
