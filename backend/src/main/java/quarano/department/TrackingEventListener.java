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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.tracking.TrackedPerson.EncounterReported;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
public class TrackingEventListener {

	private final @NonNull TrackedCaseRepository cases;

	@EventListener
	void on(EncounterReported event) {

		var trackedCase = cases.findByTrackedPerson(event.getPersonIdentifier()) //
				.orElseThrow(() -> new IllegalStateException(
						"No tracked case found for tracked person " + event.getPersonIdentifier() + "!"));

		var enrollment = trackedCase.getEnrollment();

		if (!enrollment.isCompletedQuestionnaire()) {
			throw new EnrollmentException("Cannot add contacts prior to completing the enrollment questionnaire!");
		}
	}
}
