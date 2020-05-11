package quarano.department.web;

import io.jsonwebtoken.lang.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quarano.core.validation.Email;
import quarano.core.validation.UserName;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.validation.Errors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {

	private @UserName @NotBlank String username;
	private @NotBlank String password, passwordConfirm;  // password rules are tested in entity
	private @NotNull @Past LocalDate dateOfBirth;
	private @NotNull UUID clientCode;
	private @Email @NotBlank String email;
	private UUID clientId;
	private String departmentId;

	Errors validate(Errors errors) {

		if (!Objects.nullSafeEquals(password, passwordConfirm)) {
			errors.rejectValue("passwordConfirm", "NonMatching.password");
		}

		return errors;
	}
}
