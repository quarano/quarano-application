package de.wevsvirushackathon.coronareport.authentication.provider;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.wevsvirushackathon.coronareport.authentication.Account;
import de.wevsvirushackathon.coronareport.client.Client;
import de.wevsvirushackathon.coronareport.client.ClientRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private AccountRepository accountRepository;
    private JwtTokenCreationService jwtTokenService;
    private PasswordEncoder passwordEncoder;

    public JWTTokenResponse generateJWTToken(String username, String password) {
    	// lookup  account object from database and check if password matches
        return accountRepository.findOneByUsername(username)
                .filter(account ->  passwordEncoder.matches(password, account.getPassword()))
                .map(account ->  new JWTTokenResponse(generateTokenFrom(account)))
                .orElseThrow(() ->  new EntityNotFoundException("Account not found"));
    }


	private String generateTokenFrom(Account account) {
		
		return jwtTokenService.generateToken(account.getUsername(), account.getRoles(), account.getClientId());
		
	}
    
 
    
}