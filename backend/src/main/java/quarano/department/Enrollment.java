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

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;

import org.jddd.core.types.Identifier;

/**
 * @author Oliver Drotbohm
 */
@Data
@Setter(AccessLevel.NONE)
@Embeddable
@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
public class Enrollment {

	private boolean completedPersonalData;
	private boolean completedQuestionnaire;
	private boolean completedContactRetro;

	public Enrollment markDetailsSubmitted() {

		this.completedPersonalData = true;

		return this;
	}

	public Enrollment markQuestionaireSubmitted() {

		if (!completedPersonalData) {
			throw new EnrollmentException("Cannot submit questionaire before personal details were submitted!");
		}

		this.completedQuestionnaire = true;

		return this;
	}

	/**
	 * @return
	 */
	public Enrollment markInitialContactsSubmitted() {

		if (!completedQuestionnaire) {
			throw new EnrollmentException("Cannot mark contacts submitted before questionaire was submitted!");
		}

		this.completedContactRetro = true;

		return this;
	}

	public boolean isComplete() {
		return completedPersonalData && completedQuestionnaire && completedContactRetro;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class EnrollmentIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = 7871473225101042167L;
		final UUID id;
	}

}
