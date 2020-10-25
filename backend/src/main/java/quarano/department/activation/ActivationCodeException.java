package quarano.department.activation;

import lombok.Getter;
import quarano.department.activation.ActivationCode.ActivationCodeIdentifier;

/**
 * @author Oliver Drotbohm
 */
@Getter
public class ActivationCodeException extends RuntimeException {

	private static final long serialVersionUID = -1862988574924745449L;

	private final ActivationCodeIdentifier code;
	private final Status status;

	public static ActivationCodeException notFound(ActivationCodeIdentifier id) {
		return new ActivationCodeException(id, Status.NOT_FOUND, "Could not find activation code " + id.toString());
	}

	public static ActivationCodeException expired(ActivationCode code) {
		return new ActivationCodeException(code.getId(), Status.EXPIRED, "Activation code " + code.getId() + " expired!");
	}

	public static ActivationCodeException usedOrCanceled(ActivationCode code) {
		return new ActivationCodeException(code.getId(), Status.USED_OR_CANCELED,
				"Activation code " + code.getId() + " was already used or canceled!");
	}

	public static ActivationCodeException activationConcluded() {
		return new ActivationCodeException(null, Status.USED_OR_CANCELED, "Account was already activated!");
	}

	private ActivationCodeException(ActivationCodeIdentifier code, Status status, String message) {

		super(message);
		this.code = code;
		this.status = status;
	}

	public enum Status {
		NOT_FOUND, EXPIRED, USED_OR_CANCELED;
	}
}
