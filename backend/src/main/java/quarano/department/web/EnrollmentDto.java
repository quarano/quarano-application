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

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import quarano.department.Enrollment;
import quarano.tracking.web.TrackingController;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@RequiredArgsConstructor
public class EnrollmentDto {

	private final @Getter(onMethod = @__(@JsonUnwrapped)) Enrollment enrollment;

	@JsonProperty("_links")
	public Map<String, Object> getLinks() {

		var questionnareUri = fromMethodCall(on(TrackedCaseController.class).addQuestionaire(null, null, null))
				.toUriString();

		var detailsUri = fromMethodCall(on(TrackingController.class).enrollmentOverview(null)).toUriString();

		if (enrollment.isComplete()) {
			return Map.of(//
					"details", Map.of("href", detailsUri), //
					"questionnaire", Map.of("href", questionnareUri), //
					"contacts", Map.of("href", "/api/enrollment/contacts"));
		}

		if (enrollment.isCompletedQuestionnaire()) {
			return Map.of(//
					"details", Map.of("href", detailsUri), //
					"questionnaire", Map.of("href", questionnareUri), //
					"next", Map.of("href", "/api/enrollment/contacts"));
		}

		if (enrollment.isCompletedPersonalData()) {
			return Map.of(//
					"details", Map.of("href", detailsUri), //
					"next", Map.of("href", questionnareUri));
		}

		return Map.of(//
				"next", Map.of("href", detailsUri));
	}
}
