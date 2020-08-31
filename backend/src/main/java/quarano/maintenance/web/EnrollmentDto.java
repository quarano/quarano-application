package quarano.maintenance.web;

import lombok.Data;

@Data
public class EnrollmentDto  {
    private boolean completedPersonalData;
    private boolean completedQuestionnaire;
    private boolean completedContactRetro;
}
