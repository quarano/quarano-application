package quarano.sormas_integration.indexcase;

import lombok.*;

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
    private LocalDateTime reportDate;
    private LocalDateTime quarantineTo;
    private LocalDateTime quarantineFrom;
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

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public LocalDateTime getQuarantineTo() {
        return quarantineTo;
    }

    public void setQuarantineTo(LocalDateTime quarantineTo) {
        this.quarantineTo = quarantineTo;
    }

    public LocalDateTime getQuarantineFrom() {
        return quarantineFrom;
    }

    public void setQuarantineFrom(LocalDateTime quarantineFrom) {
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
