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

import lombok.RequiredArgsConstructor;
import quarano.tracking.TrackedPersonDataInitializer;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
@Order(660)
class TrackedCaseDataInitializer implements ApplicationListener<ApplicationStartedEvent> {

	private final TrackedCaseRepository cases;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {

		cases.save(new TrackedCase() //
				.setTrackedPerson(TrackedPersonDataInitializer.INDEX_PERSON1_NOT_REGISTERED) //
				.setDepartment(DepartmentDataInitializer.DEPARTMENT_1));

		cases.save(new TrackedCase()//
				.setTrackedPerson(TrackedPersonDataInitializer.INDEX_PERSON2_IN_ENROLLMENT)
				.setDepartment(DepartmentDataInitializer.DEPARTMENT_1));

		cases.save(new TrackedCase() //
				.setTrackedPerson(TrackedPersonDataInitializer.INDEX_PERSON3_WITH_ACTIVE_TRACKING)
				.setDepartment(DepartmentDataInitializer.DEPARTMENT_2) //
				.markEnrollmentDetailsSubmitted() //
				.submitQuestionnaire(new InitialReport() //
						.setBelongToLaboratoryStaff(true) //
						.setBelongToMedicalStaff(true) //
						.setBelongToNursingStaff(true) //
						.setDirectContactWithLiquidsOfC19pat(false) //
						.setFamilyMember(true) //
						.setFlightCrewMemberWithC19Pat(false) //
						.setFlightPassengerCloseRowC19Pat(true) //
						.setMin15MinutesContactWithC19Pat(true) //
						.setNursingActionOnC19Pat(false) //
						.withoutSymptoms()) //
				.markEnrollmentCompleted(EnrollmentCompletion.WITHOUT_ENCOUNTERS));
	}
}
