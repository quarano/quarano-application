package quarano.auth.web;

import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.Account;
import quarano.auth.Account.AccountIdentifier;
import quarano.auth.AccountRegistrationDetails;
import quarano.auth.AccountRegistrationException;
import quarano.auth.AccountService;
import quarano.auth.ActivationCodeException;
import quarano.auth.ActivationCodeService;
import quarano.core.web.ErrorsDto;
import quarano.core.web.MapperWrapper;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseRepository;

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
	private final @NonNull RegistrationRepresentations representations;

	@PostMapping("/api/hd/accounts")
	public HttpEntity<?> addStaffAccount(@Valid @RequestBody StaffAccountDto accountDto, Errors errors){
		ErrorsDto dto = ErrorsDto.of(errors, messages);
		
		var storedAccount = accounts.addStaffAccount(null);
		
		return ResponseEntity.ok(StaffAccountDto.of(storedAccount, mapper));
	}
	
	
	@GetMapping("/api/hd/accounts")
	public Stream<?> getStaffAccounts(@LoggedIn Account admin){

		var departmentAccounts = accounts.findStaffAccountsOfDepartmentOrderedByLastNameASC(admin.getDepartmentId());

		return departmentAccounts.stream() //
				.map(it -> StaffAccountDto.of(it, mapper));
	}
	
	
	@GetMapping("api/hd/accounts/{accountId}")
	public ResponseEntity<?> getStaffAccount(@PathVariable AccountIdentifier accountId, @LoggedIn Account admin) {
		
		var adminDepartmentId = admin.getDepartmentId();
		
		return ResponseEntity.of(accounts.findById(accountId) //
				.filter(it -> it.belongsTo(adminDepartmentId)) //
				.map(it -> StaffAccountDto.of(it, mapper)));
	}	
	
	
	@DeleteMapping("/api/hd/accounts/{accountId}")
	public HttpEntity<?> deleteStaffAccounts(@PathVariable AccountIdentifier accountId, @LoggedIn Account admin){

		Try<?> deletion = accounts.deleteAccount(accountId, admin);
		
		if(deletion.isSuccess())
		{
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build();
		}
		else {
			return deletion
					.<HttpEntity<?>> map(__ -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build())
					.getOrElseGet(it -> ResponseEntity.badRequest().body(it.getMessage()));
		}

	}	
	


	
	
//	@PostMapping("/api/registration")
	public HttpEntity<?> registerClient(@Valid @RequestBody AccountRegistrationDto registrationDto, Errors errors) {

		if (registrationDto.validate(errors).hasErrors()) {
			return ErrorsDto.of(errors, messages).toBadRequest();
		}

		var details = mapper.map(registrationDto, AccountRegistrationDetails.class);

		ErrorsDto dto = ErrorsDto.of(errors, messages);

		return accounts.createTrackedPersonAccount(details) //
				.<HttpEntity<?>> map(__ -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build())
				.recover(AccountRegistrationException.class, it -> map(dto, it).toBadRequest()) //
				.recover(ActivationCodeException.class, it -> dto //
						.rejectField("clientCode", "Invalid", it.getMessage()) //
						.toBadRequest())
				.getOrElseGet(it -> ResponseEntity.badRequest().body(it.getMessage()));
	}

//	@PutMapping("/api/hd/cases/{id}/registration")
	HttpEntity<?> createRegistration(@PathVariable TrackedCaseIdentifier id, @LoggedIn Account account) {

		var trackedCase = cases.findById(id).orElse(null);
		var departmentId = account.getDepartmentId();

		if (trackedCase == null || !trackedCase.belongsTo(departmentId)) {
			return ResponseEntity.notFound().build();
		}

		return activationCodes.createActivationCode(trackedCase.getTrackedPerson().getId(), departmentId) //
				.map(it -> representations.toRepresentation(it, trackedCase)) //
				.fold(it -> ResponseEntity.badRequest().body(it.getMessage()), //
						it -> ResponseEntity.ok(it));
	}

//	@GetMapping("/api/hd/cases/{id}/registration")
	HttpEntity<?> getRegistrationDetails(@PathVariable TrackedCaseIdentifier id, @LoggedIn Account account) {

		return cases.findById(id) //
				.filter(it -> it.belongsTo(account.getDepartmentId())) //
				.map(it -> {

					return activationCodes.getPendingActivationCode(it.getTrackedPerson().getId()) //
							.<HttpEntity<?>> map(code -> ResponseEntity.ok(representations.toRepresentation(code, it))) //
							.orElseGet(() -> ResponseEntity.ok(representations.toNoRegistration(it)));

				}).orElseGet(() -> ResponseEntity.notFound().build());
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
	

