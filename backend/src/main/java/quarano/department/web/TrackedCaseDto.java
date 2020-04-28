package quarano.department.web;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import quarano.core.validation.AlphaNumeric;
import quarano.core.validation.Alphabetic;
import quarano.core.validation.Strings;
import quarano.core.validation.Textual;
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

	private @NotEmpty @Alphabetic String lastName;
	private @NotEmpty @Alphabetic String firstName;
	private @NotNull LocalDate testDate;
	private @NotNull LocalDate quarantineStartDate;
	private @NotNull LocalDate quarantineEndDate;

	private @Pattern(regexp = Strings.STREET) String street;
	private @AlphaNumeric String houseNumber;
	private @Pattern(regexp = Strings.CITY) String city;
	private @Pattern(regexp = ZipCode.PATTERN) String zipCode;
	private @Pattern(regexp = PhoneNumber.PATTERN) String mobilePhone;
	private @Pattern(regexp = PhoneNumber.PATTERN) String phone;
	private @Pattern(regexp = EmailAddress.PATTERN) String email;
	private @Past LocalDate dateOfBirth;

	private @Textual String comment;

	private boolean infected;

	Errors validate(Errors errors) {

		if (!StringUtils.hasText(phone) && !StringUtils.hasText(mobilePhone)) {
			errors.rejectValue("phone", "PhoneOrMobile");
			errors.rejectValue("mobilePhone", "PhoneOrMobile");
		}

		return errors;
	}
}
