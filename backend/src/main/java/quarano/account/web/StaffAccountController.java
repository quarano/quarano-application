package quarano.account.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.Account.AccountIdentifier;
import quarano.account.AccountService;
import quarano.account.Department;
import quarano.account.Password.UnencryptedPassword;
import quarano.account.RoleType;
import quarano.account.web.StaffAccountRepresentations.StaffAccountInputDto;
import quarano.core.EmailAddress;
import quarano.core.web.ErrorsDto;
import quarano.core.web.LoggedIn;
import quarano.core.web.MapperWrapper;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.MediaTypes;
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
	private final @NonNull StaffAccountRepresentations representations;
	private final MessageSourceAccessor messages;	
	private final @NonNull MessageSourceAccessor accessor;	
	

	@PostMapping("/api/hd/accounts")
	public HttpEntity<?> addStaffAccount(@Valid @RequestBody StaffAccountInputDto payload, @LoggedIn Department department, Errors errors) {


		if (payload.validate(errors).hasErrors()) {
			return ErrorsDto.of(errors, messages).toBadRequest();
		}		
		
		var storedAccount = accounts.createStaffAccount(payload.getUsername(),  //
				UnencryptedPassword.of(payload.getPassword()), //
				payload.getFirstName(), // 
				payload.getLastName(),  //
				EmailAddress.of(payload.getEmail()), //
				department.getId(), //
				payload.getRoles().stream() //
					.map(it -> RoleType.valueOf(it)) //
					.collect(Collectors.toList()));		
		
		var location = on(StaffAccountController.class).getStaffAccount(storedAccount.getId(), storedAccount);

		return ResponseEntity //
				.created(URI.create(fromMethodCall(location).toUriString())) //
				.body(representations.toSummary(storedAccount));		
	}
	

//	@PutMapping("/api/hd/accounts/{accountId}")
//	public HttpEntity<?> updateStaffAccount(@PathVariable AccountIdentifier accountId, @Valid @RequestBody StaffAccountInputDto payload, @LoggedIn Department department, Errors errors) {
//
//
//		if (payload.validate(errors).hasErrors()) {
//			return ErrorsDto.of(errors, messages).toBadRequest();
//		}		
//		
//		Account newAccount = new Account();
//		Optional<Account> account = accounts.findById(accountId);
//		
//		if(account.isPresent()) {
//			account = mapper.map(payload, account);
//			
//			account.get().
//			
//			var storedAccount = accounts.saveAccount(payload.getUsername(),  //
//					UnencryptedPassword.of(payload.getPassword()), //
//					payload.getFirstName(), // 
//					payload.getLastName(),  //
//					EmailAddress.of(payload.getEmail()), //
//					department.getId(), //
//					payload.getRoles().stream() //
//						.map(it -> RoleType.valueOf(it)) //
//						.collect(Collectors.toList()));					
//			
//		}
//		
//		
//
//		
//		var location = on(StaffAccountController.class).getStaffAccount(storedAccount.getId(), storedAccount);
//
//		return ResponseEntity //
//				.created(URI.create(fromMethodCall(location).toUriString())) //
//				.body(representations.toSummary(storedAccount));		
//	}	

	@GetMapping(path = "/api/hd/accounts", produces = MediaTypes.HAL_JSON_VALUE)
	public Stream<?> getStaffAccounts(@LoggedIn Account admin) {

		var departmentAccounts = accounts.findStaffAccountsFor(admin.getDepartmentId());

		return departmentAccounts.stream() //
				.map(it -> representations.toSummary(it));
	}

	@GetMapping(path = "/api/hd/accounts/{accountId}" , produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<?> getStaffAccount(@PathVariable AccountIdentifier accountId, @LoggedIn Account admin) {

		var adminDepartmentId = admin.getDepartmentId();

		return ResponseEntity.of(accounts.findById(accountId) //
				.filter(it -> it.belongsTo(adminDepartmentId)) //
				.map(it -> representations.toSummary(it)));
	}

	@DeleteMapping("/api/hd/accounts/{accountId}")
	public HttpEntity<?> deleteStaffAccounts(@PathVariable AccountIdentifier accountId, @LoggedIn Account admin) {

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
	
}
