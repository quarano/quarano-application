package quarano.user.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;
import static quarano.actions.web.AnomaliesLinkRelations.*;
import static quarano.department.web.TrackedCaseLinkRelations.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import quarano.account.Account;
import quarano.account.AccountService;
import quarano.account.DepartmentContact.ContactType;
import quarano.account.DepartmentRepository;
import quarano.account.Password.EncryptedPassword;
import quarano.account.Password.UnencryptedPassword;
import quarano.actions.web.AnomaliesController;
import quarano.core.web.LoggedIn;
import quarano.core.web.MappedPayloads;
import quarano.department.CaseType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.department.web.TrackedCaseController;
import quarano.tracking.TrackedPersonRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.MvcLink;
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

	private final @NonNull TrackedPersonRepository trackedPersonRepository;
	private final @NonNull DepartmentRepository departments;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull AccountService accounts;
	private final @NonNull UserRepresentations representations;

	@GetMapping("/me")
	public ResponseEntity<UserDto> getMe(@LoggedIn Account account) {

		var userDto = UserDto.of(account);
		var caseController = on(TrackedCaseController.class);

		if (account.isTrackedPerson()) {
			var person = trackedPersonRepository.findByAccount(account);

			person.map(representations::toRepresentation)
					.ifPresent(userDto::setClient);

			var enrollmentLink = MvcLink.of(caseController.enrollment(null), ENROLLMENT);
			var trackedCase = person.flatMap(cases::findByTrackedPerson);

			trackedCase
					.map(TrackedCase::getEnrollment)
					.map(representations::toRepresentation)
					.ifPresent(it -> {

						userDto.add(enrollmentLink);
						userDto.add(enrollmentLink.withRel(IanaLinkRelations.NEXT));
						userDto.setEnrollment(it);
					});

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

			if (account.isCaseAgent()) {

				userDto.add(MvcLink.of(caseController.getCases(null, null, null, null), CASES));
				userDto.add(MvcLink.of(on(AnomaliesController.class).getAllCasesByAnomalies(null), ANOMALIES));
			}
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

		/**
		 * The current password.
		 */
		@NotBlank
		String current;

		/**
		 * The new password to set.
		 */
		@NotBlank
		String password;

		/**
		 * The new password repeated for verification.
		 */
		@NotBlank
		String passwordConfirm;

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
