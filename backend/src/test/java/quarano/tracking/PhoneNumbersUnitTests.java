/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.tracking;

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
			"0123456789", "0123456789", //
			"+49123456789", "0123456789", //
			"+49-123-456789", "0123456789", //
			"+49(0)123 456789", "0123456789", //
			"+48 (0) 123 456789", "0048123456789");

	@TestFactory
	Stream<DynamicTest> parsesValidPhoneNumbers() {

		return DynamicTest.stream(VALID_NUMBERS.keySet().iterator(), //
				it -> String.format("%s is a valid phone number.", it), //
				it -> PhoneNumber.isValid(it));
	}

	@TestFactory
	Stream<DynamicTest> replacesSuperfluosCharacters() {

		return DynamicTest.stream(VALID_NUMBERS.entrySet().iterator(), //
				it -> String.format("Parses %s into %s.", it.getKey(), it.getValue()), //
				it -> assertThat(PhoneNumber.of(it.getKey())).isEqualTo(PhoneNumber.of(it.getValue())));
	}

	@Test
	void defaultsNullAndEmptyValuesToNullNumber() {

		assertThat(PhoneNumber.ofNullable(null)).isNull();
		assertThat(PhoneNumber.ofNullable("")).isNull();
	}
}
