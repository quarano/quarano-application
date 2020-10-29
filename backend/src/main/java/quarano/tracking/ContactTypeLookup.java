package quarano.tracking;

import quarano.account.DepartmentContact.ContactType;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

/**
 * API to lookup the {@link ContactType} based on the {@link TrackedPerson}.
 *
 * @author Oliver Drotbohm
 */
public interface ContactTypeLookup {

	/**
	 * Returns the {@link ContactType} to be used for the given {@link TrackedPerson}.
	 *
	 * @param person must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	default ContactType getBy(TrackedPerson person) {
		return getBy(person.getId());
	}

	/**
	 * Returns the {@link ContactType} to be used for the given {@link TrackedPersonIdentifier}.
	 *
	 * @param identifier must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	ContactType getBy(TrackedPersonIdentifier identifier);
}
