package quarano.department;

import io.jsonwebtoken.lang.Objects;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import quarano.account.Department;
import quarano.account.Department.DepartmentIdentifier;
import quarano.core.QuaranoAggregate;
import quarano.department.Questionnaire.SymptomInformation;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.tracking.ContactPerson;
import quarano.tracking.Quarantine;
import quarano.tracking.TrackedPerson;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.*;

import org.apache.commons.lang3.ObjectUtils;
import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.event.types.DomainEvent;
import org.springframework.data.util.Streamable;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.querydsl.core.annotations.QueryInit;

/**
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 * @author Felix Schultze
 */
@Entity
@Table(name = "tracked_cases")
@Data
@Setter(AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true, of = {})
@Slf4j
public class TrackedCase extends QuaranoAggregate<TrackedCase, TrackedCaseIdentifier> {

	@QueryInit({ "*.*", "address.zipCode", "department.id" }) //
	@OneToOne(cascade = { CascadeType.ALL }) //
	@JoinColumn(name = "tracked_person_id") //
	private TrackedPerson trackedPerson;

	@QueryInit({ "*.*", "id" }) //
	@ManyToOne //
	@JoinColumn(name = "department_id", nullable = false) //
	private Department department;

	private @Getter TestResult testResult;

	@Setter(AccessLevel.NONE)
	@OneToOne(cascade = CascadeType.ALL,
			orphanRemoval = true)
	@JoinColumn(name = "questionnaire_id")
	private @Getter Questionnaire questionnaire;

	@Setter(AccessLevel.NONE)
	private Enrollment enrollment = new Enrollment();

	@Column(name = "case_type")
	@Enumerated(EnumType.STRING)
	private @Getter @Setter CaseType type = CaseType.INDEX;

	@Nullable //
	private Quarantine quarantine = null;

	@Nullable
	private @Getter @Setter String sormasUuid = null;

	@Nullable
	@Setter(AccessLevel.NONE) //
	@Column(name = "quarantine_last_modified") //
	private LocalDateTime quarantineLastModified = null;

	private @Getter @Setter String extReferenceNumber;

	/**
	 * @since 1.4
	 */
	private @Getter @Setter SormasCaseIdentifier sormasCaseId;

	@OneToMany(cascade = { CascadeType.ALL })
	private @Getter List<ContactPerson> originContacts = new ArrayList<>();

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(
			name = "tracked_case_id")
	private @Getter List<Comment> comments = new ArrayList<>();

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private @Getter Status status;

	@OneToMany
	private @Getter List<TrackedCase> originCases = new ArrayList<>();

	public static TrackedCase of(CaseSource source) {

		var contactPerson = source.getPerson();
		var person = new TrackedPerson(contactPerson);
		var caseType = CaseType.CONTACT;

		// CORE-185 medical staff overwrites the others
		if (Boolean.TRUE.equals(contactPerson.getIsHealthStaff())) {
			caseType = CaseType.CONTACT_MEDICAL;
		} else if (contactPerson.isVulnerable()) {
			caseType = CaseType.CONTACT_VULNERABLE;
		}

		var originCase = source.getTrackedCase();

		return new TrackedCase(person, caseType, originCase.getDepartment(), contactPerson)
				.addOriginCase(originCase);
	}

	@SuppressWarnings("unused")
	private TrackedCase() {
		this.id = TrackedCaseIdentifier.of(UUID.randomUUID());
	}

	public TrackedCase(TrackedPerson person, CaseType type, Department department) {
		this(TrackedCaseIdentifier.of(UUID.randomUUID()), person, type, department, null);
	}

	public TrackedCase(TrackedPerson person, CaseType type, Department department,
			@Nullable ContactPerson contactPerson) {
		this(TrackedCaseIdentifier.of(UUID.randomUUID()), person, type, department, contactPerson);
	}

	public TrackedCase(TrackedCaseIdentifier id, TrackedPerson person, CaseType type, Department department,
			@Nullable ContactPerson originContact) {

		this.id = id;
		this.trackedPerson = person;
		this.type = type;
		this.department = department;
		this.status = person.hasAccount() ? Status.REGISTERED : Status.OPEN;
		this.originCases = new ArrayList<>();

		this.registerEvent(CaseCreated.of(id));

		if (originContact != null) {
			this.originContacts.add(originContact);
		}
	}

	public TrackedCase addComment(Comment comment) {

		Assert.notNull(comment, "Comment must not be null!");

		this.comments.add(comment);

		return this;
	}

	public TrackedCase addOriginCase(TrackedCase trackedCase) {

		Assert.state(trackedCase.isIndexCase(), () -> String.format("Case %s is not an index case!", trackedCase.getId()));

		this.originCases.add(trackedCase);

		return this;
	}

