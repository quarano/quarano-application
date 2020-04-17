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

import quarano.tracking.ContactPerson;
import quarano.tracking.Encounter;
import quarano.tracking.TrackedPerson.EncounterReported;
import quarano.tracking.TrackedPersonDataInitializer;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Oliver Drotbohm
 */
@ExtendWith(MockitoExtension.class)
public class TrackingEventListenerUnitTests {

	@Mock TrackedCaseRepository cases;

	@Test
	void rejectsEncounterReportIfEnrollmentQuestionnaireIncomplete() {

		var listener = new TrackingEventListener(cases);

		var person = TrackedPersonDataInitializer.createTanja();
		var contactPerson = new ContactPerson("Michaela", "Mustermann");
		var event = EncounterReported.of(Encounter.with(contactPerson, LocalDate.now()), person.getId());

		var trackedCase = new TrackedCase(person, new Department("Mannheim", UUID.randomUUID()));
		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));

		assertThatExceptionOfType(EnrollmentException.class).isThrownBy(() -> {
			listener.on(event);
		});

		trackedCase.markEnrollmentDetailsSubmitted() //
				.submitQuestionnaire(new CompletedInitialReport());

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();
	}
}
