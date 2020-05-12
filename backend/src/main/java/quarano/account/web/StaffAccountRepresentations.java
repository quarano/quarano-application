package quarano.account.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import io.jsonwebtoken.lang.Objects;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
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
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Patrick Otto
 */
@Component
@RequiredArgsConstructor
class StaffAccountRepresentations {

	private final @NonNull MapperWrapper mapper;
	private final @NonNull RoleRepository roles;

	StaffAccountSummaryDto toSummary(Account account) {

		var dto = new StaffAccountSummaryDto();
		dto = mapper.map(account, dto);
		dto.accountId = account.getId().toString();
		dto.setRoles(account.getRoles().stream() //
				.map(x -> x.getRoleType().getCode()) //
				.collect(Collectors.toList()));

		return dto;
	}
	
	
	Account from(Account existing, @Valid StaffAccountUpdateInputDto payload) {

		var mappedAccount = mapper.map(payload, existing);
		
		return mappedAccount;
	}
	
	
	Account from(Account existing, @Valid StaffAccountCreateInputDto payload) {

		var mappedAccount = mapper.map(payload, existing);

		return mappedAccount;
	}

	@Relation(collectionRelation = "accounts")
	@Accessors(chain = true)
	@RequiredArgsConstructor(staticName = "of")
	static class StaffAccountSummaryDto extends RepresentationModel<StaffAccountSummaryDto> {

		private @Setter @Getter @NotBlank String firstName, lastName, username;
		private @Setter @Getter @NotBlank @Email String email;
		@JsonIgnore private @Setter @Getter String departmentId;
		private @Setter @Getter List<String> roles = new ArrayList<>();
		@Setter @Getter private String accountId;

		/*
		 * (non-Javadoc)
		 * @see org.springframework.hateoas.RepresentationModel#getLinks()
		 */
		@Override
		@SuppressWarnings("null")
		public Links getLinks() {

			var staffAccountController = on(StaffAccountController.class);

			return super.getLinks().and(Links.of( //
					Link.of(fromMethodCall(staffAccountController.getStaffAccount(AccountIdentifier.of(UUID.fromString(accountId)), null)).toUriString(),
							IanaLinkRelations.SELF),
					Link.of(fromMethodCall(staffAccountController.deleteStaffAccounts(AccountIdentifier.of(UUID.fromString(accountId)), null)).toUriString(),
							StaffAccountLinkRelations.DELETE)));
		}
	}

	@RequiredArgsConstructor(staticName = "of")
	static class StaffAccountCreateInputDto {

		private @Setter @Getter @Pattern(regexp = Strings.NAMES) @NotBlank String firstName, lastName;
		private @Setter @Getter @NotBlank String password, passwordConfirm; // password rules are checked in Entity
		private @Setter @Getter @UserName @NotBlank String username;
		private @Setter @Getter @NotBlank @Email String email;
		private @Setter @Getter List<String> roles = new ArrayList<>();

		Errors validate(Errors errors, AccountService accounts) {

			if (!Objects.nullSafeEquals(password, passwordConfirm)) {
				errors.rejectValue("passwordConfirm", "NonMatching.password");
			}
			
			validateUsername(errors, this.username, accounts);			

			return errors;
		}
	}
	
	@RequiredArgsConstructor(staticName = "of")
	static class StaffAccountUpdateInputDto{


		private @Setter @Getter @Pattern(regexp = Strings.NAMES) @NotBlank String firstName, lastName;
		private @Setter @Getter @UserName @NotBlank String username;
		private @Setter @Getter @NotBlank @Email String email;
		private @Setter @Getter List<String> roles = new ArrayList<>();

		Errors validate(Errors errors, Account existing, AccountService accounts) {
			
			// validate username only if it has changed
			if(!existing.getUsername().equals(this.username)) {
				validateUsername(errors, this.username, accounts);	
			}

			return errors;
		}
	}	

	
	static void validateUsername(Errors errors, String username, AccountService accounts) {
		if(!accounts.isUsernameAvailable(username)) {
			errors.rejectValue("username", "UserNameNotAvailable");
		}
		
		if(!accounts.isValidUsername(username)) {
			errors.rejectValue("username", "InvalidUserName");
		}
	}
}
