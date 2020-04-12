package de.wevsvirushackathon.coronareport.client;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRegistrationDto {
	
	private String username;
	private String password;
	private String firstname;
	private String lastmname;
	private Date dateOfBirth;
	private String clientCode;
	private String email;
	private Long clientId;
	private String departmentId;
	

}
