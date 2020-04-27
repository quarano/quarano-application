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
import lombok.NonNull;
import quarano.core.EnumMessageSourceResolvable;
import quarano.department.CaseStatus;
import quarano.department.TrackedCase;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Oliver Drotbohm
 */
@Relation(collectionRelation = "cases")
public class TrackedCaseSummary extends RepresentationModel<TrackedCaseSummary> {

	private final @Getter(onMethod = @__(@JsonIgnore)) TrackedCase trackedCase;
	private final @NonNull MessageSourceAccessor messages;

	@SuppressWarnings("null")
	private TrackedCaseSummary(TrackedCase trackedCase, MessageSourceAccessor messages) {

		this.trackedCase = trackedCase;
		this.messages = messages;

		var controller = on(TrackedCaseController.class);

		add(Link.of(fromMethodCall(controller.concludeCase(trackedCase.getId(), null)).toUriString(),
				TrackedCaseRepresentations.CONCLUDE));
	}

	public static TrackedCaseSummary of(TrackedCase trackedCase, MessageSourceAccessor messages) {
		return new TrackedCaseSummary(trackedCase, messages);
	}

	public String getCaseId() {
		return trackedCase.getId().toString();
	}

	public boolean getEnrollmentCompleted() {
		return trackedCase.getEnrollment().isComplete();
	}

	public String getFirstName() {
		return trackedCase.getTrackedPerson().getFirstName();
	}

	public String getLastName() {
		return trackedCase.getTrackedPerson().getLastName();
	}

	public String getStatus() {
		return messages.getMessage(EnumMessageSourceResolvable.of(resolveStatus()));
	}

	public String getPrimaryPhoneNumber() {

		var trackedPerson = trackedCase.getTrackedPerson();
		var phoneNumber = trackedPerson.getPhoneNumber();
		var mobilePhoneNumber = trackedPerson.getMobilePhoneNumber();

		if (phoneNumber != null) {
			return phoneNumber.toString();
		} else {
			return mobilePhoneNumber == null ? null : mobilePhoneNumber.toString();
		}
	}

	public String getZipCode() {

		var zipCode = trackedCase.getTrackedPerson().getAddress().getZipCode();

		return zipCode == null ? null : zipCode.toString();
	}

	public String getDateOfBirth() {
		var person = trackedCase.getTrackedPerson();
		var dateOfBirth = person.getDateOfBirth();

		return dateOfBirth == null ? null : dateOfBirth.format(DateTimeFormatter.ISO_DATE);
	}

	public String getEmail() {

		var trackedPerson = trackedCase.getTrackedPerson();
		var emailAddress = trackedPerson.getEmailAddress();

		return emailAddress == null ? null : emailAddress.toString();
	}

	public String getCaseType() {
		return trackedCase.getType().name().toLowerCase(Locale.US);
	}

	public Boolean isMedicalStaff() {

		var initialReport = trackedCase.getInitialReport();

		return initialReport == null ? null : initialReport.getBelongToMedicalStaff();
	}

	@Nullable
	public Map<String, Object> getQuarantine() {

		if (!trackedCase.isInQuarantine()) {
			return null;
		}

		var quarantine = trackedCase.getQuarantine();

		return Map.of("from", quarantine.getFrom().format(DateTimeFormatter.ISO_DATE), //
				"to", quarantine.getTo().format(DateTimeFormatter.ISO_DATE));
	}

	private CaseStatus resolveStatus() {

		if (trackedCase.isConcluded()) {
			return CaseStatus.STOPPED;
		}

		if (hasLink(TrackedCaseRepresentations.START_TRACKING)) {
			return CaseStatus.OPENED;
		}

		if (hasLink(TrackedCaseRepresentations.RENEW)) {
			return CaseStatus.IN_REGISTRATION;
		}

		return trackedCase.getEnrollment().isComplete() //
				? CaseStatus.TRACKING_ACTIVE //
				: CaseStatus.REGISTRATION_COMPLETED;
	}
}
