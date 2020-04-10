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

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Oliver Drotbohm
 */
@Embeddable
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PhoneNumber {

	// TODO - More restrictive
	public static final Pattern REGEX = Pattern.compile("^[\\+\\(\\)0-9\\s-]*?$");

	private final @Column(name = "phoneNumber") String value;

	public static PhoneNumber of(String number) {

		if (!isValid(number)) {
			throw new IllegalArgumentException(String.format("%s is not a valid phone number!", number));
		}

		return new PhoneNumber(number);
	}

	public static boolean isValid(String candidate) {
		return REGEX.matcher(candidate).matches();
	}
}
