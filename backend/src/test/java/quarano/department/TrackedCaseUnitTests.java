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

import quarano.tracking.ContactPerson;
import quarano.tracking.TrackedPerson;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link TrackedCase}.
 *
 * @author Oliver Drotbohm
 */
public class TrackedCaseUnitTests {

	@Test
	void rejectsEnrollmentCompletionForRequiredEncountersButNoneRegistered() {

		var person = new TrackedPerson("Michael", "Mustermann");
		var trackedCase = prepareTrackedCaseForCompletion(person);

		assertThatExceptionOfType(EnrollmentException.class)
				.isThrownBy(() -> trackedCase.markEnrollmentCompleted(EnrollmentCompletion.WITH_ENCOUNTERS));
		assertThat(trackedCase.getEnrollment().isComplete()).isFalse();
	}

	@Test
	void allowsEnrollmentCompletionWithoutEncountersIfExplicitlyAcknowledged() {

		var person = new TrackedPerson("Michael", "Mustermann");
		var trackedCase = prepareTrackedCaseForCompletion(person);

		assertThatCode(() -> trackedCase.markEnrollmentCompleted(EnrollmentCompletion.WITHOUT_ENCOUNTERS)) //
				.doesNotThrowAnyException();
		assertThat(trackedCase.getEnrollment().isComplete()).isTrue();
	}

	@Test
	void allowsCompletionWithEncountersIfRegistered() {

		var person = new TrackedPerson("Michael", "Mustermann");
		var trackedCase = prepareTrackedCaseForCompletion(person);

		person.reportContactWith(new ContactPerson("Michaela", "Mustermann"), LocalDate.now());

		assertThatCode(() -> trackedCase.markEnrollmentCompleted(EnrollmentCompletion.WITH_ENCOUNTERS)) //
				.doesNotThrowAnyException();
		assertThat(trackedCase.getEnrollment().isComplete()).isTrue();
	}

	@Test
	void reopensEnrollment() {

		var person = new TrackedPerson("Michael", "Mustermann");
		var trackedCase = prepareTrackedCaseForCompletion(person);

		assertThatCode(() -> trackedCase.markEnrollmentCompleted(EnrollmentCompletion.WITHOUT_ENCOUNTERS)) //
				.doesNotThrowAnyException();
		assertThat(trackedCase.getEnrollment().isComplete()).isTrue();

		trackedCase.reopenEnrollment();

		assertThat(trackedCase.getEnrollment().isComplete()).isFalse();
	}

	private static TrackedCase prepareTrackedCaseForCompletion(TrackedPerson person) {

		var department = new Department("Musterstadt", UUID.randomUUID());

		return new TrackedCase(person, department) //
				.markEnrollmentDetailsSubmitted() //
				.submitQuestionnaire(new CompletedInitialReport());
	}
}
