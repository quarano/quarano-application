package quarano.sormas_integration.person;

import lombok.*;
import quarano.sormas_integration.common.SormasReportingUser;

import java.time.LocalDateTime;

/**
 * @author Federico Grasso
 *
 * SORMAS Contact model
 */

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SormasContact {
    private String uuid;
    private SormasContactPerson person;
    private String disease = "CORONAVIRUS";
    private LocalDateTime reportDateTime;
    private SormasContactHealthConditions healthConditions;
    private SormasContactCase caze;
    private SormasReportingUser reportingUser;

    /**
     * Getters and setter
     * (Already defined with Lombok
     * but re-defined to make properties accessible by MapStruct)
     */

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public SormasContactPerson getPerson() {
        return person;
    }

    public void setPerson(SormasContactPerson person) {
        this.person = person;
    }

    public LocalDateTime getReportDateTime() {
        return reportDateTime;
    }

    public void setReportDateTime(LocalDateTime reportDateTime) {
        this.reportDateTime = reportDateTime;
    }

    public SormasContactHealthConditions getHealthConditions() {
        return healthConditions;
    }

    public void setHealthConditions(SormasContactHealthConditions healthConditions) {
        this.healthConditions = healthConditions;
    }

    public SormasContactCase getCaze() {
        return caze;
    }

    public void setCaze(SormasContactCase caze) {
        this.caze = caze;
    }

    public SormasReportingUser getReportingUser() {
        return reportingUser;
    }

    public void setReportingUser(SormasReportingUser reportingUser) {
        this.reportingUser = reportingUser;
    }
}
