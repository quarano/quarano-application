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
import quarano.core.web.QuaranoHttpHeaders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.MvcLink;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
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
class StaffAccountRepresentations implements AccountRepresentations {

	private final @NonNull MapperWrapper mapper;
	private final @NonNull RoleRepository roles;
	private final @NonNull TokenGenerator generator;

	StaffAccountSummaryDto toSummary(Account account) {
		return mapper.map(account, new StaffAccountSummaryDto().setAccountId(account.getId().toString()));
	}

	Account from(StaffAccountUpdateInputDto payload, Account existing) {
		return mapper.map(payload, existing);
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.account.web.AccountRepresentations#toTokenResponse(quarano.account.Account)
	 */
	@Override
	public HttpEntity<?> toTokenResponse(Account account) {

		return ResponseEntity.ok()
				.header(QuaranoHttpHeaders.AUTH_TOKEN, generator.generateTokenFor(account))
				.body(new AccountRepresentation(account));
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

			var controller = on(StaffAccountController.class);
			var identifier = AccountIdentifier.of(UUID.fromString(accountId));

			return super.getLinks()
					.and(MvcLink.of(controller.getStaffAccount(identifier, null), IanaLinkRelations.SELF))
					.and(MvcLink.of(controller.deleteStaffAccounts(identifier, null), StaffAccountLinkRelations.DELETE))
					.and(MvcLink.of(controller.putStaffAccountPassword(identifier, null, null, null),
							StaffAccountLinkRelations.RESET_PASSWORD));
		}
	}

	@Data
	@Getter
	static class StaffAccountCreateInputDto {

		private @Pattern(regexp = Strings.NAMES) @NotBlank String firstName, lastName;
		private @NotBlank String password, passwordConfirm; // password rules are checked in Entity
		private @UserName @NotBlank String username;
		private @NotBlank @Email String email;
		private List<String> roles = new ArrayList<>();

		StaffAccountCreateInputDto validate(Errors errors, AccountService accounts) {

			if (!Objects.nullSafeEquals(password, passwordConfirm)) {
				errors.rejectValue("passwordConfirm", "NonMatching.password");
			}

			validateUsername(errors, this.username, accounts);

			return this;
		}
	}

	@Getter
	@Setter
	static class StaffAccountUpdateInputDto {

		private @Pattern(regexp = Strings.NAMES) @NotBlank String firstName, lastName;
		private @UserName @NotBlank String username;
		private @NotBlank @Email String email;
		private List<String> roles = new ArrayList<>();

		StaffAccountUpdateInputDto validate(Errors errors, Account existing, AccountService accounts) {

			// validate username only if it has changed
			if (!existing.getUsername().equals(this.username)) {
				validateUsername(errors, this.username, accounts);
			}

			return this;
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
