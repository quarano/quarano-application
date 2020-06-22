package quarano.tracking.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quarano.core.PhoneNumber;
import quarano.core.validation.Email;
import quarano.core.validation.Strings;
import quarano.tracking.ZipCode;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackedPersonDto {

	private @Pattern(regexp = Strings.NAMES) @NotEmpty String firstName, lastName;
	private @Pattern(regexp = Strings.STREET) String street;
	private @Pattern(regexp = Strings.HOUSE_NUMBER) String houseNumber;
	private @Pattern(regexp = Strings.CITY) @NotEmpty String city;
	private @Pattern(regexp = ZipCode.PATTERN) @NotEmpty String zipCode;
	private @Pattern(regexp = PhoneNumber.PATTERN) String mobilePhone;
	private @Pattern(regexp = PhoneNumber.PATTERN) String phone;
	private @NotEmpty @Email String email;
	private @NotNull @Past LocalDate dateOfBirth;
	private List<URI> originContacts;
}
