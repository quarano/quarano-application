package quarano.sormas_integration.person;

import lombok.*;
import quarano.sormas_integration.common.SormasReportingUser;
import quarano.sormas_integration.indexcase.SormasCaseDistrict;
import quarano.sormas_integration.indexcase.SormasCaseRegion;

import java.time.LocalDate;
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
    private String reportDateTime;
    private SormasContactHealthConditions healthConditions;
    private SormasContactCase caze;
    private SormasReportingUser reportingUser;
    private String contactClassification;
    private SormasCaseDistrict district;
    private SormasCaseRegion region;
    private String lastContactDate;

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

    public String getReportDateTime() {
        return reportDateTime;
    }

    public void setReportDateTime(String reportDateTime) {
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

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getContactClassification() {
        return contactClassification;
    }

    public void setContactClassification(String contactClassification) {
        this.contactClassification = contactClassification;
    }

    public SormasCaseDistrict getDistrict() {
        return district;
    }

    public void setDistrict(SormasCaseDistrict district) {
        this.district = district;
    }

    public SormasCaseRegion getRegion() {
        return region;
    }

    public void setRegion(SormasCaseRegion region) {
        this.region = region;
    }

    public String getLastContactDate() {
        return lastContactDate;
    }

    public void setLastContactDate(String lastContactDate) {
        this.lastContactDate = lastContactDate;
    }
}
