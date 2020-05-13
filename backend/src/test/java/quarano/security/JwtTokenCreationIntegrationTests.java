package quarano.security;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.account.AccountService;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class JwtTokenCreationIntegrationTests {

	private final AccountService accounts;
	private final JwtProperties configuration;

	@Test
	void generatesTokenForDepartmentStaff() {

		assertThat(accounts.findByUsername("agent1") //
				.map(configuration::generateTokenFor) //
				.map(configuration::createToken)) //
						.hasValueSatisfying(it -> {
							assertThat(it.getUsername()).isEqualTo("agent1");
						});
	}
}
