package quarano.security.web;

import static io.vavr.API.*;
import static quarano.security.web.AuthenticationController.ForbiddenException.*;
import static quarano.security.web.AuthenticationController.ForbiddenException.Reason.*;

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
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPersonRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import org.springframework.context.support.MessageSourceAccessor;
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
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@RestController
@RequestMapping
@RequiredArgsConstructor
class AuthenticationController {

	private final @NonNull AccountService accounts;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull TrackedPersonRepository people;
	private final @NonNull AccountRepresentations accountRepresentations;
	private final @NonNull MessageSourceAccessor messages;

	@PostMapping({ "/login", "/api/login" })
	HttpEntity<?> login(@RequestBody AuthenticationRequest request, HttpServletRequest httpRequest) {

		UnencryptedPassword password = UnencryptedPassword.of(request.getPassword());

		return Try.ofSupplier(() -> lookupAccountFor(request.getUsername()))
				.filter(it -> accounts.matches(password, it.getPassword()),
						() -> new AccessDeniedException("Authentication failed!"))
				.flatMap(this::isAccountEligibleForLogin)
				// set authentication to security context for a later use during the request (e.g. to get the current
				// authentication)
				.peek(it -> SecurityContextHolder.getContext()
						.setAuthentication(new PreAuthenticatedAuthenticationToken(it, null)))
				// sets the locale to TrackedPerson if none is set yet and saves this entity
				.peek(account -> people.findByAccount(account)
						.filter(it -> it.getLocale() == null)
						.map(it -> it.setLocale(new AcceptHeaderLocaleResolver().resolveLocale(httpRequest)))
						.ifPresent(people::save))
				.<HttpEntity<?>> map(accountRepresentations::toTokenResponse)
				.recover(AccessDeniedException.class,
						it -> toUnauthorized(messages.getMessage("authentication.trackedCase.failed")))
				.recover(ForbiddenException.class, it -> toForbidden(it.getMessage()))
				.get();
	}

	/**
	 * Verifies further conditions on the given {@link Account} to decide whether it is eligible for log in.
	 *
	 * @return A <{@link Try} with the given account or an exception describing the reason why the login was rejected.
	 */
	private Try<Account> isAccountEligibleForLogin(Account account) {

		if (!account.isTrackedPerson()) {
			return Try.success(account);
		}

		var tc = cases.findByAccount(account).orElse(null);

		return Match(tc).of(
				Case($(it -> it == null), __ -> reject(CASE_ABSENT)),
				Case($(it -> !it.isOpen()), __ -> reject(CASE_CLOSED)),
				Case($(it -> it.isExternal()), __ -> reject(EXTERNAL_ZIP)),
				Case($(), Try.success(account)));
	}

	private Account lookupAccountFor(String username) {

		return accounts.findByUsername(username.trim())
				.orElseThrow(() -> new AccessDeniedException("No user found based on this token"));
	}

	private static HttpEntity<?> toUnauthorized(String message) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
	}

	private HttpEntity<?> toForbidden(String key) {

		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(messages.getMessage("authentication.trackedCase." + key));
	}

	@Data
	@AllArgsConstructor(access = AccessLevel.PACKAGE)
	static class AuthenticationRequest {
		private @NotBlank String username, password;
	}

	static class ForbiddenException extends RuntimeException {

		private static final long serialVersionUID = 3751035061264462192L;

		static enum Reason {
			CASE_ABSENT, CASE_CLOSED, EXTERNAL_ZIP;
		}

		private ForbiddenException(Reason reason) {
			super(reason.name());
		}

		public static Try<Account> reject(Reason reason) {
			return Try.failure(new ForbiddenException(reason));
		}
	}
}
