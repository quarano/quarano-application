package quarano.auth.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import quarano.auth.Account;
import quarano.auth.Account.AccountIdentifier;
import quarano.tracking.EmailAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffAccountDto {

	private @NotBlank String firstName, lastName;
	private @NotBlank @Pattern(regexp = EmailAddress.PATTERN) String email;
	private String departmentId;
	private List<String> roles = new ArrayList<>();
	@JsonIgnore
	private AccountIdentifier accountId;

	
	@JsonProperty("_links")
	public Map<String, Object> getLinks() {
		

		var staffAccountController = on(StaffAccountController.class);

		var deleteUri = fromMethodCall(staffAccountController.deleteStaffAccounts(accountId, null)).toUriString();
		var detailsUri = fromMethodCall(staffAccountController.getStaffAccount(accountId, null)).toUriString();

		return Map.of(//
					"details", Map.of("href", detailsUri), //
					"delete", Map.of("href", deleteUri));
	}

	public static StaffAccountDto of(Account account) {
		return new StaffAccountDto();
	}
}
