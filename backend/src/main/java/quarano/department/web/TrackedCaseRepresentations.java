package quarano.department.web;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.Department;
import quarano.core.PhoneNumber;
import quarano.core.validation.Email;
import quarano.core.validation.Strings;
import quarano.core.validation.Textual;
import quarano.core.web.ErrorsDto;
import quarano.core.web.MapperWrapper;
import quarano.department.CaseType;
import quarano.department.Comment;
import quarano.department.ContactChaser;
import quarano.department.Questionnaire;
import quarano.department.Questionnaire.SymptomInformation;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.reference.SymptomRepository;
import quarano.tracking.ContactPerson;
import quarano.tracking.TrackedPerson;
import quarano.tracking.ZipCode;
import quarano.tracking.web.TrackedPersonDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
@Component
@RequiredArgsConstructor
class TrackedCaseRepresentations implements ExternalTrackedCaseRepresentations {

	private final @NonNull TrackedCaseRepository cases;
	private final MapperWrapper mapper;
	private final SmartValidator validator;
	private final MessageSourceAccessor messages;
	private final @NonNull SymptomRepository symptoms;
	private final ContactChaser contactChaser;

	TrackedCaseDto toInputRepresentation(TrackedCase trackedCase) {

		var personDto = mapper.map(trackedCase.getTrackedPerson(), TrackedPersonDto.class);
		var caseDto = mapper.map(trackedCase, TrackedCaseDto.class);

		return mapper.map(personDto, caseDto);
	}

	TrackedCaseDetails toRepresentation(TrackedCase trackedCase) {

		var dto = toInputRepresentation(trackedCase);

		List<Contact> contactToIndexCases = contactChaser.findIndexContactsFor(trackedCase) //
				.map(Contact::new) //
				.collect(Collectors.toList());

		var contactCount = trackedCase.getTrackedPerson().getEncounters().stream().count();
		
		return new TrackedCaseDetails(trackedCase, dto, messages, contactToIndexCases, contactCount);
	}

	public TrackedCaseSummary toSummary(TrackedCase trackedCase) {
		return new TrackedCaseSummary(trackedCase, messages);
	}
	
	public TrackedCaseContactSummary toContactSummary(ContactPerson contactPerson, List<LocalDate> contactDates) {
		
		var contactTrackedCase = cases.findByOriginContacts(contactPerson);
		return new TrackedCaseContactSummary(contactPerson, contactDates, contactTrackedCase, messages);
	}

	QuestionnaireDto toRepresentation(Questionnaire report) {
		return mapper.map(report, QuestionnaireDto.class);
	}

	TrackedCase from(TrackedCaseDto source, TrackedCase existing, ErrorsDto errors) {

		validateForUpdate(source, existing, errors);

		if (errors.hasErrors()) {
			return existing;
		}

		var personSource = mapper.map(source, TrackedPersonDto.class);
		mapper.map(personSource, existing.getTrackedPerson());

		var result = mapper.map(source, existing);

		return mapper.map(source, result).markEdited();
	}

	TrackedCase from(TrackedCaseDto source, Department department, CaseType type, ErrorsDto errors) {

		validateForCreation(source, type, errors);

		var personDto = mapper.map(source, TrackedPersonDto.class);
		var person = mapper.map(personDto, TrackedPerson.class);

		return mapper.map(source, new TrackedCase(person, type, department));
	}

	private Questionnaire createQuestionnaireFrom(QuestionnaireDto source) {

		SymptomInformation symptomsInfo;

		if (source.getHasSymptoms()) {

			var result = source.getSymptoms().stream() //
					.map(it -> symptoms.findById(it).get()) //
					.collect(Collectors.toList());

			symptomsInfo = SymptomInformation.withSymptomsSince(source.getDayOfFirstSymptoms(), result);

		} else {
			symptomsInfo = SymptomInformation.withoutSymptoms();
		}

		return new Questionnaire(symptomsInfo, source.getHasPreExistingConditionsDescription(),
				source.getBelongToMedicalStaffDescription());
	}

	Questionnaire from(QuestionnaireDto source) {

		var report = createQuestionnaireFrom(source);
		return source.applyTo(mapper.map(source, report), symptoms);

	}

	Questionnaire from(QuestionnaireDto source, Questionnaire existing) {
		return source.applyTo(mapper.map(source, existing), symptoms);
	}

	Comment from(CommentInput payload, Account account) {
		return new Comment(payload.getComment(), account.getFullName());
	}

	private ErrorsDto validateForUpdate(TrackedCaseDto payload, TrackedCase existing, ErrorsDto errors) {

		if (existing.isEnrollmentCompleted()) {
			errors = validateAfterEnrollment(payload, errors);
		}

		return validate(payload, existing.getType(), errors);
	}

	private ErrorsDto validateForCreation(TrackedCaseDto payload, CaseType type, ErrorsDto errors) {

		if (type.equals(CaseType.CONTACT)) {

			var positiveTestResult = payload.getTestDate() != null && payload.isInfected();

			Stream.of("infected", "testDate") //
					.forEach(it -> errors.rejectField(positiveTestResult, it, "ContactCase.infected"));

		}

		return validate(payload, type, errors);
	}

	private ErrorsDto validate(TrackedCaseDto payload, CaseType type, ErrorsDto errors) {

		if (payload.getTestDate() == null && payload.isInfected()) {
			errors.rejectField("testDate", "ContactCase.infected");
		}

		var validationGroups = new ArrayList<>();
		validationGroups.add(Default.class);

		if (type.equals(CaseType.INDEX) || payload.getTestDate() != null) {
			validationGroups.add(ValidationGroups.Index.class);
		}

		return errors //
				.doWith(it -> validator.validate(payload, it, validationGroups.toArray())) //
				.doWith(it -> payload.validate(it, type));
	}

