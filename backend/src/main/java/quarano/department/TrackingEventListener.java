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
import lombok.extern.slf4j.Slf4j;
import quarano.tracking.ContactPerson;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.EncounterReported;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TrackingEventListener {

	private final @NonNull TrackedCaseRepository cases;

	@EventListener
	void on(EncounterReported event) {

		verifyEnrollmentCompleted(event);
		createContactCaseFor(event);
	}

	private void verifyEnrollmentCompleted(EncounterReported event) {

		var trackedCase = findTrackedCaseFor(event);
		var enrollment = trackedCase.getEnrollment();

		if (!enrollment.isCompletedQuestionnaire()) {
			throw new EnrollmentException("Cannot add contacts prior to completing the enrollment questionnaire!");
		}
	}

	/**
	 * Listens for first encounters with contacts and creates new cases from these contacts if they came from an Index
	 * person
	 *
	 * @param event
	 */
	private void createContactCaseFor(EncounterReported event) {

		ContactPerson contactPerson = event.getEncounter().getContact();

		try {

			if (event.isFirstEncounterWithTargetPerson()) {

				var caseOfContactInitializer = findTrackedCaseFor(event);

				// Only contacts of index-cases shall be converted to new cases automatically

				if (caseOfContactInitializer.isIndexCase()) {

					var person = new TrackedPerson(contactPerson);
					
					var caseType = (contactPerson.getIsHealthStaff() == Boolean.TRUE) ? CaseType.CONTACT_MEDICAL : CaseType.CONTACT;
					
					cases.save(new TrackedCase(person, caseType, caseOfContactInitializer.getDepartment()));

					log.info("Created automatic contact-case from contact " + contactPerson.getId());
				}
			}

		} catch (Exception e) {

			// just log the error, do not stop usual saving process of the encounter
			log.error("Error during automatical contact-case creation check for contact " + contactPerson.getId(), e);
		}
	}

	private TrackedCase findTrackedCaseFor(EncounterReported event) {

		return cases.findByTrackedPerson(event.getPersonIdentifier()) //
				.orElseThrow(() -> new IllegalStateException(
						"No tracked case found for tracked person " + event.getPersonIdentifier() + "!"));
	}
}
