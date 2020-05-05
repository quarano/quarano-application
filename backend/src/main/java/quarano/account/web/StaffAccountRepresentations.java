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
import quarano.core.EmailAddress;
import quarano.core.web.MapperWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
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
	private final MessageSourceAccessor messages;

	StaffAccountSummaryDto toSummary(Account account) {

		var dto = new StaffAccountSummaryDto();
		dto = mapper.map(account, dto);
		dto.accountId = account.getId().toString();
		dto.setRoles(account.getRoles().stream() //
				.map(x -> x.getRoleType().getCode()) //
				.collect(Collectors.toList()));

		return dto;
	}

	@Accessors(chain = true)
	@RequiredArgsConstructor(staticName = "of")
	static class StaffAccountSummaryDto extends RepresentationModel<StaffAccountSummaryDto> {

		private @Setter @Getter @NotBlank String firstName, lastName, username;
		private @Setter @Getter @NotBlank @Pattern(regexp = EmailAddress.PATTERN) String email;
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
	static class StaffAccountInputDto extends RepresentationModel<StaffAccountInputDto> {

		private @Setter @Getter @NotBlank String firstName, lastName, password, username, passwordConfirm;
		private @Setter @Getter @NotBlank @Pattern(regexp = EmailAddress.PATTERN) String email;
		@JsonIgnore private @Setter @Getter String departmentId;
		private @Setter @Getter String unencryptedPassword;
		private @Setter @Getter List<String> roles = new ArrayList<>();
		@Setter @Getter private String accountId;

		Errors validate(Errors errors) {

			if (!Objects.nullSafeEquals(password, passwordConfirm)) {
				errors.rejectValue("passwordConfirm", "NonMatching.password");
			}

			return errors;
		}

	}

}
