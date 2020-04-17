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
import quarano.department.TrackedCase;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
public class TrackedCaseSummaryDto {

	private final TrackedCase trackedCase;

	public boolean getEnrollmentCompleted() {
		return trackedCase.getEnrollment().isComplete();
	}

	public String getFirstName() {
		return trackedCase.getTrackedPerson().getFirstName();
	}

	public String getLastName() {
		return trackedCase.getTrackedPerson().getLastName();
	}

	public String getZipCode() {

		var zipCode = trackedCase.getTrackedPerson().getAddress().getZipCode();

		return zipCode == null ? null : zipCode.toString();
	}

	public String getDateOfBirth() {
		return trackedCase.getTrackedPerson().getDateOfBirth().format(DateTimeFormatter.ISO_DATE);
	}

	public String getEmail() {
		return trackedCase.getTrackedPerson().getEmailAddress().toString();
	}

	public String getCaseType() {
		return trackedCase.getType().name().toLowerCase(Locale.US);
	}

	public Boolean isMedicalStaff() {

		var initialReport = trackedCase.getInitialReport();

		return initialReport == null ? null : initialReport.getBelongToMedicalStaff();
	}

	public Map<String, Object> getQuarantine() {

		if (!trackedCase.isInQuarantine()) {
			return null;
		}

		var quarantine = trackedCase.getQuarantine();

		return Map.of("from", quarantine.getFrom().format(DateTimeFormatter.ISO_DATE), //
				"to", quarantine.getTo().format(DateTimeFormatter.ISO_DATE));
	}
}
