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
package quarano.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("quarano.department")
public class DepartmentProperties {

	private final DefaultDepartment defaultDepartment;
	private final @Getter DefaultAdminAccount defaultAccount;

	@RequiredArgsConstructor
	static class DefaultDepartment {
		private final String name, emailAddress, phoneNumber;
	}

	@Getter
	@RequiredArgsConstructor
	static class DefaultAdminAccount {
		private final String firstname, lastname, emailAddress;
	}

	Department getDefaultDepartment() {

		return new Department(defaultDepartment.name) //
				.setEmailAddress(EmailAddress.of(defaultDepartment.emailAddress)) //
				.setPhoneNumber(PhoneNumber.of(defaultDepartment.phoneNumber));
	}
}