	public TrackedCase setQuarantine(Quarantine quarantine) {

		if (Objects.nullSafeEquals(this.quarantine, quarantine)) {
			return this;
		}

		this.quarantineLastModified = LocalDateTime.now();
		this.quarantine = quarantine;

		return this;
	}

	/**
	 * Returns whether the current {@link TrackedCase} originates from the given one.
	 *
	 * @param other must not be {@literal null}.
	 * @return
	 */
	public boolean originatesFrom(TrackedCase other) {

		Assert.notNull(other, "Other TrackedCase must not be null!");

		return originCases.contains(other);
	}

	public boolean isEligibleForTracking() {
		return status.equals(Status.OPEN) && trackedPerson.isEligibleForTracking();
	}

	public boolean isIndexCase() {
		return type.equals(CaseType.INDEX);
	}

	public boolean isContactCase() {
		return type.equals(CaseType.CONTACT);
	}

	public boolean isMedicalContactCase() {
		return type.equals(CaseType.CONTACT_MEDICAL);
	}

	public boolean isVulnerableContactCase() {
		return type.equals(CaseType.CONTACT_VULNERABLE);
	}

	public boolean isInQuarantine() {
		return quarantine != null && !quarantine.isOver();
	}

	public boolean isInitialCallNeeded() {
		return status.equals(Status.OPEN) && trackedPerson.getDateOfBirth() != null
				&& trackedPerson.getEmailAddress() != null
				&& (trackedPerson.getPhoneNumber() != null || trackedPerson.getMobilePhoneNumber() != null);
	}

	public boolean isInvestigationNeeded() {
		return !isConcluded() && (trackedPerson.getPhoneNumber() == null && trackedPerson.getMobilePhoneNumber() == null
				|| trackedPerson.getEmailAddress() == null
				|| trackedPerson.getDateOfBirth() == null);
	}

	public TrackedCase submitEnrollmentDetails() {

		assertStatus(Status.REGISTERED, "Cannot submit enrollment details for case %s in state %s!", id, status);

		if (trackedPerson.isDetailsCompleted()) {
			this.enrollment.markDetailsSubmitted();
		}

		return this;
	}

	public TrackedCase conclude() {

		this.status = Status.CONCLUDED;

		registerEvent(CaseConcluded.of(id));

		return this;
	}

	public TrackedCase submitQuestionnaire(Questionnaire questionnaire) {

		assertStatus(Status.REGISTERED, "Cannot submit questionnaire for case %s in state %s!", id, status);

		this.questionnaire = questionnaire;
		log.debug("Submitting questionnaire {}.", questionnaire);

		enrollment.markQuestionaireSubmitted();

		return this;
	}

	public TrackedCase report(TestResult testResult, boolean executeContactRetroForContactCases) {
		 
 		if (this.type != CaseType.INDEX && testResult.isInfected()) {
 			registerEvent(CaseConvertedToIndex.of(this));

			if (!executeContactRetroForContactCases) {
				reopenEnrollment();
			}
 		}

		this.testResult = testResult;
		this.type = testResult.isInfected() ? CaseType.INDEX : type;

		return this;
	}

	public TrackedCase markEdited() {

		registerEvent(CaseUpdated.of(id));

		return this;
	}

	public TrackedCase markEnrollmentCompleted(EnrollmentCompletion completion) {

		if (!completion.verify(trackedPerson.getEncounters())) {
			throw new EnrollmentException("No encounters registered so far! Explicit acknowledgement needed!");
		}

		assertStatus(Status.REGISTERED, "Cannot mark enrollment completion for case %s in status %s!", id, status);

		this.enrollment.markEnrollmentCompleted();
		this.status = Status.TRACKING;

		this.registerEvent(CaseStatusUpdated.of(id));

		return this;
	}

	public TrackedCase reopenEnrollment() {

		this.enrollment.reopenEnrollment();
		this.status = Status.REGISTERED;

		this.registerEvent(CaseStatusUpdated.of(id));

		return this;
	}

	public TrackedCase markAsExternalZip() {

		this.status = Status.EXTERNAL_ZIP;

		this.registerEvent(CaseStatusUpdated.of(id));

		return this;
	}

	public boolean belongsTo(Department department) {
		return this.department.equals(department);
	}

	public boolean belongsTo(DepartmentIdentifier id) {
		return this.department.hasId(id);
	}

	public boolean isConcluded() {
		return status.equals(Status.CONCLUDED);
	}

	public boolean isOpen() {
		return !isConcluded();
	}

	public boolean isTracking() {
		return status.equals(Status.TRACKING);
	}

	public boolean isExternal() {
		return status.equals(Status.EXTERNAL_ZIP);
	}

	public boolean isInRegistration() {
		return status.equals(Status.IN_REGISTRATION);
	}

