package quarano.department.activation;

import lombok.Getter;
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

	/**
	 * When a new contact person is created, an automatic notification is sent to them. With this switch it can be
	 * determined whether an activation code is also generated.
	 * 
	 * @since 1.4
	 */
	private final @Getter boolean createAutomaticForNewContacts;

	public LocalDateTime getExpiryDate() {
		return LocalDateTime.now().plus(expiration);
	}
}
