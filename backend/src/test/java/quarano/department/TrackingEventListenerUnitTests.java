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
import quarano.tracking.RiskAssessment;
import quarano.tracking.TrackedPerson;
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
		var trackedCase = new TrackedCase(person, CaseType.INDEX, new Department("Mannheim", UUID.randomUUID())) //
				.submitEnrollmentDetails();
		var contactPerson = new ContactPerson("Michaela", "Mustermann",
				ContactWays.ofEmailAddress("michaela@mustermann.de"));
		var event = EncounterReported.firstEncounter(Encounter.with(contactPerson, LocalDate.now()), person.getId());
		
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
		var encounter = createFirstEncounterWithMichaelaFor(person);
		var trackedCase = createIndexCaseFor(person);

		var event = EncounterReported.firstEncounter(encounter, person.getId());

		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));

		var listener = new TrackingEventListener(cases);
		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		ArgumentCaptor<TrackedCase> argumentCaptor = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor.capture());

		TrackedCase capturedArgument = argumentCaptor.getValue();

		assertIdentityOfMichaela(capturedArgument);
		
		assertThat(capturedArgument.isContactCase()).isTrue();
	}
	
	@Test
	void createNoContactCaseAutomaticallyForSubsequentEncounter() {

		var person = TrackedPersonDataInitializer.createTanja();
		var encounter1 = createFirstEncounterWithMichaelaFor(person);
		var encounter2 = person.reportContactWith(encounter1.getContact(), LocalDate.now().minusDays(1));
		var trackedCase = createIndexCaseFor(person);
		var event = EncounterReported.firstEncounter(encounter1, person.getId());
		var event2 = EncounterReported.subsequentEncounter(encounter2, person.getId());

		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));

		var listener = new TrackingEventListener(cases);
		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();
		assertThatCode(() -> listener.on(event2)).doesNotThrowAnyException();
		
		ArgumentCaptor<TrackedCase> argumentCaptor = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor.capture());

	}


	@Test
	void createContactCaseWithTypeContactMedical() {

		var person = TrackedPersonDataInitializer.createTanja();
		var encounter = createFirstEncounterWithMichaelaFor(person);
		var assessment = new RiskAssessment();
		assessment.setIsHealthStaff(Boolean.TRUE);
		encounter.getContact().setRisk(assessment);
		var trackedCase = createIndexCaseFor(person);

		var event = EncounterReported.firstEncounter(encounter, person.getId());
		
		when(cases.findByTrackedPerson(person.getId())).thenReturn(Optional.of(trackedCase));

		var listener = new TrackingEventListener(cases);
		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		ArgumentCaptor<TrackedCase> argumentCaptor = ArgumentCaptor.forClass(TrackedCase.class);
		verify(cases, times(1)).save(argumentCaptor.capture());

		TrackedCase capturedArgument = argumentCaptor.getValue();

		assertIdentityOfMichaela(capturedArgument);
		assertThat(capturedArgument.isMedicalContactCase()).isTrue();
		
	}

	private void assertIdentityOfMichaela(TrackedCase capturedArgument) {
		assertThat(capturedArgument.getTrackedPerson().getFirstName()).isEqualTo("Michaela");
		assertThat(capturedArgument.getTrackedPerson().getLastName()).isEqualTo("Mustermann");
		assertThat(capturedArgument.getTrackedPerson().getEmailAddress())
				.isEqualTo(EmailAddress.of("michaela@mustermann.de"));
	}
	
	
	private Encounter createFirstEncounterWithMichaelaFor(TrackedPerson person) {
		
		var contactWays = ContactWays.ofEmailAddress("michaela@mustermann.de");
		var contactPerson = new ContactPerson("Michaela", "Mustermann", contactWays) //
				.assignOwner(person);
		var encounter = person.reportContactWith(contactPerson, LocalDate.now());
		
		return encounter;
	}
	

	private TrackedCase createIndexCaseFor(TrackedPerson person) {
		var trackedCase = new TrackedCase(person, CaseType.INDEX, new Department("Mannheim", UUID.randomUUID())) //
				.submitEnrollmentDetails() //
				.submitQuestionnaire(new CompletedInitialReport());
		return trackedCase;
	}
}
