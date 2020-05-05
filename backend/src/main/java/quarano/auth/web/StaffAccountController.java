package quarano.auth.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.Account;
import quarano.auth.Account.AccountIdentifier;
import quarano.auth.AccountRegistrationException;
import quarano.auth.AccountService;
import quarano.auth.ActivationCodeService;
import quarano.core.web.ErrorsDto;
import quarano.core.web.MapperWrapper;
import quarano.department.TrackedCaseRepository;

import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class StaffAccountController {

	private final @NonNull MapperWrapper mapper;
	private final @NonNull AccountService accounts;
	private final @NonNull ActivationCodeService activationCodes;
	private final @NonNull MessageSourceAccessor messages;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull StaffAccountRepresentations representations;

	@PostMapping("/api/hd/accounts")
	public HttpEntity<?> addStaffAccount(@Valid @RequestBody StaffAccountDto accountDto, Errors errors){
		ErrorsDto dto = ErrorsDto.of(errors, messages);
		
		
		//var storedAccount = accounts.addStaffAccount(representations.from(accountDto));
		
		return ResponseEntity.ok(representations.toRepresentation(storedAccount));
	}
	
	
	@GetMapping("/api/hd/accounts")
	public Stream<?> getStaffAccounts(@LoggedIn Account admin){

		var departmentAccounts = accounts.findStaffAccountsOfDepartmentOrderedByLastNameASC(admin.getDepartmentId());

		return departmentAccounts.stream() //
				.map(it -> representations.toRepresentation(it));
	}
	
	
	@GetMapping("/api/hd/accounts/{accountId}")
	public ResponseEntity<?> getStaffAccount(@PathVariable AccountIdentifier accountId, @LoggedIn Account admin) {
		
		var adminDepartmentId = admin.getDepartmentId();
		
		return ResponseEntity.of(accounts.findById(accountId) //
				.filter(it -> it.belongsTo(adminDepartmentId)) //
				.map(it -> representations.toRepresentation(it)));
	}	
	
	
	@DeleteMapping("/api/hd/accounts/{accountId}")
	public HttpEntity<?> deleteStaffAccounts(@PathVariable AccountIdentifier accountId, @LoggedIn Account admin){
		
		Optional<Account> account = accounts.findById(accountId);
		
		if(account.isPresent())
		{
			accounts.deleteAccount(accountId);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build();
		}
		else {
			return ResponseEntity.badRequest().build();
		}

	}	
	
	private ErrorsDto map(ErrorsDto errors, AccountRegistrationException o_O) {

		switch (o_O.getProblem()) {
			case INVALID_BIRTHDAY:
				return errors.rejectField("dateOfBirth", "Invalid", o_O.getMessage());
			case INVALID_USERNAME:
				return errors.rejectField("username", "Invalid", o_O.getMessage());
			default:
				return errors;
		}
	}

}
	

