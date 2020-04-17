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

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
class InitialReportUnitTests {

	@Test
	void isCompleteWithoutOtherContactTypeAndWithoutFirstDayOfSymptomsIfAsymptomatic() {

		var report = allButSymptomsAndOtherContact() //
				.withoutSymptoms();

		assertThat(report.isComplete()).isTrue();
	}

	@Test
	void isCompleteWithoutOtherContactTypeAndWithFirstDayOfSymptomsIfSymptomatic() {

		var report = allButSymptomsAndOtherContact() //
				.withFirstSymptomsAt(LocalDate.now());

		assertThat(report.isComplete()).isTrue();
	}

	@Test
	void doesNotCarryDateOfFirstSymptomIfWithoutSymptom() {

		var report = new InitialReport().withoutSymptoms();

		assertThat(report.getHasSymptoms()).isFalse();
		assertThat(report.getDayOfFirstSymptoms()).isNull();
	}

	@Test
	void exposesHasSymptomsIfDayOfFirstSymptomsIsSet() {

		var now = LocalDate.now();
		var report = new InitialReport().withFirstSymptomsAt(now);

		assertThat(report.getHasSymptoms()).isTrue();
		assertThat(report.getDayOfFirstSymptoms()).isEqualTo(now);
	}

	private static InitialReport allButSymptomsAndOtherContact() {

		return new InitialReport() //
				.setBelongToLaboratoryStaff(false) //
				.setBelongToMedicalStaff(false) //
				.setBelongToNursingStaff(false) //
				.setDirectContactWithLiquidsOfC19pat(false) //
				.setFamilyMember(false) //
				.setFlightCrewMemberWithC19Pat(false) //
				.setFlightPassengerCloseRowC19Pat(false) //
				.setMin15MinutesContactWithC19Pat(false) //
				.setNursingActionOnC19Pat(false);
	}
}
