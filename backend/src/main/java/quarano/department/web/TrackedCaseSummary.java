package quarano.department.web;

import lombok.Getter;
import lombok.NonNull;
import quarano.core.EnumMessageSourceResolvable;
import quarano.department.CaseType;
import quarano.department.TrackedCase;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Relation(collectionRelation = "cases")
public class TrackedCaseSummary extends TrackedCaseStatusAware<TrackedCaseSummary> {

	private final @Getter(onMethod = @__(@JsonIgnore)) TrackedCase trackedCase;
	private final @NonNull MessageSourceAccessor messages;

	TrackedCaseSummary(TrackedCase trackedCase, MessageSourceAccessor messages) {

		super(trackedCase, messages);

		this.trackedCase = trackedCase;
		this.messages = messages;
	}

	public String getCaseId() {
		return trackedCase.getId().toString();
	}

	public boolean getEnrollmentCompleted() {
		return trackedCase.isEnrollmentCompleted();
	}

	public String getFirstName() {
		return trackedCase.getTrackedPerson().getFirstName();
	}

	public String getLastName() {
		return trackedCase.getTrackedPerson().getLastName();
	}

	public String getExtReferenceNumber() {
		return trackedCase.getExtReferenceNumber();
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
		return getPrimaryCaseType().name().toLowerCase(Locale.US);
	}

	public Boolean isMedicalStaff() {

		var questionnaire = trackedCase.getQuestionnaire();

		return questionnaire == null ? null : questionnaire.isMedicalStaff();
	}

	public String getCaseTypeLabel() {
		return messages.getMessage(EnumMessageSourceResolvable.of(trackedCase.getType()));
	}

	public String getCreatedAt() {
		return trackedCase.getMetadata().getCreated().format(DateTimeFormatter.ISO_DATE);
	}

	@Nullable
	public Map<String, Object> getQuarantine() {
		var quarantine = trackedCase.getQuarantine();

		if (quarantine == null) {
			return null;
		}

		Map<String, Object> map = new HashMap<>();
		Optional.ofNullable(quarantine.getFrom()).map(it -> it.format(DateTimeFormatter.ISO_DATE)).ifPresent(it -> map.put("from", it));
		Optional.ofNullable(quarantine.getTo()).map(it -> it.format(DateTimeFormatter.ISO_DATE)).ifPresent(it -> map.put("to", it));
		return map;
	}

	private CaseType getPrimaryCaseType() {

		switch (trackedCase.getType()) {
			case CONTACT:
			case CONTACT_MEDICAL:
			case CONTACT_VULNERABLE:
				return CaseType.CONTACT;
			default:
				return CaseType.INDEX;
		}
	}
}
