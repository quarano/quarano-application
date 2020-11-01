package quarano.department.activation;

import lombok.Getter;
import quarano.department.activation.ActivationCode.ActivationCodeIdentifier;

/**
 * @author Oliver Drotbohm
 */
@Getter
public class ActivationCodeException extends RuntimeException {

		private static final long serialVersionUID = -1862988574924745449L;

		private final String messageKey;

		public static ActivationCodeException notFound(ActivationCodeIdentifier id) {
				return new ActivationCodeException("Could not find activation code " + id.toString(),
						"Invalid.accountRegistration.clientCodeNotFound");
		}

		public static ActivationCodeException expired(ActivationCode code) {
				return new ActivationCodeException("Activation code " + code.getId() + " expired!",
						"Invalid.accountRegistration.clientCodeExpired");
		}

		public static ActivationCodeException usedOrCanceled(ActivationCode code) {
				return new ActivationCodeException(
						"Activation code " + code.getId() + " was already used or canceled!",
						"Invalid.accountRegistration.clientCodeUsedOrCanceled");
		}

		public static ActivationCodeException activationConcluded() {
				return new ActivationCodeException("Account was already activated!",
						"Invalid.accountRegistration.clientCodeUsedOrCanceled");
		}

		private ActivationCodeException(String message, String messageKey) {

				super(message);
				this.messageKey = messageKey;
		}
}
