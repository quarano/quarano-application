package quarano.registration.web;

import java.time.LocalDate;
import java.util.UUID;

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
	private LocalDate dateOfBirth;
	private UUID clientCode;
	private String email;
	private UUID clientId;
	private String departmentId;

}
