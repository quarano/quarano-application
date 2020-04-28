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
package quarano.auth;

import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;
import quarano.tracking.TrackedPersonRepository;

import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Oliver Drotbohm
 */
@QuaranoUnitTest
class SpringSecurityAuthenticationManagerUnitTest {

	@Mock TrackedPersonRepository repository;
	@Mock AccountService accounts;

	@AfterAll
	void cleanUp() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	@Test
	void looksUpTrackedPersonById() {

		var manager = new SpringSecurityAuthenticationManager(repository);
		var identifier = TrackedPersonIdentifier.of(UUID.randomUUID());

		SecurityContextHolder.getContext() //
				.setAuthentication(getAuthenticationWithDetailsOf(identifier));

		manager.getLoggedInTrackedPerson();

		verify(repository, times(1)).findById(identifier);
	}

	private static Authentication getAuthenticationWithDetailsOf(Object details) {

		var authentication = mock(Authentication.class);
		when(authentication.getDetails()).thenReturn(details);

		return authentication;
	}
}
