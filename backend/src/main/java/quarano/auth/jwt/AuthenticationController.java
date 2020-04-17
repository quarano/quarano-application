package quarano.auth.jwt;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.NotAuthorizedException;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
class AuthenticationController {

	private final @NonNull AuthenticationService authenticationService;

	@PostMapping({ "/login", "/api/login" })
	HttpEntity<?> login(@RequestBody AuthenticationRequest request) {

		return authenticationService.generateJWTToken(request.getUsername(), request.getPassword())
				.<HttpEntity<?>> map(it -> new ResponseEntity<>(it, HttpStatus.OK))
				.recover(EntityNotFoundException.class, it -> toUnauthorized(it.getMessage())) //
				.recover(NotAuthorizedException.class, it -> toUnauthorized(it.getMessage())) //
				.get();
	}

	private static HttpEntity<?> toUnauthorized(String message) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
	}

	@Data
	static class AuthenticationRequest {

		private String username;
		private String password;
	}
}
