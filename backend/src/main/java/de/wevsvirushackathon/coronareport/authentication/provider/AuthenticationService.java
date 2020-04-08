package de.wevsvirushackathon.coronareport.authentication.provider;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.wevsvirushackathon.coronareport.authentication.Role;
import de.wevsvirushackathon.coronareport.client.Client;
import de.wevsvirushackathon.coronareport.client.ClientRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private AccountRepository accountRepository;
    private JwtTokenCreationService jwtTokenService;
    private PasswordEncoder passwordEncoder;
    private ClientRepository clientRepository;


    public JWTTokenResponse generateJWTToken(String username, String password) {
    	
        return accountRepository.findOneByUsername(username)
                .filter(account ->  passwordEncoder.matches(password, account.getPassword()))
                .map(account ->  new JWTTokenResponse(generateToken(username, account.getRoles())))
                .orElseThrow(() ->  new EntityNotFoundException("Account not found"));
    }


	private String generateToken(String username, List<Role> roles) {
		
    	// get id of referenced client if one exists (HD-Accounts do not have referenced client data)
    	Optional<Client> client = clientRepository.findClientByAccountName(username);
		
		return jwtTokenService.generateToken(username, roles, client);
		
	}
    
 
    
}