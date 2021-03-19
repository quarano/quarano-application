package quarano.sormas_integration.mapping;

import java.time.LocalDate;

/**
 * @author Federico Grasso
 *
 * Sormas case Dto class
 */

public class SormasCaseDto {
    private String lastName;
    private String firstName;
    private LocalDate testDate;
    private LocalDate quarantineStartDate;
    private LocalDate quarantineEndDate;

    private boolean infected = true;

    private String sormasUuid;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }

    public LocalDate getQuarantineStartDate() {
        return quarantineStartDate;
    }

    public void setQuarantineStartDate(LocalDate quarantineStartDate) {
        this.quarantineStartDate = quarantineStartDate;
    }

    public LocalDate getQuarantineEndDate() {
        return quarantineEndDate;
    }

    public void setQuarantineEndDate(LocalDate quarantineEndDate) {
        this.quarantineEndDate = quarantineEndDate;
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    public String getSormasUuid() {
        return sormasUuid;
    }

    public void setSormasUuid(String sormasUuid) {
        this.sormasUuid = sormasUuid;
    }
}
