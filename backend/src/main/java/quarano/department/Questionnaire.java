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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import quarano.core.QuaranoEntity;
import quarano.department.Questionnaire.QuestionnaireIdentifier;
import quarano.reference.Symptom;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.jddd.core.types.Identifier;

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
@Slf4j
public class Questionnaire extends QuaranoEntity<TrackedCase, QuestionnaireIdentifier> {

	private @Setter(value = AccessLevel.NONE) boolean hasSymptoms;
	private LocalDate dayOfFirstSymptoms;
	private @ManyToMany List<Symptom> symptoms;
	
	private String familyDoctor;
	private String guessedOriginOfInfection;

	private @Setter(value = AccessLevel.NONE) boolean hasPreExistingConditions;
	private String hasPreExistingConditionsDescription;

	private @Setter(value = AccessLevel.NONE) boolean belongToMedicalStaff;
	private String belongToMedicalStaffDescription;
	
	private @Setter(value = AccessLevel.NONE) Boolean hasContactToVulnerablePeople;
	private String hasContactToVulnerablePeopleDescription;

	private Questionnaire() {
		 // noArgs Constructor
	}


	public Questionnaire(SymptomInformation symptomInformation, String hasPreExistingConditionsDescription, String belongToMedicalStaffDescription) {
		
		this.dayOfFirstSymptoms = symptomInformation.dayOfFirstSymptoms;
		this.hasSymptoms = symptomInformation.hasSymptoms;
		this.symptoms = symptomInformation.symptoms;
		
		if(null != hasPreExistingConditionsDescription) {
			this.withPreExistingConditions(hasPreExistingConditionsDescription);
		}
		
		if(null != belongToMedicalStaffDescription) {
			this.withBelongToMedicalStaff(belongToMedicalStaffDescription);
		}		
		
		this.id = QuestionnaireIdentifier.of(UUID.randomUUID());
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
	
	@AllArgsConstructor (access = AccessLevel.PRIVATE)
	public static class SymptomInformation{
		
		private boolean hasSymptoms;
		private LocalDate dayOfFirstSymptoms;
		private List<Symptom> symptoms;
		
		
		public static SymptomInformation withSymptomsSince(LocalDate daysOfFirstSymptoms, List<Symptom> symptoms){
			
			if(symptoms == null) {
				throw new IllegalArgumentException("Symptoms must be specified");
			}
			
			if(daysOfFirstSymptoms == null) {
				throw new IllegalArgumentException("Symptoms must be specified");
			}
			
			return new SymptomInformation(true, daysOfFirstSymptoms, symptoms);
		}
		
		public static SymptomInformation withoutSymptoms(){
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
