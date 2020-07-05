package quarano.security.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.AccountService;
import quarano.account.Password.UnencryptedPassword;
import quarano.core.web.QuaranoHttpHeaders;
import quarano.department.TokenGenerator;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPersonRepository;
import quarano.user.web.UserController;

import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.server.mvc.MvcLink;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
	private final @NonNull TokenGenerator generator;

	@PostMapping({ "/login", "/api/login" })
	HttpEntity<?> login(@RequestBody AuthenticationRequest request) {

		UnencryptedPassword password = UnencryptedPassword.of(request.getPassword());

		return Try.ofSupplier(() -> lookupAccountFor(request.getUsername()))
				.filter(it -> accounts.matches(password, it.getPassword()),
						() -> new AccessDeniedException("Authentication failed!"))
				.filter(this::hasOpenCaseForAccount, () -> new AccessDeniedException("Case already closed!"))
				.<HttpEntity<?>> map(this::toTokenResponse)
				.recover(EmptyResultDataAccessException.class, it -> toUnauthorized(it.getMessage()))
				.get();
	}

	private boolean hasOpenCaseForAccount(Account it) {

		return !it.isTrackedPerson() || people.findByAccount(it)
				.flatMap(cases::findByTrackedPerson)
				.filter(TrackedCase::isOpen)
				.isPresent();
	}

	private HttpEntity<?> toTokenResponse(Account it) {

		var token = generator.generateTokenFor(it);

		return !it.getPassword().isExpired()
				? QuaranoHttpHeaders.toTokenResponse(token)
				: ResponseEntity.ok()
						.header(QuaranoHttpHeaders.AUTH_TOKEN, token)
						.body(createBodyWithChangePasswordLink());
	}

	private Account lookupAccountFor(String username) {

		return accounts.findByUsername(username.trim())
				.orElseThrow(() -> new EmptyResultDataAccessException("No user found based on this token", 1));
	}

	@SuppressWarnings("null")
	private static Map<String, Object> createBodyWithChangePasswordLink() {

		var target = on(UserController.class).putPassword(null, null, null);

		var links = Links.of(MvcLink.of(target, IanaLinkRelations.NEXT))
				.and(MvcLink.of(target, UserController.CHANGE_PASSWORD));

		return Map.of("_links", links);
	}

	private static HttpEntity<?> toUnauthorized(String message) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
	}

	@Data
	@AllArgsConstructor(access = AccessLevel.PACKAGE)
	static class AuthenticationRequest {

		private String username;
		private String password;
	}
}
