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
@Accessors(chain = true)
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
		final UUID id;
	}
}
