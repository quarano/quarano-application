package quarano.security.web;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import quarano.account.Account;
import quarano.account.AccountService;
import quarano.account.Password.UnencryptedPassword;
import quarano.department.TokenGenerator;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPersonRepository;

import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quarano.user.web.UserController;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;
import static quarano.core.web.QuaranoHttpHeaders.*;

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
				.filter(it -> {
					return !it.isTrackedPerson() || people.findByAccount(it)
							.flatMap(cases::findByTrackedPerson)
							.filter(TrackedCase::isOpen)
							.isPresent();

				}, () -> new AccessDeniedException("Case already closed!"))
				.map(it -> {
					var token = generator.generateTokenFor(it);

					if (it.getPassword().isExpired()) {
						return ResponseEntity.ok()
								.header(AUTH_TOKEN, token)
								.body(createBodyWithChangePasswordLink());
					}

					return toTokenResponse(token);
				}) //
				.recover(EmptyResultDataAccessException.class, it -> toUnauthorized(it.getMessage())) //
				.get();
	}

	private Map<String, Object> createBodyWithChangePasswordLink() {
		var userController = on(UserController.class);

		var linkToChangePassword = Link.of(fromMethodCall(userController.putPassword(null, null, null)).toUriString(),
				IanaLinkRelations.NEXT); //

		return Map.of("_links", Links.of(linkToChangePassword));
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

		private String username;
		private String password;
	}
}
