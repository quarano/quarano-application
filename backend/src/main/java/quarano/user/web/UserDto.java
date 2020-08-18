package quarano.user.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quarano.account.Account;
import quarano.department.web.TrackedCaseRepresentations.EnrollmentDto;
import quarano.tracking.web.TrackedPersonDto;

import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {

	private String username;
	private DepartmentDto healthDepartment;
	private TrackedPersonDto client;

	/**
	 * The user's first name.
	 */
	private String firstName;

	/**
	 * The user's last name.
	 */
	private String lastName;
	private EnrollmentDto enrollment;

	public static UserDto of(Account user) {

		var result = new UserDto();
		result.setFirstName(user.getFirstname());
		result.setLastName(user.getLastname());
		result.setUsername(user.getUsername());

		return result;
	}
}
