package quarano.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import quarano.department.web.DepartmentDto;
import quarano.department.web.EnrollmentDto;
import quarano.tracking.web.TrackedPersonDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	@With private String username;
    private DepartmentDto healthDepartment;
	private TrackedPersonDto client;
	private String firstName;
	private String lastName;
	private EnrollmentDto enrollment;
	
}
