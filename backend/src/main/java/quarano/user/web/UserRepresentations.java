/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.user.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import quarano.account.AccountService;
import quarano.account.Department;
import quarano.account.Password.EncryptedPassword;
import quarano.account.Password.UnencryptedPassword;
import quarano.core.EmailAddress;
import quarano.core.validation.UserName;
import quarano.core.web.MapperWrapper;
import quarano.department.Enrollment;
import quarano.department.web.TrackedCaseRepresentations;
import quarano.department.web.TrackedCaseRepresentations.EnrollmentDto;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonManagement.PasswordReset;
import quarano.tracking.web.TrackedPersonDto;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class UserRepresentations {

	private final MapperWrapper mapper;
	private final TrackedCaseRepresentations delegate;

	EnrollmentDto toRepresentation(Enrollment enrollment) {
		return delegate.toRepresentation(enrollment);
	}

	DepartmentDto toRepresentation(Department department) {
		return mapper.map(department, DepartmentDto.class);
	}

	TrackedPersonDto toRepresentation(TrackedPerson person) {
		return mapper.map(person, TrackedPersonDto.class);
	}

	PasswordReset from(PasswordResetInput request, UUID token) {
		return request.toPasswordReset(token);
	}

	@Data
	static abstract class NewPassword {

		/**
		 * The new password to set.
		 */
		protected @NotBlank String password;

		/**
		 * The new password repeated for verification.
		 */
		protected @NotBlank String passwordConfirm;

		UnencryptedPassword toPassword() {
			return UnencryptedPassword.of(password);
		}

		NewPassword validate(Errors errors) {

			if (!password.equals(passwordConfirm)) {
				errors.rejectValue("password", "Password.nonMatching");
				errors.rejectValue("passwordConfirm", "Password.nonMatching");
			}

			return this;
		}
	}

	@Data
	static class PasswordResetRequest {

		/**
		 * The username of the account for which to reset the password.
		 */
		private @UserName @NotBlank String username;

		/**
		 * The email address associated with the the account to reset the password for.
		 */
		private @NotEmpty @Email String email;

		public EmailAddress toEmail() {
			return EmailAddress.of(email);
		}
	}

	@Data
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = true)
	static class PasswordChangeRequest extends NewPassword {

		/**
		 * The current password.
		 */
		@NotBlank String current;

		PasswordChangeRequest(String current, String password, String passwordConfirm) {

			this.current = current;
			this.password = password;
			this.passwordConfirm = passwordConfirm;
		}

		NewPassword validate(Errors errors, EncryptedPassword existing, AccountService accounts) {

			super.validate(errors);

			if (!accounts.matches(UnencryptedPassword.of(current), existing)) {
				errors.rejectValue("current", "Password.currentInvalid");
			}

			if (accounts.matches(UnencryptedPassword.of(password), existing)) {
				errors.rejectValue("password", "Password.isOldPassword");
			}

			return this;
		}
	}

	@Data
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = true)
	@NoArgsConstructor
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	static class PasswordResetInput extends NewPassword {

		/**
		 * The username of the account for which to reset the password.
		 */
		private @UserName @NotBlank String username;

		/**
		 * The date of birth of the person for which the password shall be reset.
		 */
		private @Past @NotNull LocalDate dateOfBirth;

		/*
		 * (non-Javadoc)
		 * @see quarano.user.web.UserController.NewPassword#validate(org.springframework.validation.Errors)
		 */
		@Override
		PasswordResetInput validate(Errors errors) {

			super.validate(errors);

			return this;
		}

		PasswordReset toPasswordReset(UUID token) {
			return new PasswordReset(token, UnencryptedPassword.of(password), dateOfBirth);
		}
	}
}
