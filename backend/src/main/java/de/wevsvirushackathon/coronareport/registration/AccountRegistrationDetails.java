package de.wevsvirushackathon.coronareport.registration;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegistrationDetails {
	
	private String username;
	private String unencryptedPassword;
	private String firstname;
	private String lastmname;
	private Date dateOfBirth;
	private String clientCode;
	private String email;
	private Long clientId;
	private String departmentId;
	
}
