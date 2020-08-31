package quarano.maintenance.web;

import java.util.List;

import lombok.Data;

@Data
public class AccountDto extends QuaranoDto {

	private EncryptedPasswordDto password;
	private String username;
	private String firstname;
	private String lastname;
	private String email;
	private String departmentId;
	private List<RoleDto> roles;

}
