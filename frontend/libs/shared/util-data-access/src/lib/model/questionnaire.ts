export interface QuestionnaireDto {
  hasSymptoms: boolean;
  dayOfFirstSymptoms?: string;
  symptoms?: string[];
  familyDoctor: string;
  guessedOriginOfInfection: string;
  guessedDateOfInfection: string;
  hasPreExistingConditions: boolean;
  hasPreExistingConditionsDescription?: string;
  belongToMedicalStaff: boolean;
  belongToMedicalStaffDescription?: string;
  hasContactToVulnerablePeople: boolean;
  hasContactToVulnerablePeopleDescription?: string;
}
