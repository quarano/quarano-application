package quarano.department.activation;

import quarano.department.activation.ActivationCode.ActivationCodeIdentifier;

/**
 * @author Oliver Drotbohm
 * @author Jochen Kraushaar
 */
public class ActivationCodeException extends RuntimeException {

	private static final long serialVersionUID = -1862988574924745449L;

	private final Status status;

	private ActivationCodeException(String message, Status status) {

		super(message);

		this.status = status;
	}

	static ActivationCodeException notFound(ActivationCodeIdentifier id) {
		return new ActivationCodeException("Could not find activation code " + id.toString(),
				Status.NOT_FOUND);
	}

	static ActivationCodeException expired(ActivationCode code) {
		return new ActivationCodeException("Activation code " + code.getId() + " expired!",
				Status.EXPIRED);
	}

	static ActivationCodeException usedOrCanceled(ActivationCode code) {
		return new ActivationCodeException(
				"Activation code " + code.getId() + " was already used or canceled!",
				Status.USED_OR_CANCELED);
	}

	static ActivationCodeException canceled(ActivationCode code) {
		return new ActivationCodeException(
				"Activation code " + code.getId() + " is canceled!",
				Status.CANCELED);
	}

	static ActivationCodeException activationConcluded() {
		return new ActivationCodeException("Account was already activated!", Status.USED_OR_CANCELED);
	}

	public String getMessageKey() {
		return status.getMessageKey();
	}

	public enum Status {

		NOT_FOUND, EXPIRED, USED_OR_CANCELED, CANCELED;

		public String getMessageKey() {
			return "Invalid.activationCode." + name();
		}
	}
}
