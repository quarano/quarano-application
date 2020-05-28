package quarano.user.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quarano.account.Department;
import quarano.account.DepartmentContact;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class DepartmentDto {

	private String name;
	private String email;
	private String phone;

	public static DepartmentDto of(Department department, DepartmentContact contact) {

		return new DepartmentDto(department.getName(), contact.getEmailAddress().toString(),
				contact.getPhoneNumber().toString());
	}
}
