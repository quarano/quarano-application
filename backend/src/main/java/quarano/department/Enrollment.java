package quarano.department;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;

import org.jddd.core.types.Identifier;

/**
 * @author Oliver Drotbohm
 */
@Slf4j
@Data
@Setter(AccessLevel.NONE)
@Embeddable
@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
public class Enrollment {

	private boolean completedPersonalData;
	private boolean completedQuestionnaire;
	private boolean completedContactRetro;

	Enrollment markDetailsSubmitted() {

		this.completedPersonalData = true;

		return this;
	}

	Enrollment markQuestionaireSubmitted() {

		if (!completedPersonalData) {
			throw new EnrollmentException("Cannot submit questionaire before personal details were submitted!");
		}

		log.debug("Marking questionnaire completed.");

		this.completedQuestionnaire = true;

		return this;
	}

	/**
	 * @return
	 */
	Enrollment markEnrollmentCompleted() {

		if (!completedQuestionnaire) {
			throw new EnrollmentException("Cannot mark contacts submitted before questionaire was submitted!");
		}

		this.completedContactRetro = true;

		return this;
	}

	Enrollment reopenEnrollment() {

		this.completedContactRetro = false;

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
