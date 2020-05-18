package quarano.department;

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
}
