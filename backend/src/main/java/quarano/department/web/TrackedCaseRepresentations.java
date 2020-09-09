package quarano.department.web;

import static org.springframework.hateoas.IanaLinkRelations.NEXT;
import static org.springframework.hateoas.IanaLinkRelations.SELF;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;
import static quarano.department.web.TrackedCaseLinkRelations.*;

import lombok.*;
import quarano.account.Account;
import quarano.account.Department;
import quarano.core.PhoneNumber;
import quarano.core.validation.Email;
import quarano.core.validation.Strings;
import quarano.core.validation.Textual;
import quarano.core.web.MapperWrapper;
import quarano.department.*;
import quarano.department.Questionnaire.SymptomInformation;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.diary.DiaryEntry;
import quarano.reference.Language;
import quarano.reference.SymptomRepository;
import quarano.tracking.ContactPerson;
import quarano.tracking.TrackedPerson;
import quarano.tracking.ZipCode;
import quarano.tracking.web.TrackedPersonDto;
import quarano.tracking.web.TrackingController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.Links.MergeMode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.MvcLink;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
@Component
@RequiredArgsConstructor
public class TrackedCaseRepresentations implements ExternalTrackedCaseRepresentations {

	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull MapperWrapper mapper;
	private final @NonNull SmartValidator validator;
	private final @NonNull MessageSourceAccessor messages;
	private final @NonNull SymptomRepository symptoms;
	private final @NonNull ContactChaser contactChaser;

