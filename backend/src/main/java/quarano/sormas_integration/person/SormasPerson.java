package quarano.sormas_integration.person;


import lombok.*;

/**
 * @author Federico Grasso
 *
 * SORMAS Person model
 */

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SormasPerson {
    private String uuid;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phone;
    private SormasPersonAddress address;
    private Integer birthdateDD;
    private Integer birthdateMM;
    private Integer birthdateYYYY;

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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public SormasPersonAddress getAddress() {
        return address;
    }

    public void setAddress(SormasPersonAddress address) {
        this.address = address;
    }

    public Integer getBirthdateDD() {
        return birthdateDD;
    }

    public void setBirthdateDD(Integer birthdateDD) {
        this.birthdateDD = birthdateDD;
    }

    public Integer getBirthdateMM() {
        return birthdateMM;
    }

    public void setBirthdateMM(Integer birthdateMM) {
        this.birthdateMM = birthdateMM;
    }

    public Integer getBirthdateYYYY() {
        return birthdateYYYY;
    }

    public void setBirthdateYYYY(Integer birthdateYYYY) {
        this.birthdateYYYY = birthdateYYYY;
    }
}
