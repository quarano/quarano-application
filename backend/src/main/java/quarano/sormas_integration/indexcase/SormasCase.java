package quarano.sormas_integration.indexcase;

import lombok.*;
import quarano.sormas_integration.common.SormasReportingUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Federico Grasso
 *
 * SORMAS Case model
 */
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SormasCase {
    private String uuid;
    private String reportDate;
    private String quarantineTo;
    private String quarantineFrom;
    private SormasCasePerson person;
    private SormasCaseDistrict district;
    private SormasCaseRegion region;
    private String disease = "CORONAVIRUS";
    private String caseClassification = "PROBABLE";
    private String investigationStatus = "PENDING";
    private String facilityType = "LABORATORY";
    private SormasReportingUser reportingUser;
    private SormasCaseHealthFacility healthFacility;
    private String caseOrigin;
    private SormasCaseOrigin pointOfEntry;
    private String externalID;

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

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getQuarantineTo() {
        return quarantineTo;
    }

    public void setQuarantineTo(String quarantineTo) {
        this.quarantineTo = quarantineTo;
    }

    public String getQuarantineFrom() {
        return quarantineFrom;
    }

    public void setQuarantineFrom(String quarantineFrom) {
        this.quarantineFrom = quarantineFrom;
    }

    public SormasCasePerson getPerson() {
        return person;
    }

    public void setPerson(SormasCasePerson person) {
        this.person = person;
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

    public SormasReportingUser getReportingUser() {
        return reportingUser;
    }

    public void setReportingUser(SormasReportingUser reportingUser) {
        this.reportingUser = reportingUser;
    }

    public SormasCaseHealthFacility getHealthFacility() {
        return healthFacility;
    }

    public void setHealthFacility(SormasCaseHealthFacility healthFacility) {
        this.healthFacility = healthFacility;
    }

    public String getCaseOrigin() {
        return caseOrigin;
    }

    public void setCaseOrigin(String caseOrigin) {
        this.caseOrigin = caseOrigin;
    }

    public SormasCaseOrigin getPointOfEntry() {
        return pointOfEntry;
    }

    public void setPointOfEntry(SormasCaseOrigin pointOfEntry) {
        this.pointOfEntry = pointOfEntry;
    }

    public String getExternalID() {
        return externalID;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }
}
