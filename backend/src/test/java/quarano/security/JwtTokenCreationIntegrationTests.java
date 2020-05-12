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
