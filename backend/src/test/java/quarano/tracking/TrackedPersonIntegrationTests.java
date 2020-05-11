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

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoIntegrationTest
class TrackedPersonIntegrationTests {

	private final TrackedPersonRepository people;
	private final ContactPersonRepository contacts;

	@Test
	public void registersEncounter() {

		var personId = TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2;
		var person = people.findById(personId).orElseThrow();

		var contact = contacts.save(new ContactPerson("Max", "Mustermann", ContactWays.ofEmailAddress("max@mustermann.de")) //
				.assignOwner(person));

		person.reportContactWith(contact, LocalDate.now());

		people.save(person);
	}
}
