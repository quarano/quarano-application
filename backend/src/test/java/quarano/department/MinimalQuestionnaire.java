package quarano.department;

class MinimalQuestionnaire extends Questionnaire {

	MinimalQuestionnaire(SymptomInformation symptomInformation, //
			String hasPreExistingConditionsDescription, //
			String belongToMedicalStaffDescription) {
		super(symptomInformation, hasPreExistingConditionsDescription, belongToMedicalStaffDescription);
	}

	public MinimalQuestionnaire() {
		super(SymptomInformation.withoutSymptoms(), null, null);
	}
}
