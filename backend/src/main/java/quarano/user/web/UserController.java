package quarano.user.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;
import static quarano.actions.web.AnomaliesLinkRelations.*;
import static quarano.department.web.TrackedCaseLinkRelations.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.AccountService;
import quarano.account.AuthenticationManager;
import quarano.account.DepartmentContact.ContactType;
import quarano.account.DepartmentRepository;
import quarano.account.Password.UnencryptedPassword;
import quarano.actions.web.AnomaliesController;
import quarano.core.web.I18nedMessage;
import quarano.core.web.LoggedIn;
import quarano.core.web.MappedPayloads;
import quarano.core.web.QuaranoApiRoot;
import quarano.department.CaseType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.department.web.TrackedCaseController;
import quarano.security.web.AuthenticationController;
import quarano.security.web.AuthenticationLinkRelations;
import quarano.tracking.TrackedPersonManagement;
import quarano.user.web.UserRepresentations.PasswordChangeRequest;
import quarano.user.web.UserRepresentations.PasswordResetInput;
import quarano.user.web.UserRepresentations.PasswordResetRequest;

import java.util.UUID;
import java.util.function.Supplier;

import javax.validation.Valid;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.MvcLink;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final @NonNull TrackedPersonManagement trackedPeople;
	private final @NonNull DepartmentRepository departments;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull AccountService accounts;
	private final @NonNull UserRepresentations representations;

	@GetMapping("/user/me")
	public ResponseEntity<UserDto> getMe(@LoggedIn Account account) {

		var userDto = UserDto.of(account);
		var caseController = on(TrackedCaseController.class);

		if (account.isTrackedPerson()) {
			var person = trackedPeople.findByAccount(account);

			person.map(representations::toRepresentation)
					.ifPresent(userDto::setClient);

			var trackedCase = person.flatMap(cases::findByTrackedPerson);

			trackedCase
					.map(TrackedCase::getEnrollment)
					.ifPresent(it -> {

						var enrollmentDto = representations.toRepresentation(it);
						var enrollmentLink = MvcLink.of(caseController.enrollment(null), ENROLLMENT);

						userDto.setEnrollment(enrollmentDto)
								.add(enrollmentLink)
								.addIf(!it.isComplete(),
										() -> MvcLink.of(caseController.enrollment(null), ENROLLMENT).withRel(IanaLinkRelations.NEXT));
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

	@PutMapping("/user/me/password")
	public HttpEntity<?> putPassword(@Valid @RequestBody PasswordChangeRequest payload, Errors errors,
			@LoggedIn Account account) {

		return MappedPayloads.of(payload, errors)
				.alwaysMap((password, it) -> password.validate(it, account.getPassword(), accounts))
				.peek(it -> accounts.changePassword(UnencryptedPassword.of(it.password), account))
				.onValidGet(() -> ResponseEntity.noContent().build());
	}

	// Password reset

	/**
	 * Requests the password reset for the {@link Account} identified by the given username and email address.
	 *
	 * @param payload must not be {@literal null}.
	 * @param errors must not be {@literal null}.
	 * @return will never be {@literal null}.
	 * @since 1.4
	 */
	// @PostMapping("/password/reset")
	// Temporarily deactivated for the release because of an open vulnerability.
	HttpEntity<?> requestPasswordReset(@Valid @RequestBody PasswordResetRequest request, Errors errors) {

		Supplier<HttpEntity<?>> errorResponse = () -> ResponseEntity.badRequest()
				.body(I18nedMessage.of("PasswordReset.invalidData"));

		return MappedPayloads.of(request, errors)
				.flatMap(it -> accounts.requestPasswordReset(it.toEmail(), it.getUsername()))
				.onErrors(errorResponse).onAbsence(errorResponse)
				.concludeIfValid(token -> {

					var controller = on(UserController.class);
					var resetPasswordLink = MvcLink.of(controller.resetPassword(token.getToken(), null, null),
							UserLinkRelations.RESET_PASSWORD);

					return ResponseEntity.ok(new RepresentationModel<>().add(resetPasswordLink));
				});
	}

	/**
	 * Resets the password for the {@link Account} identified by the given username and date of birth.
	 *
	 * @param token must not be {@literal null}.
	 * @param payload must not be {@literal null}.
	 * @param errors must not be {@literal null}.
	 * @return will never be {@literal null}.
	 * @since 1.4
	 */
	@PutMapping(UserLinkRelations.PASSWORD_RESET_URI_TEMPLATE)
	HttpEntity<?> resetPassword(@PathVariable UUID token,
			@Valid @RequestBody PasswordResetInput payload,
			Errors errors) {

		return MappedPayloads.of(payload, errors)
				.alwaysMap(PasswordResetInput::validate)
				.map(it -> representations.from(it, token))
				.flatMap(trackedPeople::resetPassword)
				.onAbsence(() -> ResponseEntity.badRequest().body("Invalid or expired password request token!"))
				.onValidGet(() -> {

					var loginLink = MvcLink.of(on(AuthenticationController.class).login(null, null),
							AuthenticationLinkRelations.LOGIN);

					return ResponseEntity.ok(new RepresentationModel<>().add(loginLink));
				});
	}

	@Component
	@RequiredArgsConstructor
	static class PasswordResetProcessor implements RepresentationModelProcessor<QuaranoApiRoot> {

		private final AuthenticationManager authentication;

		/*
		 * (non-Javadoc)
		 * @see org.springframework.hateoas.server.RepresentationModelProcessor#process(org.springframework.hateoas.RepresentationModel)
		 */
		@Override
		@SuppressWarnings("null")
		public QuaranoApiRoot process(QuaranoApiRoot model) {

			var controller = on(UserController.class);

			return model;
			// Temporarily deactivated for the release because of an open vulnerability.
			// return authentication.isAnonymousOrHasRoleMatching(RoleType::isHuman)
			// ? model.add(MvcLink.of(controller.requestPasswordReset(null, null), UserLinkRelations.RESET_PASSWORD))
			// : model;
		};
	}
}
