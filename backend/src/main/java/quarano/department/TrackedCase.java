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
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import quarano.core.QuaranoAggregate;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.tracking.TrackedPerson;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.jddd.core.types.Identifier;

/**
 * @author Oliver Drotbohm
 */
@Entity
@Data
@Setter(AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true, of = {})
@Accessors(chain = true)
public class TrackedCase extends QuaranoAggregate<TrackedCase, TrackedCaseIdentifier> {

	private @OneToOne TrackedPerson trackedPerson;
	private @ManyToOne Department department;
	private @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) InitialReport initialReport;
	private Enrollment enrollment = new Enrollment();

	TrackedCase() {
		this.id = TrackedCaseIdentifier.of(UUID.randomUUID());
	}

	public TrackedCase(TrackedPerson person, Department department) {

		this.id = TrackedCaseIdentifier.of(UUID.randomUUID());
		this.trackedPerson = person;
		this.department = department;
	}

	public InitialReport getOrCreateInitialReport() {
		return initialReport == null ? new InitialReport() : initialReport;
	}

	public TrackedCase markEnrollmentDetailsSubmitted() {

		this.enrollment.markDetailsSubmitted();

		return this;
	}

	public TrackedCase submitQuestionnaire(InitialReport report) {

		this.initialReport = report;

		if (report.isComplete()) {
			enrollment.markQuestionaireSubmitted();
		}

		return this;
	}

	public TrackedCase markEnrollmentCompleted(EnrollmentCompletion completion) {

		if (!completion.verify(trackedPerson.getEncounters())) {
			throw new EnrollmentException("No encounters registered so far! Explicit acknowledgement needed!");
		}

		this.enrollment.markEnrollmentCompleted();

		return this;
	}

	public TrackedCase reopenEnrollment() {

		this.enrollment.reopenEnrollment();

		return this;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class TrackedCaseIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = -1255657328932035265L;
		final UUID id;
	}
}