	public boolean isEnrollmentCompleted() {
		return getEnrollment().isComplete();
	}

	public boolean hasTestResult() {
		return testResult != null;
	}

	public boolean hasQuestionnaire() {
		return questionnaire != null;
	}

	/**
	 * @since 1.4
	 */
	public boolean isEmailAvailable() {
		return trackedPerson.getEmailAddress() != null;
	}

	TrackedCase markAsManuallyTracked() {

		assertStatus(Status.OPEN, "Cannot mark case %s as tracked manually as it is in status %s!", id, status);

		this.status = Status.TRACKED_MANUALLY;
		this.registerEvent(CaseStatusUpdated.of(id));

		return this;
	}

	/**
	 * Marks the current {@link TrackedCase} to be in registration.
	 *
	 * @return
	 */
	TrackedCase markInRegistration() {

		if(this.getStatus() != Status.TRACKING && this.getStatus() != Status.REGISTERED){
			assertStatus(Streamable.of(Status.OPEN, Status.IN_REGISTRATION),
					"Cannot start registration for case %s in status %s!", id, status);

			this.status = Status.IN_REGISTRATION;
		}

		this.registerEvent(RegistrationInitiated.of(getId()));

		return this;
	}

	TrackedCase markRegistrationCanceled() {

		if (this.getStatus() != Status.OPEN) {

			assertStatus(Status.IN_REGISTRATION, "Cannot cancel registration for case %s in status %s!", id, status);

			this.status = Status.OPEN;

			this.registerEvent(CaseStatusUpdated.of(id));
		}

		return this;
	}

	TrackedCase markRegistrationCompleted() {
		//CORE-455 in case a account was deleted it kept its old status wich could be tracking or registered
		if (this.getStatus() != Status.TRACKING && this.getStatus() != Status.REGISTERED) {

			assertStatus(Status.IN_REGISTRATION, "Cannot complete registration for case %s in status %s!", id, status);

			this.status = Status.REGISTERED;

			this.registerEvent(CaseStatusUpdated.of(id));
		}

		return this;
	}

	/**
	 * anonymized personal data
	 * 
	 * @return
	 * @since 1.4
	 */
	TrackedCase anonymize() {

		setExtReferenceNumber(null);

		getTrackedPerson().anonymize();
		getComments().forEach(Comment::anonymize);
		Optional.ofNullable(getQuestionnaire()).ifPresent(Questionnaire::anonymize);

		setStatus(Status.ANONYMIZED);

		return this;
	}

	/**
	 * @since 1.4
	 */
	TrackedCase fillSampleData() {

		extReferenceNumber = ObjectUtils.defaultIfNull(extReferenceNumber, "extReferenceNumber");

		trackedPerson.fillSampleData();
		comments.forEach(Comment::fillSampleData);
		comments.add(new Comment("text", "author"));

		Optional.ofNullable(getQuestionnaire())
				.orElse(new Questionnaire(SymptomInformation.withoutSymptoms(), null, null))
				.fillSampleData();

		return this;
	}

	private void assertStatus(Status status, String message, Object... args) {
		assertStatus(Streamable.of(status), message, args);
	}

	private void assertStatus(Streamable<Status> statuses, String message, Object... args) {

		if (!statuses.stream().anyMatch(status::equals)) {
			throw new IllegalStateException(String.format(message, args));
		}
	}

	public enum Status {

		OPEN,

		TRACKED_MANUALLY,

		IN_REGISTRATION,

		REGISTERED,

		TRACKING,

		CONCLUDED,

		EXTERNAL_ZIP,

		ANONYMIZED;
	}

	@Value(staticConstructor = "of")
	public static class CaseCreated implements DomainEvent {
		TrackedCaseIdentifier trackedCase;
	}

	@Value(staticConstructor = "of")
	public static class CaseUpdated implements DomainEvent {
		TrackedCaseIdentifier trackedCase;
	}

	@Value(staticConstructor = "of")
	public static class CaseStatusUpdated implements DomainEvent {
		TrackedCaseIdentifier trackedCase;
	}

	@Value(staticConstructor = "of")
	static class RegistrationInitiated implements DomainEvent {
		TrackedCaseIdentifier caseIdentifier;
	}

	@Value(staticConstructor = "of")
	public static class CaseConvertedToIndex implements DomainEvent {
		TrackedCase trackedCase;
	}

	@Value(staticConstructor = "of")
	public static class CaseConcluded {
		TrackedCaseIdentifier caseIdentifier;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class TrackedCaseIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -1255657328932035265L;

		final UUID trackedCaseId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return trackedCaseId.toString();
		}
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class SormasCaseIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = 5512222218561608908L;

		final String sormasCaseId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return sormasCaseId;
		}
	}
}
