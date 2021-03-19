package quarano.sormas_integration.mapping;

public class SormasContactDto {
    private String firstName;
    private String lastName;
    private String sormasUuid;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSormasUuid() {
        return sormasUuid;
    }

    public void setSormasUuid(String sormasUuid) {
        this.sormasUuid = sormasUuid;
    }
}
