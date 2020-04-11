package de.wevsvirushackathon.coronareport.authentication.provider;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.wevsvirushackathon.coronareport.authentication.Account;
import de.wevsvirushackathon.coronareport.authentication.AccountRepository;
import de.wevsvirushackathon.coronareport.authentication.NotAuthorizedException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private AccountRepository accountRepository;
    private JwtTokenCreationService jwtTokenService;
    private PasswordEncoder passwordEncoder;

    public JWTTokenResponse generateJWTToken(String username, String password) {
    	
    	// lookup  account object from database and check if password matches
    	Optional<Account> userFromDB = accountRepository.findOneByUsername(username.trim())
    		.filter(account ->  passwordEncoder.matches(password, account.getPassword()));
    	
    	userFromDB.orElseThrow(() -> new NotAuthorizedException());	
                
        return userFromDB.map(account ->  new JWTTokenResponse(generateTokenFrom(account)))
               .orElseThrow(() -> new EntityNotFoundException("No user found based on this token"));
    }


	private String generateTokenFrom(Account account) {
		
		return jwtTokenService.generateToken(account.getUsername(), account.getRoles(), account.getClientId());
		
	}
    
 
    
}