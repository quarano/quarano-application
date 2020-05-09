package quarano.department.web;

import lombok.Getter;
import lombok.NonNull;
import quarano.department.TrackedCase;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

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

		var questionnaire = trackedCase.getQuestionnaire();

		return questionnaire == null ? null : questionnaire.isMedicalStaff();
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
}
