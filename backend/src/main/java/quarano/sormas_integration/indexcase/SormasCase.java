package quarano.sormas_integration.indexcase;

import lombok.*;

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
    private Date reportDate;
    private Date quarantineTo;
    private Date quarantineFrom;
    private SormasCasePerson person;
    private SormasCaseDistrict district;

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

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Date getQuarantineTo() {
        return quarantineTo;
    }

    public void setQuarantineTo(Date quarantineTo) {
        this.quarantineTo = quarantineTo;
    }

    public Date getQuarantineFrom() {
        return quarantineFrom;
    }

    public void setQuarantineFrom(Date quarantineFrom) {
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
}
