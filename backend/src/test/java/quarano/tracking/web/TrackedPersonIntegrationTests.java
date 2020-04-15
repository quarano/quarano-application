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
package quarano.tracking.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.department.TrackingEventListener;
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactPersonRepository;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.EncounterReported;
import quarano.tracking.TrackedPersonRepository;

import java.time.LocalDate;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.moduliths.test.PublishedEvents;
import org.moduliths.test.PublishedEventsExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oliver Drotbohm
 */
@ActiveProfiles("integrationtest")
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
@ExtendWith(PublishedEventsExtension.class)
@Transactional
@Disabled
public class TrackedPersonIntegrationTests {

	private final TrackedPersonRepository people;
	private final ContactPersonRepository contacts;

	@MockBean TrackingEventListener listener;

	@Test
	void publishesEventOnEncounterReport(PublishedEvents events) {

		var person = people.save(new TrackedPerson("Michael", "Mustermann"));
		var contact = contacts.save(new ContactPerson("Michaela", "Mustermann"));

		person.reportContactWith(contact, LocalDate.now());

		people.save(person);

		var expected = events.ofType(EncounterReported.class) //
				.matching(it -> it.getPersonIdentifier().equals(person.getId()));

		assertThat(expected).hasSize(1);
	}
}
