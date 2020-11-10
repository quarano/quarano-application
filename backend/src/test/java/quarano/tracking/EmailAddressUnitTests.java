package quarano.tracking;

import static org.assertj.core.api.Assertions.*;

import quarano.core.EmailAddress;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
class EmailAddressUnitTests {

	@Test
	void defaultsNullAndEmptySourceToNullEmailAddress() {

		assertThat(EmailAddress.ofNullable(null)).isNull();
		assertThat(EmailAddress.ofNullable("")).isNull();
	}

	@Test
	void acceptsSourceWithCapitalLetters() {

		assertThatCode(() -> EmailAddress.of("Foo@Bar.de"))
				.doesNotThrowAnyException();
	}
}
