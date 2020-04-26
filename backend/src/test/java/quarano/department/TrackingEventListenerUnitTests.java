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
import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactWays;
import quarano.tracking.EmailAddress;
import quarano.tracking.Encounter;
import quarano.tracking.TrackedPerson.EncounterReported;
import quarano.tracking.TrackedPersonDataInitializer;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

/**
 * @author Oliver Drotbohm
 * @author Patrick Otto
 */
@QuaranoUnitTest
class TrackingEventListenerUnitTests {

	@Mock TrackedCaseRepository cases;

	@Test
	void rejectsEncounterReportIfEnrollmentQuestionnaireIncomplete() {

		var listener = new TrackingEventListener(cases);

		var person = TrackedPersonDataInitializer.createTanja();
		var contactPerson = new ContactPerson("Michaela", "Mustermann",
				ContactWays.ofEmailAddress("michaela@mustermann.de"));
		var event = EncounterReported.firstEncounter(Encounter.with(contactPerson, LocalDate.now()), person.getId());

		var trackedCase = new TrackedCase(person, CaseType.INDEX, new Department("Mannheim", UUID.randomUUID()));
		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));

		assertThatExceptionOfType(EnrollmentException.class).isThrownBy(() -> {
			listener.on(event);
		});

		trackedCase.submitEnrollmentDetails() //
				.submitQuestionnaire(new CompletedInitialReport());

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();
	}

	@Test
	void createContactCaseAutomaticallyAfterFirstEncounter() {

		var person = TrackedPersonDataInitializer.createTanja();
		var contactWays = ContactWays.ofEmailAddress("michaela@mustermann.de");
		var contactPerson = new ContactPerson("Michaela", "Mustermann", contactWays) //
				.assignOwner(person);

		var encounter = person.reportContactWith(contactPerson, LocalDate.now());
		var event = EncounterReported.firstEncounter(encounter, person.getId());
		var trackedCase = new TrackedCase(person, CaseType.INDEX, new Department("Mannheim", UUID.randomUUID())) //
				.submitEnrollmentDetails() //
				.submitQuestionnaire(new CompletedInitialReport());

		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));

		var listener = new TrackingEventListener(cases);
		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		ArgumentCaptor<TrackedCase> argumentCaptor = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor.capture());

		TrackedCase capturedArgument = argumentCaptor.getValue();

		assertThat(capturedArgument.getTrackedPerson().getFirstName()).isEqualTo("Michaela");
		assertThat(capturedArgument.getTrackedPerson().getLastName()).isEqualTo("Mustermann");
		assertThat(capturedArgument.getTrackedPerson().getEmailAddress())
				.isEqualTo(EmailAddress.of("michaela@mustermann.de"));
		assertThat(capturedArgument.isContactCase()).isTrue();
	}
}
