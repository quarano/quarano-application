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

import lombok.RequiredArgsConstructor;
import quarano.department.TrackedCaseProperties;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
@JsonAutoDetect(getterVisibility = Visibility.NON_PRIVATE)
class TrackedCaseDefaults {

	private final LocalDate now = LocalDate.now();
	private final TrackedCaseProperties properties;

	LocalDate getTestDate() {
		return now;
	}

	LocalDate getQuarantineStart() {
		return now;
	}

	LocalDate getQuarantineEnd() {
		return now.plus(properties.getQuarantinePeriod());
	}
}
