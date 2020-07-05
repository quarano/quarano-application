package quarano.department.web;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.validation.MapBindingResult;

/**
 * @author Oliver Drotbohm
 */
class QuestionnaireDtoUnitTests {

	@Test
	void rejectsEmptyDayOfFirstSymptomsIfSymptomatic() {

		var dto = new QuestionnaireDto()
				.setHasSymptoms(true)
				.setBelongToMedicalStaff(true)
				.setHasPreExistingConditions(true);

		var errors = new MapBindingResult(new HashMap<>(), "name");

		dto.validate(errors);

		assertThat(errors.hasFieldErrors("dayOfFirstSymptoms")).isTrue();
		assertThat(errors.hasFieldErrors("belongToMedicalStaffDescription")).isTrue();
		assertThat(errors.hasFieldErrors("hasPreExistingConditionsDescription")).isTrue();
	}

}
