package quarano.department;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import quarano.department.activation.ActivationCodeProperties;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
@ConstructorBinding
@ConfigurationProperties("quarano.registration")
@RequiredArgsConstructor
public class RegistrationProperties implements ActivationCodeProperties {

	private final Duration expiration;

	/**
	 * Whether to automatically initiate a new registration for contact cases on creation.
	 *
	 * @since 1.4
	 */
	private final @Getter boolean automaticallyInitiateRegistrationForContactCases;

	/*
	 * (non-Javadoc)
	 * @see quarano.department.activation.ActivationCodeProperties#getExpiryDate()
	 */
	public LocalDateTime getExpiryDate() {
		return LocalDateTime.now().plus(expiration);
	}
}
