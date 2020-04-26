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
package quarano.department;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactPerson.ContactPersonAdded;
import quarano.tracking.ContactWays;
import quarano.tracking.TrackedPersonDataInitializer;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

/**
 * @author Patrick Otto
 */
@QuaranoUnitTest
class ContactPersonEventListenerUnitTests {

	@Mock TrackedCaseRepository cases;

	@Test
	void createContactCaseAutomatically() {

		var listener = new ContactPersonCreationEventListener(cases);
		
		var person = TrackedPersonDataInitializer.createTanja();
		var contactPerson = new ContactPerson("Michaela", "Mustermann",
				ContactWays.ofEmailAddress("michaela@mustermann.de"));
		contactPerson.assignOwner(person);
		var event = ContactPersonAdded.of(contactPerson);

		var trackedCase = new TrackedCase(person,  CaseType.INDEX, new Department("Mannheim", UUID.randomUUID()));
		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));
		
		
		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();
		
		ArgumentCaptor<TrackedCase> argumentCaptor = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor.capture());

		TrackedCase capturedArgument = argumentCaptor.<TrackedCase> getValue();
		assertThat(capturedArgument.getTrackedPerson().getFirstName().equals("Michaela"));
		assertThat(capturedArgument.getTrackedPerson().getLastName().equals("Mustermann"));
		assertThat(capturedArgument.getTrackedPerson().getEmailAddress().toString().equals("michaela@mustermann.de"));
		assertThat(capturedArgument.getType() == CaseType.CONTACT);
		
	}
	
}
