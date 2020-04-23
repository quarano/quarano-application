package quarano.department.web;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import quarano.tracking.EmailAddress;
import quarano.tracking.PhoneNumber;
import quarano.tracking.ZipCode;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TrackedCaseDto {

	private @NotEmpty String lastName;
	private @NotEmpty String firstName;
	private @NotNull LocalDate testDate;
	private @NotNull LocalDate quarantineStartDate;
	private @NotNull LocalDate quarantineEndDate;

	private String street;
	private String houseNumber;
	private String city;
	private @Pattern(regexp = ZipCode.PATTERN) String zipCode;
	private @Pattern(regexp = PhoneNumber.PATTERN) String mobilePhone;
	private @Pattern(regexp = PhoneNumber.PATTERN) String phone;
	private @Pattern(regexp = EmailAddress.PATTERN) String email;
	private @Past LocalDate dateOfBirth;

	private String comment;

	private boolean infected;

	Errors validate(Errors errors) {

		if (!StringUtils.hasText(phone) && !StringUtils.hasText(mobilePhone)) {
			errors.reject("phone", "PhoneOrMobile");
			errors.reject("mobilePhone", "PhoneOrMobile");
		}

		return errors;
	}
}
