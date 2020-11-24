package quarano.event.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;
import quarano.core.PhoneNumber;
import quarano.core.validation.Email;
import quarano.core.validation.Strings;
import quarano.core.validation.Textual;
import quarano.tracking.ZipCode;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitorDto {
	private @Pattern(regexp = Strings.NAMES) String lastName, firstName;
	private @Pattern(regexp = Strings.STREET) String street;
	private @Pattern(regexp = Strings.HOUSE_NUMBER) String houseNumber;
	private @Pattern(regexp = ZipCode.PATTERN) String zipCode;
	private @Pattern(regexp = Strings.CITY) String city;
	private @Pattern(regexp = PhoneNumber.PATTERN) String phone;
	private @Email String email;
	private @Textual String qualifier;

	VisitorDto validate(Errors errors) {

		return this;
	}
}
