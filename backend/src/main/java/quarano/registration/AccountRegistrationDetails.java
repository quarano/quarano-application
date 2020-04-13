package quarano.registration;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import quarano.department.Department.DepartmentIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegistrationDetails {
	
	private String username;
	private String unencryptedPassword;
	private String firstname;
	private String lastname;
	private LocalDate dateOfBirth;
	private String activationCodeLiteral;
	private String email;
	private TrackedPersonIdentifier trackedPersonId;
	private DepartmentIdentifier departmentId;
	
}
