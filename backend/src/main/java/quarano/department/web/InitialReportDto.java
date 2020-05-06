package quarano.department.web;

import lombok.Data;
import quarano.department.InitialReport;

import java.time.LocalDate;

import javax.validation.constraints.Past;

import org.springframework.validation.Errors;

@Data
public class InitialReportDto {

	private Boolean min15MinutesContactWithC19Pat;
	private Boolean nursingActionOnC19Pat;
	private Boolean directContactWithLiquidsOfC19pat;
	private Boolean flightPassengerCloseRowC19Pat;
	private Boolean flightCrewMemberWithC19Pat;
	private Boolean belongToMedicalStaff;
	private Boolean belongToNursingStaff;
	private Boolean belongToLaboratoryStaff;
	private Boolean familyMember;
	private Boolean hasSymptoms;
	private String OtherContactType;
	private @Past LocalDate dayOfFirstSymptoms;

	InitialReport applyTo(InitialReport report) {

		if (dayOfFirstSymptoms != null) {
			return report.withFirstSymptomsAt(dayOfFirstSymptoms);
		}

		if (hasSymptoms == null) {
			return report;
		}

		return hasSymptoms //
				? report.withFirstSymptomsAt(dayOfFirstSymptoms)
				: report.withoutSymptoms();
	}

	Errors validate(Errors errors) {

		if (Boolean.TRUE.equals(hasSymptoms) && dayOfFirstSymptoms == null) {
			errors.rejectValue("dayOfFirstSymptoms", "NotNull.IntialReportDto.dayOfFirstSymptoms");
		}

		return errors;
	}
}
