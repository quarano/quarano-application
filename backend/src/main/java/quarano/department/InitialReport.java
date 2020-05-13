package quarano.department;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import quarano.core.QuaranoEntity;
import quarano.department.InitialReport.InitialReportIdentifier;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jddd.core.types.Identifier;
import org.springframework.util.ReflectionUtils;

/**
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 */
@Entity
@Table(name = "initial_reports")
@Data
@EqualsAndHashCode(callSuper = true, of = {})
@Setter(AccessLevel.PACKAGE)
@Slf4j
public class InitialReport extends QuaranoEntity<TrackedCase, InitialReportIdentifier> {

	private Boolean min15MinutesContactWithC19Pat, //
			nursingActionOnC19Pat, //
			directContactWithLiquidsOfC19pat, //
			flightPassengerCloseRowC19Pat, //
			flightCrewMemberWithC19Pat, //
			belongToMedicalStaff, //
			belongToNursingStaff, //
			belongToLaboratoryStaff, //
			familyMember; //

	private @Setter(value = AccessLevel.NONE) Boolean hasSymptoms;

	private String otherContactType;
	private LocalDate dayOfFirstSymptoms;

	InitialReport() {
		this.id = InitialReportIdentifier.of(UUID.randomUUID());
	}

	public InitialReport withoutSymptoms() {

		this.hasSymptoms = false;
		this.dayOfFirstSymptoms = null;

		return this;
	}

	public InitialReport withFirstSymptomsAt(LocalDate date) {

		this.dayOfFirstSymptoms = date;
		this.hasSymptoms = true;

		return this;
	}

	public boolean isComplete() {

		for (Field it : InitialReport.class.getDeclaredFields()) {

			ReflectionUtils.makeAccessible(it);

			if (it.getName().equals("otherContactType") || it.getName().equals("dayOfFirstSymptoms")) {
				continue;
			}

			if (ReflectionUtils.getField(it, this) == null) {

				log.warn("Report field {} is null. Report not complete!", it);

				return false;
			}
		}

		return true;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class InitialReportIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = 5716082490854001737L;
		final UUID initialReportId;
	}
}
