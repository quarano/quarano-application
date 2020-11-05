package quarano.department;

import static java.util.function.BinaryOperator.*;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.diary.DiaryManagement;
import quarano.tracking.ContactPerson;
import quarano.tracking.TrackedPerson;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Felix Schultze
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
public class ContactChaser {

	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull DiaryManagement diaries;

	/**
	 * @param startCase
	 * @since 1.4
	 */
	public Optional<Contact> findLastIndexCaseContactFor(TrackedCase startCase) {

		if (startCase.isIndexCase() || startCase.getOriginCases().isEmpty()) {
			return Optional.empty();
		}

		try {

			return startCase.getOriginCases().stream().distinct()
					.map(originCase -> {

						return startCase.getOriginContacts().stream()
								.flatMap(it -> lastContactFromIndexCase(originCase, it).stream())
								.reduce(BinaryOperator.maxBy(Comparator.comparing(Contact::getContactAt)))
								.orElseThrow();
					})
					.reduce(BinaryOperator.maxBy(Comparator.comparing(Contact::getContactAt)));

		} catch (NoSuchElementException e) {

			/*
			 * Only if there is an contact (encounter) for each original case (these can also be entered directly by the health
			 * authority employee) can the last contact be determined with certainty.
			 */

			return Optional.empty();
		}
	}

	public Stream<Contact> findIndexContactsFor(TrackedCase contactCase) {

		if (contactCase.isIndexCase()) {
			return Stream.empty();
		}

		return contactCase.getOriginContacts().stream().distinct()
				.flatMap(it -> cases.findByTrackedPerson(it.getOwnerId())
						.map(indexCase -> fromIndexCase(indexCase, it))
						.stream());
	}

	// having that index case, we need to find the contact date per encounters of index person
	private Contact fromIndexCase(TrackedCase indexCase, ContactPerson contactPerson) {

		var trackedPerson = indexCase.getTrackedPerson();
		var encounters = trackedPerson.getEncounters();
		var encounterDate = encounters.getDateOfFirstEncounterWith(contactPerson)
				.or(() -> diaries.findDiaryFor(trackedPerson).getDateOfFirstEncounterWith(contactPerson))
				.orElse(null);

		return new Contact(indexCase, contactPerson, encounterDate);
	}

	private Optional<Contact> lastContactFromIndexCase(TrackedCase indexCase, ContactPerson contactPerson) {

		var trackedPerson = indexCase.getTrackedPerson();
		var encounters = trackedPerson.getEncounters();
		var encounterDate = Stream.concat(
				encounters.getDateOfLastEncounterWith(contactPerson).stream(),
				diaries.findDiaryFor(trackedPerson).getDateOfLastEncounterWith(contactPerson).stream())
				.reduce(maxBy(Comparator.naturalOrder()));

		return encounterDate.map(it -> new Contact(indexCase, contactPerson, it));
	}

	@Getter
	public static class Contact {

		private final TrackedCase trackedCase;
		private final TrackedCaseIdentifier caseId;
		private final TrackedPerson person;
		private final LocalDate contactAt;
		private final ContactPerson contactPerson;

		private Contact(TrackedCase trackedCase, ContactPerson contactPerson, @Nullable LocalDate contactAt) {
			this.trackedCase = trackedCase;
			this.caseId = trackedCase.getId();
			this.person = trackedCase.getTrackedPerson();
			this.contactAt = contactAt;
			this.contactPerson = contactPerson;
		}
	}
}
