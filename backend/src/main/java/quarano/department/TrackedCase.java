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
package quarano.department;

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
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.tracking.ContactPerson;
import quarano.tracking.Quarantine;
import quarano.tracking.TrackedPerson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jddd.core.types.Identifier;
import org.jddd.event.types.DomainEvent;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 */
@Entity
@Table(name = "tracked_cases")
@Data
@Setter(AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true, of = {})
@Slf4j
public class TrackedCase extends QuaranoAggregate<TrackedCase, TrackedCaseIdentifier> {

	@OneToOne(cascade = { CascadeType.ALL }) //
	@JoinColumn(name = "tracked_person_id") //
	private TrackedPerson trackedPerson;

	@ManyToOne @JoinColumn(name = "department_id", nullable = false) //
	private Department department;

	private @Getter TestResult testResult;

	@Setter(AccessLevel.NONE) //
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) //
	@JoinColumn(name = "initial_report_id") //
	private InitialReport initialReport;

	@Setter(AccessLevel.NONE) //
	private Enrollment enrollment = new Enrollment();
	private @Column(name = "case_type") @Getter @Setter CaseType type = CaseType.INDEX;
	private @Getter @Setter Quarantine quarantine = null;

	@OneToMany(cascade = { CascadeType.ALL }) //
	private @Getter List<ContactPerson> originContacts = new ArrayList<>();

	@OneToMany(cascade = { CascadeType.ALL }) //
	@JoinColumn(name = "tracked_case_id") //
	private @Getter List<Comment> comments = new ArrayList<>();

	@Column(nullable = false) //
	private @Getter Status status;

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

	TrackedCase(TrackedCaseIdentifier id, TrackedPerson person, CaseType type, Department department,
			@Nullable ContactPerson originContact) {

		this.id = id;
		this.trackedPerson = person;
		this.type = type;
		this.department = department;
		this.status = Status.OPEN;

		this.registerEvent(CaseCreated.of(this));

		if (originContact != null) {
			this.originContacts.add(originContact);
		}

		if (person.hasAccount()) {
			markInRegistration();
			markRegistrationCompleted();
		}
	}

	public TrackedCase addComment(Comment comment) {

		Assert.notNull(comment, "Comment must not be null!");

		this.comments.add(comment);

		return this;
	}

	public void addOriginContact(ContactPerson contact) {

		if (!originContacts.contains(contact)) {
			this.originContacts.add(contact);
		}
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

	public boolean isInQuarantine() {
		return quarantine != null;
	}

	public boolean isInitialCallNeeded() {
		return status.equals(Status.OPEN) && trackedPerson.getDateOfBirth() != null
				&& trackedPerson.getEmailAddress() != null
				&& (trackedPerson.getPhoneNumber() != null || trackedPerson.getMobilePhoneNumber() != null);
	}

	public boolean isInvestigationNeeded() {
		return !isConcluded() && (trackedPerson.getPhoneNumber() == null && trackedPerson.getMobilePhoneNumber() == null
				|| trackedPerson.getEmailAddress() == null //
				|| trackedPerson.getDateOfBirth() == null);
	}

	public InitialReport getOrCreateInitialReport() {
		return initialReport == null ? new InitialReport() : initialReport;
	}

	public TrackedCase submitEnrollmentDetails() {

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

	public TrackedCase submitQuestionnaire(InitialReport report) {

		this.initialReport = report;
		log.debug("Submitting initial report {}.", report);

		if (report.isComplete()) {
			enrollment.markQuestionaireSubmitted();
		} else {
			log.debug("Questionnaire incomplete! Enrollment step not completed.");
		}

		return this;
	}

	public TrackedCase report(TestResult testResult) {

		this.testResult = testResult;
		this.type = CaseType.INDEX;

		return this;
	}

	public TrackedCase markEdited() {

		registerEvent(CaseUpdated.of(this));

		return this;
	}

	public TrackedCase markEnrollmentCompleted(EnrollmentCompletion completion) {

		if (!completion.verify(trackedPerson.getEncounters())) {
			throw new EnrollmentException("No encounters registered so far! Explicit acknowledgement needed!");
		}

		assertStatus(Status.REGISTERED, "Cannot mark enrollment completion for case %s in status %s!", id, status);

		this.enrollment.markEnrollmentCompleted();
		this.status = Status.TRACKING;

		return this;
	}

	public TrackedCase reopenEnrollment() {

		this.enrollment.reopenEnrollment();
		this.status = Status.REGISTERED;

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

	public boolean isEnrollmentCompleted() {
		return getEnrollment().isComplete();
	}

	public boolean hasTestResult() {
		return testResult != null;
	}

	/**
	 * @return
	 */
	TrackedCase markInRegistration() {

		assertStatus(Status.OPEN, "Cannot start registration for case %s in status %s!", id, status);

		this.status = Status.IN_REGISTRATION;

		return this;
	}

	TrackedCase markRegistrationCompleted() {

		assertStatus(Status.IN_REGISTRATION, "Cannot complete registration for case %s in status %s!", id, status);

		this.status = Status.REGISTERED;

		return this;
	}

	private static void assertStatus(Status status, String message, Object... args) {

		if (!status.equals(status)) {
			throw new IllegalStateException(String.format(message, args));
		}
	}

	public static enum Status {

		OPEN,

		IN_REGISTRATION,

		REGISTERED,

		TRACKING,

		CONCLUDED;
	}

	@Value(staticConstructor = "of")
	public static class CaseCreated implements DomainEvent {
		TrackedCase trackedCase;
	}

	@Value(staticConstructor = "of")
	public static class CaseUpdated implements DomainEvent {
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
}
