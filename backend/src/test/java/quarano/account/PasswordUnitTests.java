package quarano.account;

import static org.assertj.core.api.Assertions.*;

import quarano.account.Password.EncryptedPassword;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Password}.
 *
 * @author Jan Stamer
 * @author Oliver Drotbohm
 */
class PasswordUnitTests {

	@Test // CORE-231
	void isNotExpiredIfNoExpiryDateSet() {

		var password = EncryptedPassword.of("***");

		assertThat(password.isExpired()).isFalse();
	}

	@Test // CORE-231
	void isExpiredWithExpiryDateSet() {

		var password = EncryptedPassword.of("***", LocalDateTime.now().minusSeconds(1));

		assertThat(password.isExpired()).isTrue();
	}

	@Test // CORE-231
	void isNotExpiredWithFutureExpiryDate() {

		var password = EncryptedPassword.of("***", LocalDateTime.now().plusSeconds(1));

		assertThat(password.isExpired()).isFalse();
	}
}
