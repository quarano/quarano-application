package quarano.maintenance.web;

import lombok.Data;

@Data
public class ContactPersonDto extends QuaranoDto {
	private String firstName, lastName;
	private String emailAddress;

	private String mobilePhoneNumber;
	private String phoneNumber;
	private AddressDto address;

	private String typeOfContract;
	private String remark;
	private String identificationHint;

	private Boolean isHealthStaff;
	private Boolean isSenior;
	private Boolean hasPreExistingConditions;
}
