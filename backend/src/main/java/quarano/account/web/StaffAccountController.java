package quarano.account.web;

import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.AccountService;
import quarano.account.Account.AccountIdentifier;
import quarano.core.web.ErrorsDto;
import quarano.core.web.LoggedIn;
import quarano.core.web.MapperWrapper;

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
	private final @NonNull MessageSourceAccessor messages;

	@PostMapping("/api/hd/accounts")
	public HttpEntity<?> addStaffAccount(@Valid @RequestBody StaffAccountDto accountDto, Errors errors) {
		ErrorsDto dto = ErrorsDto.of(errors, messages);

		var storedAccount = accounts.addStaffAccount(null);

		return ResponseEntity.ok(StaffAccountDto.of(storedAccount, mapper));
	}

	@GetMapping("/api/hd/accounts")
	public Stream<?> getStaffAccounts(@LoggedIn Account admin) {

		var departmentAccounts = accounts.findStaffAccountsFor(admin.getDepartmentId());

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
	public HttpEntity<?> deleteStaffAccounts(@PathVariable AccountIdentifier accountId, @LoggedIn Account admin) {

		Try<?> deletion = accounts.deleteAccount(accountId, admin);

		if (deletion.isSuccess()) {
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build();
		} else {
			return deletion.<HttpEntity<?>> map(__ -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build())
					.getOrElseGet(it -> ResponseEntity.badRequest().body(it.getMessage()));
		}

	}
}
