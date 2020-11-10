package quarano.department.activation;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author Oliver Drotbohm
 */
@ConstructorBinding
@ConfigurationProperties("quarano.account-activation")
@RequiredArgsConstructor
public class ActivationCodeProperties {

	private final Duration expiration;

	public LocalDateTime getExpiryDate() {
		return LocalDateTime.now().plus(expiration);
	}
}
