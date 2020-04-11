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
package quarano.department.web;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

/**
 * @author Oliver Drotbohm
 */
public class InitialReportDtoUnitTests {

	@Test
	void rejectsEmptyDayOfFirstSymptomsIfSymptomatic() {

		var dto = new InitialReportDto().setHasSymptoms(true);

		Errors errors = dto.validate(new MapBindingResult(new HashMap<>(), "name"));

		assertThat(errors.hasFieldErrors("dayOfFirstSymptoms")).isTrue();
	}
}
