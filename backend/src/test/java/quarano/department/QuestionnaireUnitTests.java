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

import quarano.department.Questionnaire.SymptomInformation;
import quarano.reference.Symptom;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * @author Oliver Drotbohm
 * @author Patrick Otto
 */
class QuestionnaireUnitTests {

	@Mock
	Symptom symptom;

	@Test
	void doesNotCarryDateAndSymptomListOfFirstSymptomIfWithoutSymptom() {

		var report = allMinimalInfoFalse();

		assertThat(report.hasSymptoms()).isFalse();
		assertThat(report.getDayOfFirstSymptoms()).isNull();
		assertThat(report.getSymptoms()).isNull();
	}

	@Test
	void exposesHasSymptomsIfDayOfFirstSymptomsIsSet() {

		var report = minimalQuestionnaireWithSymptoms();
		
		assertThat(report.hasSymptoms()).isTrue();
		assertThat(report.getDayOfFirstSymptoms()).isNotNull();
		assertThat(report.getSymptoms()).hasSize(2);
	}
	
	
	@Test
	void exposesIsMedicalStaffIfCreatedWithDescription() {

		var questionnaire = new Questionnaire(SymptomInformation.withoutSymptoms(), null,  "Medical Staff");
		
		assertThat(questionnaire.isMedicalStaff()).isTrue();
		assertThat(questionnaire.belongsToMedicalStaff()).isTrue();
		assertThat(questionnaire.hasSymptoms()).isFalse();
		assertThat(questionnaire.hasPreExistingConditions()).isFalse();
		assertThat(questionnaire.getDayOfFirstSymptoms()).isNull();
		assertThat(questionnaire.getSymptoms()).isNull();
	}
	
	@Test
	void exposesHasPreExistingConditionsIfCreatedWithDescription() {

		var questionnaire = new Questionnaire(SymptomInformation.withoutSymptoms(), "my Preexisting condition",  null);
		
		assertThat(questionnaire.hasPreExistingConditions()).isTrue();
		assertThat(questionnaire.getHasPreExistingConditionsDescription()).isEqualTo("my Preexisting condition");
		
		assertThat(questionnaire.isMedicalStaff()).isFalse();
		assertThat(questionnaire.getBelongToMedicalStaffDescription()).isNull();
		assertThat(questionnaire.belongsToMedicalStaff()).isFalse();
		assertThat(questionnaire.hasSymptoms()).isFalse();
		assertThat(questionnaire.getDayOfFirstSymptoms()).isNull();
		assertThat(questionnaire.getSymptoms()).isNull();
	}
	
	@Test
	void exceptionIsRaisedIfCreatedWithoutCompleteSymptomsData() {

		assertThatCode(() -> new Questionnaire(SymptomInformation.withSymptomsSince(null, Arrays.asList(symptom, symptom)), null,  null)).isInstanceOf(IllegalArgumentException.class);
		assertThatCode(() -> new Questionnaire(SymptomInformation.withSymptomsSince(LocalDate.now(), null), null,  null)).isInstanceOf(IllegalArgumentException.class);

	}

	private Questionnaire allMinimalInfoFalse() {
		return new MinimalQuestionnaire();
	}
	
	private Questionnaire minimalQuestionnaireWithSymptoms() {
		var now = LocalDate.now();
		var listOfSymptom = Arrays.asList(symptom, symptom);
		return new Questionnaire(SymptomInformation.withSymptomsSince(now, listOfSymptom), null, null);
	}
}
