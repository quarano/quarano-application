package quarano.auth.jwt;

import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import quarano.auth.Account;
import quarano.auth.AccountRepository;
import quarano.auth.NotAuthorizedException;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class AuthenticationService {

	private AccountRepository accountRepository;
	private JwtTokenCreationService jwtTokenService;
	private PasswordEncoder passwordEncoder;

	public Try<JwtTokenResponse> generateJWTToken(String username, String password) {

		return Try.ofSupplier(() -> lookupAccountFor(username)) //
				.filter(it -> passwordEncoder.matches(password, it.getPassword()), () -> new NotAuthorizedException()) //
				.map(jwtTokenService::generateToken) //
				.map(JwtTokenResponse::new);
	}

	private Account lookupAccountFor(String username) {

		return accountRepository.findOneByUsername(username.trim())
				.orElseThrow(() -> new EntityNotFoundException("No user found based on this token"));
	}
}
