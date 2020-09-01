package quarano.department;

import lombok.RequiredArgsConstructor;
import quarano.account.DepartmentContact.ContactType;
import quarano.tracking.ContactTypeLookup;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import org.springframework.stereotype.Component;

/**
 * Default implementation for {@link ContactTypeLookup}.
 *
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class DefaultContactTypeLookup implements ContactTypeLookup {

	private final TrackedCaseRepository cases;

	/*
	 * (non-Javadoc)
	 * @see quarano.tracking.ContactTypeLookup#getBy(quarano.tracking.TrackedPerson.TrackedPersonIdentifier)
	 */
	@Override
	public ContactType getBy(TrackedPersonIdentifier identifier) {

		return cases.findByTrackedPerson(identifier)
				.map(TrackedCase::getType)
				.map(CaseType::toContactType)
				.orElse(ContactType.CONTACT);
	}
}
