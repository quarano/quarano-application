package quarano.account;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordTest {

	@Test
	void isExpiredWithUnchangedPassword() {
		var password = Password.EncryptedPassword.of("***");
		assertThat(password.isExpired()).isFalse();
	}

	@Test
	void isExpiredWithUnchangedPasswordWithChangedPassword() {
		var password = Password.EncryptedPassword.of("***");
		password.setExpiryDate(LocalDateTime.now().minusDays(2));
		assertThat(password.isExpired()).isTrue();
	}

}
