package quarano.department.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.core.EnumMessageSourceResolvable;
import quarano.department.TrackedCase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.MvcLink;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
public interface ExternalTrackedCaseRepresentations {

	TrackedCaseSummary toSummary(TrackedCase trackedCase);

	TrackedCaseSelect toSelect(TrackedCase trackedCase);

	/**
	 * Trimmed down representation to be used from selection dialogues that basically need a link to a case by the
	 * person's name.
	 *
	 * @author Oliver Drotbohm
	 */
	@Relation(collectionRelation = "cases")
	@RequiredArgsConstructor(staticName = "of")
	public class TrackedCaseSelect extends RepresentationModel<TrackedCaseSelect> {

		private final TrackedCase trackedCase;

		public String getFirstName() {
			return trackedCase.getTrackedPerson().getFirstName();
		}

		public String getLastName() {
			return trackedCase.getTrackedPerson().getLastName();
		}

		public LocalDate getDateOfBirth() {
			return trackedCase.getTrackedPerson().getDateOfBirth();
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.hateoas.RepresentationModel#getLinks()
		 */
		@Override
		public Links getLinks() {

			var id = trackedCase.getId();
			var department = trackedCase.getDepartment();

			return super.getLinks()
					.and(MvcLink.of(on(TrackedCaseController.class).getCase(id, department), IanaLinkRelations.SELF));
		}
	}

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

		public LocalDate getDateOfBirth() {
			return trackedCase.getTrackedPerson().getDateOfBirth();
		}

		public String getEmail() {

			var trackedPerson = trackedCase.getTrackedPerson();
			var emailAddress = trackedPerson.getEmailAddress();

			return emailAddress == null ? null : emailAddress.toString();
		}

		public String getCaseType() {
			return trackedCase.getType().getPrimaryCaseType().name().toLowerCase(Locale.US);
		}

		public Boolean isMedicalStaff() {

			var questionnaire = trackedCase.getQuestionnaire();

			return questionnaire == null ? null : questionnaire.isMedicalStaff();
		}

		public String getCaseTypeLabel() {
			return messages.getMessage(EnumMessageSourceResolvable.of(trackedCase.getType()));
		}

		public LocalDate getCreatedAt() {
			return trackedCase.getMetadata().getCreated().toLocalDate();
		}

		@Nullable
		public Map<String, Object> getQuarantine() {

			var quarantine = trackedCase.getQuarantine();

			if (quarantine == null) {
				return null;
			}

			return Map.of("from", quarantine.getFrom().format(DateTimeFormatter.ISO_DATE),
					"to", quarantine.getTo().format(DateTimeFormatter.ISO_DATE));
		}
	}
}