	private ErrorsDto validateAfterEnrollment(TrackedCaseDto source, ErrorsDto errors) {

		TrackedPersonDto dto = mapper.map(source, TrackedPersonDto.class);

		return errors.doWith(it -> validator.validate(dto, it));
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@Validated({ Default.class, ValidationGroups.Index.class })
	@interface ValidatedIndexCase {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@Validated
	@interface ValidatedContactCase {
	}

	@Data
	@Getter(onMethod = @__(@Nullable))
	@NoArgsConstructor
	static class TrackedCaseDto {

		private @Pattern(regexp = Strings.NAMES) @NotEmpty String lastName;
		private @Pattern(regexp = Strings.NAMES) @NotEmpty String firstName;
		private @Pattern(regexp = Strings.EXT_REFERENCE_NUMBER) String extReferenceNumber;
		private @NotNull(groups = ValidationGroups.Index.class) LocalDate testDate;
		private @NotNull(groups = ValidationGroups.Index.class) LocalDate quarantineStartDate;
		private @NotNull(groups = ValidationGroups.Index.class) LocalDate quarantineEndDate;

		private @Pattern(regexp = Strings.STREET) String street;
		private @Pattern(regexp = Strings.HOUSE_NUMBER) String houseNumber;
		private @Pattern(regexp = Strings.CITY) String city;
		private @Pattern(regexp = ZipCode.PATTERN) String zipCode;
		private @Pattern(regexp = PhoneNumber.PATTERN) String mobilePhone;
		private @Pattern(regexp = PhoneNumber.PATTERN) String phone;
		private @Email String email;
		private @Past LocalDate dateOfBirth;
		private @Getter boolean infected;

		Errors validate(Errors errors, CaseType type) {

			verifyQuarantine(errors);

			if (type.equals(CaseType.CONTACT)) {
				return errors;
			}

			if (!StringUtils.hasText(phone) && !StringUtils.hasText(mobilePhone)) {
				errors.rejectValue("phone", "PhoneOrMobile");
				errors.rejectValue("mobilePhone", "PhoneOrMobile");
			}

			return errors;
		}

		void verifyQuarantine(Errors errors) {

			if (quarantineStartDate != null || quarantineEndDate != null) {

				if (quarantineStartDate == null) {
					errors.reject("quarantineStartDate", "NotNull");
					return;
				}

				if (quarantineEndDate == null) {
					errors.reject("quarantineEndDate", "NotNull");
					return;
				}

				if (quarantineStartDate.isAfter(quarantineEndDate)) {
					errors.rejectValue("quarantineEndDate", "EndBeforeStart");
				}
			}
		}
	}

	@Relation(collectionRelation = "cases")
	static class TrackedCaseDetails extends TrackedCaseStatusAware<TrackedCaseDetails> {

		private final TrackedCase trackedCase;
		private final TrackedCaseSummary summary;
		private final @Getter(onMethod = @__(@JsonUnwrapped)) TrackedCaseDto dto;
		private final @Getter List<Contact> indexContacts;
		private final @Getter long contactCount;

		public TrackedCaseDetails(TrackedCase trackedCase, TrackedCaseDto dto, MessageSourceAccessor messages,
				List<Contact> indexContacts, long contactCount) {

			super(trackedCase, messages);

			this.trackedCase = trackedCase;
			this.dto = dto;
			this.contactCount = contactCount;
			this.summary = new TrackedCaseSummary(trackedCase, messages);
			this.indexContacts = indexContacts;
		}

		public String getCaseId() {
			return trackedCase.getId().toString();
		}

		public String getCaseTypeLabel() {
			return summary.getCaseTypeLabel();
		}

		public List<CommentRepresentation> getComments() {

			return getTrackedCase().getComments().stream() //
					.sorted(Comparator.comparing(Comment::getDate).reversed()) //
					.map(CommentRepresentation::new) //
					.collect(Collectors.toUnmodifiableList());
		}
	}

	@RequiredArgsConstructor
	static class CommentRepresentation {

		private final Comment comment;

		public LocalDateTime getDate() {
			return comment.getDate();
		}

		public String getComment() {
			return comment.getText();
		}

		public String getAuthor() {
			return comment.getAuthor();
		}
	}

	@Getter
	static class Contact {

		private final TrackedCaseIdentifier caseId;
		private final String firstName;
		private final String lastName;
		private final LocalDate dateOfBirth;
		private final LocalDate contactAt;
		private final Boolean isHealthStaff;
		private final Boolean isSenior;
		private final Boolean hasPreExistingConditions;
		private final String identificationHint;

		private Contact(ContactChaser.Contact chasedContact) {

			this.caseId = chasedContact.getCaseId();

			var person = chasedContact.getPerson();
			this.firstName = person.getFirstName();
			this.lastName = person.getLastName();
			this.dateOfBirth = person.getDateOfBirth();

			this.contactAt = chasedContact.getContactAt();

			ContactPerson contactPerson = chasedContact.getContactPerson();
			this.isHealthStaff = contactPerson.getIsHealthStaff();
			this.isSenior = contactPerson.getIsSenior();
			this.hasPreExistingConditions = contactPerson.getHasPreExistingConditions();
			this.identificationHint = contactPerson.getIdentificationHint();
		}
	}

	@Data
	static class CommentInput {
		@Textual String comment;
	}

	static class ValidationGroups {

		interface Index {}

		interface Contact {}
	}
}
