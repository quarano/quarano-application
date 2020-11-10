package quarano.account.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import quarano.account.Account;
import quarano.account.Account.AccountIdentifier;
import quarano.account.AccountService;
import quarano.account.Department;
import quarano.account.Password.UnencryptedPassword;
import quarano.account.RoleType;
import quarano.account.web.StaffAccountRepresentations.StaffAccountCreateInputDto;
import quarano.account.web.StaffAccountRepresentations.StaffAccountUpdateInputDto;
import quarano.core.EmailAddress;
import quarano.core.web.LoggedIn;
import quarano.core.web.MappedPayloads;

import java.net.URI;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class StaffAccountController {

	private final @NonNull AccountService accounts;
	private final @NonNull StaffAccountRepresentations representations;

	@PostMapping("/hd/accounts")
	HttpEntity<?> addStaffAccount(@Valid @RequestBody StaffAccountCreateInputDto payload,
			Errors errors,
			@LoggedIn Department department) {

		return MappedPayloads.of(payload, errors)
				.alwaysMap((it, nested) -> it.validate(nested, accounts))
				.map(it -> {

					return accounts.createStaffAccount(it.getUsername(),
							UnencryptedPassword.of(it.getPassword()),
							it.getFirstName(),
							it.getLastName(),
							EmailAddress.of(it.getEmail()),
							department.getId(),
							payload.getRoles().stream()
									.map(RoleType::valueOf)
									.collect(Collectors.toList()));

				}).concludeIfValid(it -> {

					var location = on(StaffAccountController.class).getStaffAccount(it.getId(), it);

					return ResponseEntity
							.created(URI.create(fromMethodCall(location).toUriString()))
							.body(representations.toSummary(it));
				});
	}

	@PutMapping("/hd/accounts/{accountId}/password")
	HttpEntity<?> putStaffAccountPassword(@PathVariable AccountIdentifier accountId,
			@Valid @RequestBody NewPassword payload, Errors errors, @LoggedIn Account admin) {

		return MappedPayloads.of(accounts.findById(accountId), errors) //
				.notFoundIf(it -> it.isTrackedPerson()) //
				.notFoundIf(it -> !admin.belongsTo(it.getDepartmentId())) //
				.peek((__, err) -> payload.validate(err)) //
				.concludeIfValid(it -> {
					accounts.changePassword(UnencryptedPassword.of(payload.password), it);
					return ResponseEntity.noContent().build();
				});
	}

	@PutMapping("/hd/accounts/{accountId}")
	HttpEntity<?> updateStaffAccount(@PathVariable AccountIdentifier accountId,
			@Validated @RequestBody StaffAccountUpdateInputDto payload,
			Errors errors,
			@LoggedIn Department department) {

		var existing = accounts.findById(accountId).orElse(null);

		return MappedPayloads.of(payload, errors)
				.notFoundIf(existing == null)
				.alwaysMap((it, foo) -> it.validate(foo, existing, accounts))
				.map(dto -> {

					return accounts.findById(accountId)
							.map(it -> representations.from(dto, it))
							.map(it -> accounts.saveStaffAccount(it))
							.map(it -> representations.toSummary(it));

				}).concludeIfValid(ResponseEntity::of);
	}

	@GetMapping(path = "/hd/accounts", produces = MediaTypes.HAL_JSON_VALUE)
	RepresentationModel<?> getStaffAccounts(@LoggedIn Account admin) {

		var departmentAccounts = accounts.findStaffAccountsFor(admin.getDepartmentId());

		return departmentAccounts.stream()
				.map(it -> representations.toSummary(it))
				.collect(Collectors.collectingAndThen(Collectors.toUnmodifiableList(), CollectionModel::of));
	}

	@GetMapping(path = "/hd/accounts/{accountId}", produces = MediaTypes.HAL_JSON_VALUE)
	ResponseEntity<?> getStaffAccount(@PathVariable AccountIdentifier accountId, @LoggedIn Account admin) {

		var adminDepartmentId = admin.getDepartmentId();

		return ResponseEntity.of(accounts.findById(accountId)
				.filter(it -> it.belongsTo(adminDepartmentId))
				.map(it -> representations.toSummary(it)));
	}

	@DeleteMapping("/hd/accounts/{accountId}")
	HttpEntity<?> deleteStaffAccounts(@PathVariable AccountIdentifier accountId, @LoggedIn Account admin) {

		return accounts.findById(accountId)
				.map(it -> {

					accounts.deleteAccount(it.getId());

					return ResponseEntity.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.build();

				}).orElseGet(() -> {

					return ResponseEntity.badRequest().build();
				});
	}

	@Value
	static class NewPassword {

		/**
		 * The new password to set.
		 */
		@NotBlank String password;

		/**
		 * The new password repeated for verification.
		 */
		@NotBlank String passwordConfirm;

		NewPassword validate(Errors errors) {

			if (!password.equals(passwordConfirm)) {
				errors.rejectValue("password", "NonMatching.password");
				errors.rejectValue("passwordConfirm", "NonMatching.password");
			}

			return this;
		}
	}
}
