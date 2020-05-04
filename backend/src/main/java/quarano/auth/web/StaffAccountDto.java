package quarano.auth.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import quarano.auth.Account;
import quarano.auth.Role;
import quarano.core.web.MapperWrapper;
import quarano.tracking.EmailAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffAccountDto {

	private @NotBlank String firstName, lastName;
	private @NotBlank @Pattern(regexp = EmailAddress.PATTERN) String email;
	private String departmentId;
	private List<Role> roles = new ArrayList<>();

	
	
	@JsonProperty("_links")
	public Map<String, Object> getLinks() {
		

		var staffAccountController = on(StaffAccountController.class);

		var deleteUri = fromMethodCall(staffAccountController.deleteStaffAccounts(null, null)).toUriString();
		var detailsUri = fromMethodCall(staffAccountController.getStaffAccount(null, null)).toUriString();

		return Map.of(//
					"details", Map.of("href", detailsUri), //
					"delete", Map.of("href", deleteUri));
	}

	public static StaffAccountDto of(Account account, MapperWrapper mapper) {
		var accountDto =  new StaffAccountDto();
		return mapper.map(account, accountDto);
	}
}
