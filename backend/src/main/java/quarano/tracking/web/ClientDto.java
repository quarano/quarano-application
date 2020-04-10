package quarano.tracking.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quarano.tracking.PhoneNumber;
import quarano.tracking.ZipCode;

import java.time.LocalDateTime;

import org.springframework.validation.Errors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

	private Long clientId;
	private String clientCode;

	private String surename;
	private String firstname;
	private String street;
	private String city;
	private String zipCode;
	private String mobilephone;
	private String phone;
	private String email;

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
