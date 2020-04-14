package quarano.tracking.web;

import lombok.Data;
import lombok.Getter;
import quarano.tracking.EmailAddress;
import quarano.tracking.PhoneNumber;
import quarano.tracking.ZipCode;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
class ContactPersonDto {

	@Getter(onMethod = @__(@JsonProperty(access = JsonProperty.Access.READ_ONLY))) //
	private UUID id;

	private @NotEmpty String lastName, firstName, street;
	private String houseNumber;
	private @NotEmpty String city;
	private @NotEmpty @Pattern(regexp = ZipCode.PATTERN) String zipCode;
	private @Pattern(regexp = PhoneNumber.PATTERN) String phone;
	private @Pattern(regexp = PhoneNumber.PATTERN) String mobilePhone;
	private @Pattern(regexp = EmailAddress.PATTERN) String email;
	private String remark;
	private String identificationHint;
	private Boolean isHealthStaff;
	private Boolean isSenior;
	private Boolean hasPreExistingConditions;
}
