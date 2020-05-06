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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.Password.UnencryptedPassword;
import quarano.core.EmailAddress;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
@Order(200)
@Slf4j
public class AccountBootstrap implements ApplicationRunner {

	private final DepartmentRepository departments;
	private final AccountService accounts;
	private final DepartmentProperties configuration;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {

		if (departments.count() > 0) {

			log.info("Found departments in database. Skipping default account creation.");

			return;
		}

		log.info("Creating default department.");

		var department = departments.save(configuration.getDefaultDepartment());
		var defaults = configuration.getDefaultAccount();

		log.info("Creating default account (root, root).");

		accounts.createStaffAccount("root", UnencryptedPassword.of("root"), //
				defaults.getFirstname(), //
				defaults.getLastname(), //
				EmailAddress.of(defaults.getEmailAddress()), //
				department.getId(), RoleType.ROLE_HD_ADMIN);
	}
}
