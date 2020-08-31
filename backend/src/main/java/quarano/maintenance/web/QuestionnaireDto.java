package quarano.maintenance.web;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class QuestionnaireDto extends QuaranoDto {
    private boolean hasSymptoms;
    private LocalDate dayOfFirstSymptoms;
    private List<SymptomDto> symptoms;

    private String familyDoctor;
    private String guessedOriginOfInfection;

    private boolean hasPreExistingConditions;
    private String hasPreExistingConditionsDescription;

    private boolean belongToMedicalStaff;
    private String belongToMedicalStaffDescription;

    private Boolean hasContactToVulnerablePeople;
    private String hasContactToVulnerablePeopleDescription;
}
