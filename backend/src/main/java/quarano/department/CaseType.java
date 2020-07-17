package quarano.department;

import java.util.Set;

/**
 * @author Patrick Otto
 * @author Oliver Drotbohm
 * @author Felix Schultze
 */
public enum CaseType {

	/**
	 * A case of an originally tested person.
	 */
	INDEX,

	/**
	 * A case for a general contact of an originally tested person
	 */
	CONTACT,

	/**
	 * A contact case for someone with some medical association.
	 */
	CONTACT_MEDICAL,

	/**
	 * A contact case for someone at high risk.
	 */
	CONTACT_VULNERABLE;

	public CaseType getPrimaryCaseType() {

		switch (this) {
			case CONTACT:
			case CONTACT_MEDICAL:
			case CONTACT_VULNERABLE:
				return CaseType.CONTACT;
			default:
				return CaseType.INDEX;
		}
	}

	public Set<CaseType> getAllTypes() {

		switch (this) {
			case CONTACT:
				return Set.of(CONTACT, CONTACT_MEDICAL, CONTACT_VULNERABLE);

			default:
				return Set.of(this);
		}
	}
}
