package quarano.department;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import quarano.core.QuaranoEntity;
import quarano.department.Questionnaire.QuestionnaireIdentifier;
import quarano.masterdata.Symptom;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.ObjectUtils;
import org.jmolecules.ddd.types.Identifier;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 * @author Patrick Otto
 */
@Entity
@Table(name = "questionnaires")
@Data
@EqualsAndHashCode(callSuper = true, of = {})
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Questionnaire extends QuaranoEntity<TrackedCase, QuestionnaireIdentifier> {

	private @Setter(value = AccessLevel.NONE) boolean hasSymptoms;
	private LocalDate dayOfFirstSymptoms;
	private @ManyToMany List<Symptom> symptoms;

	private String familyDoctor;
	private String guessedOriginOfInfection;
	private LocalDate guessedDateOfInfection;

	private @Setter(value = AccessLevel.NONE) boolean hasPreExistingConditions;
	private String hasPreExistingConditionsDescription;

	private @Setter(value = AccessLevel.NONE) boolean belongToMedicalStaff;
	private String belongToMedicalStaffDescription;

	private @Setter(value = AccessLevel.NONE) Boolean hasContactToVulnerablePeople;
	private String hasContactToVulnerablePeopleDescription;

	public Questionnaire(SymptomInformation symptomInformation,
			@Nullable String hasPreExistingConditionsDescription,
			@Nullable String belongToMedicalStaffDescription) {

		this.id = QuestionnaireIdentifier.of(UUID.randomUUID());
		this.dayOfFirstSymptoms = symptomInformation.dayOfFirstSymptoms;
		this.hasSymptoms = symptomInformation.hasSymptoms;
		this.symptoms = symptomInformation.symptoms;

		if (null != hasPreExistingConditionsDescription) {
			this.withPreExistingConditions(hasPreExistingConditionsDescription);
		}

		if (null != belongToMedicalStaffDescription) {
			this.withBelongToMedicalStaff(belongToMedicalStaffDescription);
		}
	}

	public Questionnaire withoutSymptoms() {

		this.hasSymptoms = false;
		this.dayOfFirstSymptoms = null;
		this.symptoms = null;

		return this;
	}

	public Questionnaire withFirstSymptomsAt(LocalDate date, List<Symptom> symptoms) {

		this.dayOfFirstSymptoms = date;
		this.hasSymptoms = true;
		this.symptoms = symptoms;

		return this;
	}

	public Questionnaire withoutPreExistingConditions() {

		this.hasPreExistingConditions = false;
		this.hasPreExistingConditionsDescription = null;

		return this;
	}

	public Questionnaire withPreExistingConditions(String description) {

		this.hasPreExistingConditions = true;
		this.hasPreExistingConditionsDescription = description;

		return this;
	}

	public Questionnaire withoutBelongToMedicalStaff() {

		this.belongToMedicalStaff = false;
		this.belongToMedicalStaffDescription = null;

		return this;
	}

	public Questionnaire withBelongToMedicalStaff(String description) {

		this.belongToMedicalStaff = true;
		this.belongToMedicalStaffDescription = description;

		return this;
	}

	public Questionnaire withoutContactToVulnerablePeople() {

		this.hasContactToVulnerablePeople = false;
		this.hasContactToVulnerablePeopleDescription = null;

		return this;
	}

	public Questionnaire withContactToVulnerablePeople(String description) {

		this.hasContactToVulnerablePeople = true;
		this.hasContactToVulnerablePeopleDescription = description;

		return this;
	}

	public boolean isMedicalStaff() {
		return belongToMedicalStaff;
	}

	public boolean hasSymptoms() {
		return hasSymptoms;
	}

	public boolean hasPreExistingConditions() {
		return hasPreExistingConditions;
	}

	public boolean belongsToMedicalStaff() {
		return belongToMedicalStaff;
	}

	/**
	 * anonymized personal data
	 * 
	 * @return
	 * @since 1.4
	 */
	Questionnaire anonymize() {

		setFamilyDoctor("###");
		setGuessedOriginOfInfection("###");
		setHasPreExistingConditionsDescription("###");
		setBelongToMedicalStaffDescription("###");
		setHasContactToVulnerablePeopleDescription("###");

		return this;
	}

	/**
	 * @since 1.4
	 */
	Questionnaire fillSampleData() {

		familyDoctor = ObjectUtils.defaultIfNull(familyDoctor, "familyDoctor");
		guessedOriginOfInfection = ObjectUtils.defaultIfNull(guessedOriginOfInfection, "guessedOriginOfInfection");
		hasPreExistingConditionsDescription = ObjectUtils.defaultIfNull(hasPreExistingConditionsDescription,
				"hasPreExistingConditionsDescription");
		belongToMedicalStaffDescription = ObjectUtils.defaultIfNull(belongToMedicalStaffDescription,
				"belongToMedicalStaffDescription");
		hasContactToVulnerablePeopleDescription = ObjectUtils.defaultIfNull(hasContactToVulnerablePeopleDescription,
				"hasContactToVulnerablePeopleDescription");

		return this;
	}

	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class SymptomInformation {

		private boolean hasSymptoms;
		private @Nullable LocalDate dayOfFirstSymptoms;
		private @Nullable List<Symptom> symptoms;

		public static SymptomInformation withSymptomsSince(LocalDate daysOfFirstSymptoms, List<Symptom> symptoms) {

			Assert.notNull(symptoms, "Symptoms must not be null!");
			Assert.notNull(daysOfFirstSymptoms, "Day of first symptoms must not be null!");

			return new SymptomInformation(true, daysOfFirstSymptoms, symptoms);
		}

		public static SymptomInformation withoutSymptoms() {
			return new SymptomInformation(false, null, null);
		}
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class QuestionnaireIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = 5716082490854001737L;
		final UUID questionnaireId;
	}
}
