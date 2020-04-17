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
import quarano.core.DataInitializer;
import quarano.tracking.Quarantine;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;

import java.time.LocalDate;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
@Order(660)
class TrackedCaseDataInitializer implements DataInitializer {

	private final TrackedCaseRepository cases;
	private final TrackedPersonRepository trackedPeople;
	private final DepartmentRepository departments;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		var person1 = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1).orElseThrow();
		var person2 = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON2_ID_DEP1).orElseThrow();
		var person3 = trackedPeople.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2).orElseThrow();

		cases.save(new TrackedCase() //
				.setTrackedPerson(person1) //
				.setType(CaseType.CONTACT) //
				.setDepartment(departments.findById(DepartmentDataInitializer.DEPARTMENT_ID_DEP1).orElse(null)));

		cases.save(new TrackedCase() //
				.setTrackedPerson(person2) //
				.setDepartment(departments.findById(DepartmentDataInitializer.DEPARTMENT_ID_DEP1).orElse(null)));

		LocalDate start = LocalDate.now().minusWeeks(1);
		LocalDate end = start.plusWeeks(4);

		cases.save(new TrackedCase() //
				.setTrackedPerson(person3) //
				.setDepartment(departments.findById(DepartmentDataInitializer.DEPARTMENT_ID_DEP2).orElse(null)) //
				.setQuarantine(Quarantine.of(start, end)) //
				.submitEnrollmentDetails() //
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
