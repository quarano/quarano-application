package quarano.department;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
class InitialReportUnitTests {

	@Test
	void isCompleteWithoutOtherContactTypeAndWithoutFirstDayOfSymptomsIfAsymptomatic() {

		var report = allButSymptomsAndOtherContact() //
				.withoutSymptoms();

		assertThat(report.isComplete()).isTrue();
	}

	@Test
	void isCompleteWithoutOtherContactTypeAndWithFirstDayOfSymptomsIfSymptomatic() {

		var report = allButSymptomsAndOtherContact() //
				.withFirstSymptomsAt(LocalDate.now());

		assertThat(report.isComplete()).isTrue();
	}

	@Test
	void doesNotCarryDateOfFirstSymptomIfWithoutSymptom() {

		var report = new InitialReport().withoutSymptoms();

		assertThat(report.getHasSymptoms()).isFalse();
		assertThat(report.getDayOfFirstSymptoms()).isNull();
	}

	@Test
	void exposesHasSymptomsIfDayOfFirstSymptomsIsSet() {

		var now = LocalDate.now();
		var report = new InitialReport().withFirstSymptomsAt(now);

		assertThat(report.getHasSymptoms()).isTrue();
		assertThat(report.getDayOfFirstSymptoms()).isEqualTo(now);
	}

	private static InitialReport allButSymptomsAndOtherContact() {

		return new InitialReport() //
				.setBelongToLaboratoryStaff(false) //
				.setBelongToMedicalStaff(false) //
				.setBelongToNursingStaff(false) //
				.setDirectContactWithLiquidsOfC19pat(false) //
				.setFamilyMember(false) //
				.setFlightCrewMemberWithC19Pat(false) //
				.setFlightPassengerCloseRowC19Pat(false) //
				.setMin15MinutesContactWithC19Pat(false) //
				.setNursingActionOnC19Pat(false);
	}
}
