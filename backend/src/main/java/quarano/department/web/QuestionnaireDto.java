package quarano.department.web;

import lombok.Data;
import lombok.Setter;
import quarano.core.validation.Textual;
import quarano.department.Questionnaire;
import quarano.masterdata.Symptom;
import quarano.masterdata.SymptomRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.validation.Errors;

@Data
public class QuestionnaireDto {

	private @NotNull @Setter Boolean hasSymptoms;
	private @PastOrPresent LocalDate dayOfFirstSymptoms;
	private List<UUID> symptoms;

	private @Textual String familyDoctor;
	private @Textual String guessedOriginOfInfection;
	private @PastOrPresent LocalDate guessedDateOfInfection;

	private @NotNull @Setter Boolean hasPreExistingConditions;
	private @Textual String hasPreExistingConditionsDescription;

	private @NotNull @Setter Boolean belongToMedicalStaff;
	private @Textual String belongToMedicalStaffDescription;

	private @Setter Boolean hasContactToVulnerablePeople;
	private @Textual String hasContactToVulnerablePeopleDescription;

	Questionnaire applyTo(Questionnaire report, SymptomRepository symptoms) {

		List<Symptom> symptomsOfReport = Collections.emptyList();

		if (dayOfFirstSymptoms != null) {

			if (null != this.symptoms) {
				symptomsOfReport = this.symptoms.stream()
						.map(it -> symptoms.findById(it).get())
						.collect(Collectors.toList());
			}

			report.withFirstSymptomsAt(dayOfFirstSymptoms, symptomsOfReport);
		}

		if (hasPreExistingConditionsDescription != null) {
			report.withPreExistingConditions(hasPreExistingConditionsDescription);
		}

		if (belongToMedicalStaffDescription != null) {
			report.withBelongToMedicalStaff(belongToMedicalStaffDescription);
		}

		if (hasContactToVulnerablePeopleDescription != null) {
			report.withContactToVulnerablePeople(hasContactToVulnerablePeopleDescription);
		}

		if (hasSymptoms != null) {
			report = hasSymptoms
					? report.withFirstSymptomsAt(dayOfFirstSymptoms, symptomsOfReport)
					: report.withoutSymptoms();
		}

		if (hasPreExistingConditions != null) {
			report = hasPreExistingConditions
					? report.withPreExistingConditions(hasPreExistingConditionsDescription)
					: report.withoutPreExistingConditions();
		}

		if (belongToMedicalStaff != null) {
			report = belongToMedicalStaff
					? report.withBelongToMedicalStaff(belongToMedicalStaffDescription)
					: report.withoutBelongToMedicalStaff();
		}

		if (hasContactToVulnerablePeople != null) {
			report = hasContactToVulnerablePeople
					? report.withContactToVulnerablePeople(hasContactToVulnerablePeopleDescription)
					: report.withoutContactToVulnerablePeople();
		}

		return report;
	}

	QuestionnaireDto validate(Errors errors) {

		if (Boolean.TRUE.equals(hasPreExistingConditions)
				&& (hasPreExistingConditionsDescription == null
						|| hasPreExistingConditionsDescription.isBlank())) {
			errors.rejectValue("hasPreExistingConditionsDescription",
					"NotNull.IntialReportDto.hasPreExistingConditionsDescription");
		}

		if (Boolean.TRUE.equals(belongToMedicalStaff)
				&& (belongToMedicalStaffDescription == null
						|| belongToMedicalStaffDescription.isBlank())) {
			errors.rejectValue("belongToMedicalStaffDescription", "NotNull.IntialReportDto.belongToMedicalStaffDescription");
		}

		if (Boolean.TRUE.equals(hasContactToVulnerablePeople)
				&& (hasContactToVulnerablePeopleDescription == null
						|| hasContactToVulnerablePeopleDescription.isBlank())) {
			errors.rejectValue("hasContactToVulnerablePeopleDescription",
					"NotNull.IntialReportDto.hasContactToVulnerablePeopleDescription");
		}

		if (Boolean.TRUE.equals(hasSymptoms) && dayOfFirstSymptoms == null) {
			errors.rejectValue("dayOfFirstSymptoms", "NotNull.IntialReportDto.dayOfFirstSymptoms");
		}

		if (dayOfFirstSymptoms != null && guessedDateOfInfection != null
				&& !guessedDateOfInfection.isBefore(dayOfFirstSymptoms)) {
			errors.rejectValue("guessedDateOfInfection", "Invalid.IntialReportDto.guessedDateOfInfection");
		}

		return this;
	}
}
