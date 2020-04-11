package quarano.department.web;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
class InitialReportDto {

	private LocalDateTime dateTime;
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
	private LocalDate dayOfFirstSymptoms;
}
