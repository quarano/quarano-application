package quarano.account;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.core.EmailAddress;

import org.junit.jupiter.api.Test;

/**
 * Integration tests for {@link AccountRepository}.
 *
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoIntegrationTest
class AccountRepositoryIntegrationTests {

	private final AccountRepository accounts;

	@Test // CORE-92
	void findsAccountByEmailAddress() {

		var account = accounts.findByEmailAddress(EmailAddress.of("markus.hanser@testtest.de"));
		assertThat(account).hasSize(1).anySatisfy(it -> assertThat(it.getUsername()).isEqualTo("DemoAccount"));
	}

	@Test // CORE-92
	void detectsAvailableUsername() {

		assertThat(accounts.isUsernameAvailable("DemoAccount")).isFalse();
		assertThat(accounts.isUsernameAvailable("testusername")).isTrue();
	}
}
