package quarano.registration;

import java.time.LocalDate;
import java.util.UUID;

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
	private UUID activationCodeLiteral;
	private String email;
	private TrackedPersonIdentifier trackedPersonId;
	private DepartmentIdentifier departmentId;
	
}