	private final @NonNull List<TrackedCaseDetailsEnricher> enrichers;
	/*
	 * (non-Javadoc)
	 * @see quarano.department.web.ExternalTrackedCaseRepresentations#toSummary(quarano.department.TrackedCase)
	 */
	@Override
	public TrackedCaseSummary toSummary(TrackedCase trackedCase) {
		return new TrackedCaseSummary(trackedCase, messages);
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.department.web.ExternalTrackedCaseRepresentations#toSelect(quarano.department.TrackedCase)
	 */
	@Override
	public TrackedCaseSelect toSelect(TrackedCase trackedCase) {
		return TrackedCaseSelect.of(trackedCase);
	}

	public RepresentationModel<?> toSummaryWithOriginCases(TrackedCase trackedCase) {

		var trackedCaseSummary = toSummary(trackedCase);
		var halModelBuilder = HalModelBuilder.halModelOf(trackedCaseSummary);
		var originCases = trackedCase.getOriginCases().stream()
				.map(this::toSelect)
				.collect(Collectors.toUnmodifiableList());

		if (!originCases.isEmpty()) {
			halModelBuilder.embed(originCases, TrackedCaseLinkRelations.ORIGIN_CASES);
		}

		return halModelBuilder.build();
	}

	public EnrollmentDto toRepresentation(Enrollment enrollment) {
		return new EnrollmentDto(enrollment);
	}

	String resolve(String source) {
		return messages.getMessage(source);
	}

	TrackedCaseDto toInputRepresentation(TrackedCase trackedCase) {
		return toDtoRepresentation(trackedCase, TrackedCaseDto.Input.class);
	}

	RepresentationModel<?> toRepresentation(TrackedCase trackedCase) {

		var dto = toDtoRepresentation(trackedCase, TrackedCaseDto.Output.class);
		var contactToIndexCases = contactChaser.findIndexContactsFor(trackedCase)
				.map(Contact::new)
				.collect(Collectors.toList());

		var details = new TrackedCaseDetails(trackedCase, dto, messages, contactToIndexCases);
		var enriched = enrichers.stream()
				.reduce(details, (it, enricher) -> enricher.enrich(it), (l, r) -> r);

		var originCases = trackedCase.getOriginCases().stream()
				.map(it -> toSelect(it));

		return HalModelBuilder.halModelOf(enriched)
				.embed(originCases, TrackedCaseLinkRelations.ORIGIN_CASES)
				.build();
	}

	TrackedCaseContactSummary toContactSummary(ContactPerson contactPerson, List<LocalDate> contactDates) {

		var contactTrackedCase = cases.findByOriginContacts(contactPerson);
		return new TrackedCaseContactSummary(contactPerson, contactDates, contactTrackedCase, messages);
	}

	public TrackedCaseDiaryEntrySummary toDiaryEntrySummary(DiaryEntry diaryEntry, Language lang) {
		return new TrackedCaseDiaryEntrySummary(diaryEntry, mapper, lang);
	}

	QuestionnaireDto toRepresentation(Questionnaire report) {
		return mapper.map(report, QuestionnaireDto.class);
	}

	TrackedCase from(TrackedCaseDto source, TrackedCase existing, Errors errors) {

		validateForUpdate(source, existing, errors);

		if (errors.hasErrors()) {
			return existing;
		}

		var personSource = mapper.map(source, TrackedPersonDto.class);
		mapper.map(personSource, existing.getTrackedPerson());

		var result = mapper.map(source, existing);

		return mapper.map(source, result).markEdited();
	}

	TrackedCase from(TrackedCaseDto source, Department department, CaseType type, Errors errors) {

		validateForCreation(source, type, errors);

		var personDto = mapper.map(source, TrackedPersonDto.class);
		var person = mapper.map(personDto, TrackedPerson.class);

		return mapper.map(source, new TrackedCase(person, type, department));
	}

	Questionnaire from(QuestionnaireDto source, Language lang) {

		var report = createQuestionnaireFrom(source);
		return source.applyTo(mapper.map(source, report), symptoms, lang);
	}

	Questionnaire from(QuestionnaireDto source, TrackedCase trackedCase, Language lang) {

		return trackedCase.getQuestionnaire() == null
				? from(source, lang)
				: from(source, trackedCase.getQuestionnaire(), lang);
	}

	Questionnaire from(QuestionnaireDto source, @Nullable Questionnaire existing, Language lang) {

		var mapped = existing == null
				? from(source, lang)
				: mapper.map(source, existing);

		return source.applyTo(mapped, symptoms, lang);
	}

	Comment from(CommentInput payload, Account account) {
		return new Comment(payload.getComment(), account.getFullName());
	}

	private TrackedCaseDto toDtoRepresentation(TrackedCase trackedCase, Class<? extends TrackedCaseDto> type) {

		var personDto = mapper.map(trackedCase.getTrackedPerson(), TrackedPersonDto.class);
		var caseDto = mapper.map(trackedCase, type);

		return mapper.map(personDto, caseDto);
	}

	private Questionnaire createQuestionnaireFrom(QuestionnaireDto source) {

		SymptomInformation symptomsInfo;

		if (source.getHasSymptoms()) {

			var result = source.getSymptoms().stream()
					.map(it -> symptoms.findById(it).get())
					.collect(Collectors.toList());

			symptomsInfo = SymptomInformation.withSymptomsSince(source.getDayOfFirstSymptoms(), result);

		} else {
			symptomsInfo = SymptomInformation.withoutSymptoms();
		}

		return new Questionnaire(symptomsInfo, source.getHasPreExistingConditionsDescription(),
				source.getBelongToMedicalStaffDescription());
	}

	private Errors validateForUpdate(TrackedCaseDto payload, TrackedCase existing, Errors errors) {

		if (existing.isEnrollmentCompleted()) {
			validateAfterEnrollment(payload, errors);
		}

		return validate(payload, existing.getType(), errors);
	}

	private Errors validateForCreation(TrackedCaseDto payload, CaseType type, Errors errors) {

		if (type.equals(CaseType.CONTACT) && payload.getTestDate() != null && payload.isInfected()) {

			Stream.of("infected", "testDate")
					.forEach(it -> errors.rejectValue(it, "ContactCase.infected"));
		}

		return validate(payload, type, errors);
	}

	private Errors validate(TrackedCaseDto payload, CaseType type, Errors errors) {

		if (payload.getTestDate() == null && payload.isInfected()) {
			errors.rejectValue("testDate", "ContactCase.infected");
		}

		var validationGroups = new ArrayList<>();
		validationGroups.add(Default.class);

		if (type.equals(CaseType.INDEX) || payload.getTestDate() != null) {
			validationGroups.add(ValidationGroups.Index.class);
		}

		validator.validate(payload, errors, validationGroups.toArray());
		payload.validate(errors, type);

		return errors;
	}

	private void validateAfterEnrollment(TrackedCaseDto source, Errors errors) {

		TrackedPersonDto dto = mapper.map(source, TrackedPersonDto.class);

		validator.validate(dto, errors);
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@Validated({ Default.class, ValidationGroups.Index.class })
	@interface ValidatedIndexCase {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@Validated
	@interface ValidatedContactCase {}

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
		private Locale locale;

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

		@Data
		static class Input extends TrackedCaseDto {
			private List<URI> originCases = new ArrayList<>();
		}

		static class Output extends TrackedCaseDto {}
	}

	@Relation(collectionRelation = "cases")
	public static class TrackedCaseDetails extends TrackedCaseStatusAware<TrackedCaseDetails> {

		private final @Getter(onMethod = @__(@JsonIgnore)) TrackedCase trackedCase;
		private final TrackedCaseSummary summary;
		private final @Getter(onMethod = @__(@JsonUnwrapped)) TrackedCaseDto dto;
		private final @Getter List<Contact> indexContacts;
		private final @Getter long contactCount;
		private final @Getter(onMethod = @__(@JsonAnyGetter)) Map<String, Object> additionalProperty;

		public TrackedCaseDetails(TrackedCase trackedCase, TrackedCaseDto dto, MessageSourceAccessor messages,
				List<Contact> indexContacts) {

			super(trackedCase, messages);

			this.trackedCase = trackedCase;
			this.dto = dto;
			this.contactCount = trackedCase.getTrackedPerson().getEncounters().getNumberOfUniqueContacts();
			this.summary = new TrackedCaseSummary(trackedCase, messages);
			this.indexContacts = indexContacts;
			this.additionalProperty = new HashMap<>();
		}

		public String getCaseId() {
			return trackedCase.getId().toString();
		}

		public String getCaseTypeLabel() {
			return summary.getCaseTypeLabel();
		}

		public List<CommentRepresentation> getComments() {

			return getTrackedCase().getComments().stream()
					.sorted(Comparator.comparing(Comment::getDate).reversed())
					.map(CommentRepresentation::new)
					.collect(Collectors.toUnmodifiableList());
		}

		/**
		 * Registers the given function to create additional properties of the representation.
		 *
		 * @param property must not be {@literal null} or empty.
		 * @param function must not be {@literal null}.
		 * @return
		 */
		public TrackedCaseDetails withAdditionalProperty(String property, Function<TrackedCase, Object> function) {

			Assert.notNull(property, "Property must not be null or empty!");
			Assert.notNull(function, "Function must not be null!");

			this.additionalProperty.put(property, function.apply(trackedCase));

			return this;
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

	@RequiredArgsConstructor
	public static class EnrollmentDto extends RepresentationModel<EnrollmentDto> {

		private final @Getter(onMethod = @__(@JsonUnwrapped)) Enrollment enrollment;

		@Override
		@SuppressWarnings("null")
		public Links getLinks() {

			var caseController = on(TrackedCaseController.class);
			var trackingController = on(TrackingController.class);

			var enrollmentLink = MvcLink.of(caseController.enrollment(null), SELF);
			var questionnareLink = MvcLink.of(caseController.addQuestionaire(null, null, null), QUESTIONNAIRE);
			var reopenLink = MvcLink.of(caseController.reopenEnrollment(null), REOPEN);
			var detailsLink = MvcLink.of(trackingController.enrollmentOverview(null), DETAILS);
			var encountersLink = MvcLink.of(trackingController.getEncounters(null), ENCOUNTERS);

			return Links.NONE.and(enrollmentLink, detailsLink)
					.andIf(enrollment.isComplete(), questionnareLink, encountersLink, reopenLink)
					.andIf(enrollment.isCompletedQuestionnaire(), questionnareLink, encountersLink, encountersLink.withRel(NEXT))
					.andIf(enrollment.isCompletedPersonalData(), questionnareLink, questionnareLink.withRel(NEXT))
					.merge(MergeMode.SKIP_BY_REL, detailsLink.withRel(NEXT));
		}
	}
}
