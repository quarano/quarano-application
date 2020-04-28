package quarano.tracking.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quarano.core.validation.AlphaNumeric;
import quarano.core.validation.Alphabetic;
import quarano.core.validation.Strings;
import quarano.tracking.EmailAddress;
import quarano.tracking.PhoneNumber;
import quarano.tracking.ZipCode;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackedPersonDto {

	private @Alphabetic @NotEmpty String firstName, lastName;
	private @Pattern(regexp = Strings.STREET) String street;
	private @AlphaNumeric String houseNumber;
	private @Pattern(regexp = Strings.CITY) @NotEmpty String city;
	private @NotEmpty @Pattern(regexp = ZipCode.PATTERN) String zipCode;
	private @Pattern(regexp = PhoneNumber.PATTERN) String mobilePhone;
	private @Pattern(regexp = PhoneNumber.PATTERN) String phone;
	private @NotEmpty @Pattern(regexp = EmailAddress.PATTERN) String email;
	private @NotNull @Past LocalDate dateOfBirth;

	private boolean infected;
}
