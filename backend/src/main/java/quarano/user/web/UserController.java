package quarano.user.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import quarano.account.Account;
import quarano.account.AccountService;
import quarano.account.DepartmentContact.ContactType;
import quarano.account.DepartmentRepository;
import quarano.account.Password.EncryptedPassword;
import quarano.account.Password.UnencryptedPassword;
import quarano.core.web.LoggedIn;
import quarano.core.web.MappedPayloads;
import quarano.department.CaseType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPersonRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.hateoas.LinkRelation;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	public static final LinkRelation CHANGE_PASSWORD = LinkRelation.of("change-password");

	private final @NonNull TrackedPersonRepository trackedPersonRepository;
	private final @NonNull DepartmentRepository departments;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull AccountService accounts;
	private final @NonNull UserRepresentations representations;

	@GetMapping("/me")
	ResponseEntity<?> getMe(@LoggedIn Account account) {

		var userDto = UserDto.of(account);

		if (account.isTrackedPerson()) {
			var person = trackedPersonRepository.findByAccount(account);

			person.map(representations::toRepresentation)
					.ifPresent(userDto::setClient);

			var trackedCase = person.flatMap(cases::findByTrackedPerson);

			trackedCase//
					.map(TrackedCase::getEnrollment)
					.map(representations::toRepresentation)
					.ifPresent(userDto::setEnrollment);

			trackedCase.map(TrackedCase::getType)
					.flatMap(type -> {

						var contactType = CaseType.INDEX == type ? ContactType.INDEX : ContactType.CONTACT;

						return departments.findById(account.getDepartmentId())
								.flatMap(department -> department.getContact(contactType)
										.map(contact -> DepartmentDto.of(department, contact)));
					})
					.ifPresent(userDto::setHealthDepartment);
		} else {
			departments.findById(account.getDepartmentId())
					.map(representations::toRepresentation)
					.ifPresent(userDto::setHealthDepartment);
		}

		return ResponseEntity.ok(userDto);
	}

	@PutMapping("/me/password")
	public HttpEntity<?> putPassword(@Valid @RequestBody NewPassword payload, Errors errors, @LoggedIn Account account) {

		return MappedPayloads.of(payload, errors)
				.alwaysMap((password, it) -> password.validate(it, account.getPassword(), accounts))
				.peek(it -> accounts.changePassword(UnencryptedPassword.of(it.password), account))
				.onValidGet(() -> ResponseEntity.noContent().build());
	}

	@Value
	static class NewPassword {

		private final @NotBlank String current, password, passwordConfirm;

		NewPassword validate(Errors errors, EncryptedPassword existing, AccountService accounts) {

			if (!accounts.matches(UnencryptedPassword.of(current), existing)) {
				errors.rejectValue("current", "Invalid");
			}

			if (!password.equals(passwordConfirm)) {
				errors.rejectValue("password", "NonMatching.password");
				errors.rejectValue("passwordConfirm", "NonMatching.password");
			}

			return this;
		}
	}
}
