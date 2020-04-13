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
package quarano.auth.web;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import quarano.auth.AccountRepository;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;
import quarano.tracking.TrackedPersonRepository;

/**
 * @author Oliver Drotbohm
 */
@ExtendWith(MockitoExtension.class)
public class AuthenticationManagerUnitTest {

	@Mock TrackedPersonRepository repository;
	@Mock AccountRepository accountRepository;

	@Test
	void looksUpTrackedPersonByLegacyIdIfNeeded() {

		var manager = new AuthenticationManager(repository,accountRepository);

		SecurityContextHolder.getContext() //
				.setAuthentication(getAuthenticationWithDetailsOf(4711L));

		manager.getCurrentUser();

		verify(repository, times(1)).findByLegacyClientId(4711L);
	}

	@Test
	void looksUpTrackedPersonById() {

		var manager = new AuthenticationManager(repository, accountRepository);
		var identifier = TrackedPersonIdentifier.of(UUID.randomUUID());

		SecurityContextHolder.getContext() //
				.setAuthentication(getAuthenticationWithDetailsOf(identifier));

		manager.getCurrentUser();

		verify(repository, times(1)).findById(identifier);
	}

	private static Authentication getAuthenticationWithDetailsOf(Object details) {

		var authentication = mock(Authentication.class);
		when(authentication.getDetails()).thenReturn(details);

		return authentication;
	}
}
