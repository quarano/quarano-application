package quarano.department;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.tracking.ContactPerson;
import quarano.tracking.TrackedPerson;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContactChaser {
	private final TrackedCaseRepository cases;

	public Optional<Stream<Contact>> findIndexContactsFor(TrackedCase contactCase) {
		if (contactCase.isContactCase() || contactCase.isMedicalContactCase()) {
			Stream<Contact> contacts = contactCase.getOriginContacts().stream().distinct() //
					.map(trackedContactPerson -> cases.findByTrackedPerson(trackedContactPerson.getOwnerId()) //
							// having that index case, we need to find the contact date per encounters of index person
							.map(indexCase -> indexCase.getTrackedPerson().getEncounters().stream() //
									.filter(enc -> enc.getContact().equals(trackedContactPerson)).findFirst() //
									.map(encounter -> new Contact(indexCase, trackedContactPerson, encounter.getDate())) //
									.orElse(new Contact(indexCase, trackedContactPerson, null))
							) //
					) //
					.filter(Optional::isPresent).map(Optional::get);
			return Optional.of(contacts);
		} else {
			return Optional.empty();
		}
	}

	@Getter
	public static class Contact {
		private final TrackedCaseIdentifier caseId;
		private final TrackedPerson person;
		private final LocalDate contactAt;
		private final ContactPerson contactPerson;

		private Contact(TrackedCase trackedCase, ContactPerson contactPerson, @Nullable LocalDate contactAt) {
			this.caseId = trackedCase.getId();
			this.person = trackedCase.getTrackedPerson();
			this.contactAt = contactAt;
			this.contactPerson = contactPerson;
		}
	}
}
