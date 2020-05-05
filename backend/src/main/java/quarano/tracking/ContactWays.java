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

import lombok.Builder;
import lombok.Getter;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;

import org.springframework.util.StringUtils;

/**
 * Value object to encapsulate that at least one contact way of email address, phone number, mobile phone number or
 * identification hint has to be given.
 *
 * @author Oliver Drotbohm
 */
@Builder
@Getter
public class ContactWays {

	private final EmailAddress emailAddress;
	private final PhoneNumber phoneNumber, mobilePhoneNumber;
	private final String identificationHint;

	private ContactWays(EmailAddress emailAddress, PhoneNumber phoneNumber, PhoneNumber mobilePhoneNumber,
			String identificationHint) {

		if (emailAddress == null && phoneNumber == null && mobilePhoneNumber == null
				&& !StringUtils.hasText(identificationHint)) {
			throw new IllegalArgumentException("At least one contact way has to be given!");
		}

		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.mobilePhoneNumber = mobilePhoneNumber;
		this.identificationHint = identificationHint;
	}

	public static ContactWays ofEmailAddress(String emailAddress) {
		return new ContactWays(EmailAddress.of(emailAddress), null, null, null);
	}

	public static ContactWays ofPhoneNumber(String number) {
		return new ContactWays(null, PhoneNumber.of(number), null, null);
	}

	public static ContactWays ofMobilePhoneNumber(String number) {
		return new ContactWays(null, null, PhoneNumber.of(number), null);
	}

	public static ContactWays ofIdentificationHint(String hint) {
		return new ContactWays(null, null, null, hint);
	}
}
