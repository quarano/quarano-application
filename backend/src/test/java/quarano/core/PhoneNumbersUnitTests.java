package quarano.core;

import static org.assertj.core.api.Assertions.*;

import quarano.core.PhoneNumber;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

/**
 * @author Oliver Drotbohm
 */
class PhoneNumbersUnitTests {

	Map<String, String> VALID_NUMBERS = Map.of(//
			"0123456789", "0123456789",
			"+49123456789", "0123456789",
			"+49-123-456789", "0123456789",
			"+49(0)123 456789", "0123456789",
			"+48 (0) 123 456789", "0048123456789");

	@TestFactory
	Stream<DynamicTest> parsesValidPhoneNumbers() {

		return DynamicTest.stream(VALID_NUMBERS.keySet().iterator(),
				it -> String.format("%s is a valid phone number.", it),
				it -> PhoneNumber.isValid(it));
	}

	@TestFactory
	Stream<DynamicTest> replacesSuperfluosCharacters() {

		return DynamicTest.stream(VALID_NUMBERS.entrySet().iterator(),
				it -> String.format("Parses %s into %s.", it.getKey(), it.getValue()),
				it -> assertThat(PhoneNumber.of(it.getKey())).isEqualTo(PhoneNumber.of(it.getValue())));
	}

	@Test
	void defaultsNullAndEmptyValuesToNullNumber() {

		assertThat(PhoneNumber.ofNullable(null)).isNull();
		assertThat(PhoneNumber.ofNullable("")).isNull();
	}

	@Test
	void doesNotConsiderNullValidPhoneNumberSource() {
		assertThat(PhoneNumber.isValid("")).isFalse();
	}
}
