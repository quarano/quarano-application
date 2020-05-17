package quarano.account.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import io.jsonwebtoken.lang.Objects;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import quarano.account.Account;
import quarano.account.Account.AccountIdentifier;
import quarano.account.AccountService;
import quarano.account.RoleRepository;
import quarano.core.validation.Email;
import quarano.core.validation.Strings;
import quarano.core.validation.UserName;
import quarano.core.web.MapperWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Patrick Otto
 * @author Felix Schultze
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class StaffAccountRepresentations {

	private final @NonNull MapperWrapper mapper;
	private final @NonNull RoleRepository roles;

	StaffAccountSummaryDto toSummary(Account account) {
		return mapper.map(account, new StaffAccountSummaryDto().setAccountId(account.getId().toString()));
	}

	Account from(StaffAccountUpdateInputDto payload, Account existing) {
		return mapper.map(payload, existing);
	}

	@Relation(collectionRelation = "accounts")
	@Getter
	@Setter
	@RequiredArgsConstructor(staticName = "of")
	static class StaffAccountSummaryDto extends RepresentationModel<StaffAccountSummaryDto> {

		private @NotBlank String firstName, lastName, username;
		private @NotBlank @Email String email;
		private @JsonIgnore String departmentId;
		private String accountId;
		private List<String> roles = new ArrayList<>();

		/*
		 * (non-Javadoc)
		 * @see org.springframework.hateoas.RepresentationModel#getLinks()
		 */
		@Override
		@SuppressWarnings("null")
		public Links getLinks() {

			var staffAccountController = on(StaffAccountController.class);

			return super.getLinks().and(Links.of( //
					Link.of(fromMethodCall(
							staffAccountController.getStaffAccount(AccountIdentifier.of(UUID.fromString(accountId)), null))
									.toUriString(),
							IanaLinkRelations.SELF),
					Link.of(fromMethodCall(
							staffAccountController.deleteStaffAccounts(AccountIdentifier.of(UUID.fromString(accountId)), null))
									.toUriString(),
							StaffAccountLinkRelations.DELETE)));
		}
	}

	@Data
	@Getter(onMethod = @__(@Nullable))
	@RequiredArgsConstructor(staticName = "of")
	static class StaffAccountCreateInputDto {

		private @Pattern(regexp = Strings.NAMES) @NotBlank String firstName, lastName;
		private @NotBlank String password, passwordConfirm; // password rules are checked in Entity
		private @UserName @NotBlank String username;
		private @NotBlank @Email String email;
		private List<String> roles = new ArrayList<>();

		Errors validate(Errors errors, AccountService accounts) {

			if (!Objects.nullSafeEquals(password, passwordConfirm)) {
				errors.rejectValue("passwordConfirm", "NonMatching.password");
			}

			validateUsername(errors, this.username, accounts);

			return errors;
		}
	}

	@Getter
	@Setter
	@RequiredArgsConstructor(staticName = "of")
	static class StaffAccountUpdateInputDto {

		private @Pattern(regexp = Strings.NAMES) @NotBlank String firstName, lastName;
		private @UserName @NotBlank String username;
		private @NotBlank @Email String email;
		private List<String> roles = new ArrayList<>();

		Errors validate(Errors errors, Account existing, AccountService accounts) {

			// validate username only if it has changed
			if (!existing.getUsername().equals(this.username)) {
				validateUsername(errors, this.username, accounts);
			}

			return errors;
		}
	}

	static void validateUsername(Errors errors, String username, AccountService accounts) {
		if (!accounts.isUsernameAvailable(username)) {
			errors.rejectValue("username", "UserNameNotAvailable");
		}

		if (!accounts.isValidUsername(username)) {
			errors.rejectValue("username", "InvalidUserName");
		}
	}
}
