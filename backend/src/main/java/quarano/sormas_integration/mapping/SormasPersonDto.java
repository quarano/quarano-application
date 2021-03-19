package quarano.sormas_integration.mapping;

import quarano.core.Address;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.tracking.TrackedPerson;

import java.time.LocalDate;
import java.util.Locale;

public class SormasPersonDto {

    private TrackedPerson.TrackedPersonIdentifier id;
    private String firstName;
    private String lastName;
    private PhoneNumber phoneNumber;
    private PhoneNumber mobilePhoneNumber;
    private EmailAddress emailAddress;
    private LocalDate dateOfBirth;
    private Address address;
    private String sormasUuid;
    private Locale locale;

    public TrackedPerson.TrackedPersonIdentifier getId() {
        return id;
    }

    public void setId(TrackedPerson.TrackedPersonIdentifier id) {
        this.id = id;
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

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PhoneNumber getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(PhoneNumber mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getSormasUuid() {
        return sormasUuid;
    }

    public void setSormasUuid(String sormasUuid) {
        this.sormasUuid = sormasUuid;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
