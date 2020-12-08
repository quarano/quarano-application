package quarano.department.activation;

import java.time.LocalDateTime;

/**
 * Configuration options for working with {@link ActivationCode}s.
 *
 * @author Oliver Drotbohm
 */
public interface ActivationCodeProperties {

	/**
	 * Returns the expiry date to use for new {@link ActivationCode}s to be created.
	 *
	 * @return will never be {@literal null}.
	 */
	LocalDateTime getExpiryDate();
}
