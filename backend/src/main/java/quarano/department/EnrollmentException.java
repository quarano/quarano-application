package quarano.department;

/**
 * @author Oliver Drotbohm
 */
public class EnrollmentException extends RuntimeException {

	private static final long serialVersionUID = 5076506209061485363L;

	/**
	 * @param message
	 */
	public EnrollmentException(String message) {
		super(message);
	}
}
