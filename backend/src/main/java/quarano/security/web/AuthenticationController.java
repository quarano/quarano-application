package quarano.security.web;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.AccountService;
import quarano.account.Password.UnencryptedPassword;
import quarano.account.web.AccountRepresentations;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPersonRepository;

import javax.validation.constraints.NotBlank;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
class AuthenticationController {

	private final @NonNull AccountService accounts;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull TrackedPersonRepository people;
	private final @NonNull AccountRepresentations accountRepresentations;

	@PostMapping({ "/login", "/api/login" })
	HttpEntity<?> login(@RequestBody AuthenticationRequest request) {

		UnencryptedPassword password = UnencryptedPassword.of(request.getPassword());

		return Try.ofSupplier(() -> lookupAccountFor(request.getUsername()))
				.filter(it -> accounts.matches(password, it.getPassword()),
						() -> new AccessDeniedException("Authentication failed!"))
				.filter(this::hasOpenCaseForAccount, () -> new AccessDeniedException("Case already closed!"))
				// set authentication to security context for a later use during the request (e.g. to get the current
				// authentication)
				.peek(it -> SecurityContextHolder.getContext()
						.setAuthentication(new PreAuthenticatedAuthenticationToken(it, null)))
				.<HttpEntity<?>> map(accountRepresentations::toTokenResponse)
				.recover(EmptyResultDataAccessException.class, it -> toUnauthorized(it.getMessage()))
				.get();
	}

	private boolean hasOpenCaseForAccount(Account account) {

		return !account.isTrackedPerson() || people.findByAccount(account)
				.flatMap(cases::findByTrackedPerson)
				.filter(TrackedCase::isOpen)
				.isPresent();
	}

	private Account lookupAccountFor(String username) {

		return accounts.findByUsername(username.trim())
				.orElseThrow(() -> new EmptyResultDataAccessException("No user found based on this token", 1));
	}

	private static HttpEntity<?> toUnauthorized(String message) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
	}

	@Data
	@AllArgsConstructor(access = AccessLevel.PACKAGE)
	static class AuthenticationRequest {
		private @NotBlank String username, password;
	}
}
