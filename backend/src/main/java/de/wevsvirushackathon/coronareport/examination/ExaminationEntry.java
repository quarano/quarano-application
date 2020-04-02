package de.wevsvirushackathon.coronareport.examination;

import de.wevsvirushackathon.coronareport.diary.TypeOfExamination;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Data
public class ExaminationEntry {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private RespiratoryInfectionQuery respiratoryInfectionQuery;

    private TypeOfExamination typeOfExamination;

    private boolean executed;

    private LocalDateTime localDateTime;
    private boolean result; //
    private String locationOfExamination;
    private String contactExaminerPhone;
    private boolean agreementContactExaminer;
    private RespiratoryInfectionStatus status;

}
