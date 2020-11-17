package quarano.account;

import static org.assertj.core.api.Assertions.*;

import quarano.account.Department.DepartmentIdentifier;
import quarano.account.Password.EncryptedPassword;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Account}.
 *
 * @author Oliver Drotbohm
 */
class AccountUnitTests {

	@Test // CORE-92
	void settingPasswordAfterPasswordResetRequestsResetsRequest() {

		var account = new Account("username", EncryptedPassword.of("encrypted"), "Max", "Mustermann",
				DepartmentIdentifier.of(UUID.randomUUID()), new Role(RoleType.ROLE_USER));

		var token = account.requestPasswordReset(LocalDateTime.now().plusHours(12));

		assertThat(account.getPasswordResetToken()).isEqualTo(token);
		assertThat(account.setPassword(EncryptedPassword.of("otherEcrypted")).getPasswordResetToken()).isNull();
	}
}
