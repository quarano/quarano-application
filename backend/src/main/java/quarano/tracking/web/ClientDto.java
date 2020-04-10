package quarano.tracking.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quarano.tracking.EmailAddress;
import quarano.tracking.PhoneNumber;
import quarano.tracking.ZipCode;

import java.time.LocalDateTime;

import javax.validation.constraints.Pattern;

import org.springframework.validation.Errors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

	private String clientCode;

	private String surename;
	private String firstname;
	private String street;
	private String city;
	private @Pattern(regexp = ZipCode.PATTERN) String zipCode;
	private @Pattern(regexp = PhoneNumber.PATTERN) String mobilephone;
	private @Pattern(regexp = PhoneNumber.PATTERN) String phone;
	private @Pattern(regexp = EmailAddress.PATTERN) String email;

	private LocalDateTime quarantineStartDateTime;
	private LocalDateTime quarantineEndDateTime;

	private boolean infected;

	private ClientType type;

	Errors validate(Errors errors) {

		if (!ZipCode.isValid(zipCode)) {
			errors.reject("zipCode");
		}

		if (!PhoneNumber.isValid(phone)) {
			errors.reject("phone");
		}

		return errors;
	}
}
