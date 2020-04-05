package de.wevsvirushackathon.coronareport.authentication.provider;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private AccountRepository accountRepository;
    private JwtTokenCreationService jwtTokenService;
    private PasswordEncoder passwordEncoder;

    public AuthenticationService(AccountRepository accountRepository, JwtTokenCreationService jwtTokenService, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public JWTTokenResponse generateJWTToken(String username, String password) {
        return accountRepository.findOneByUsername(username)
                .filter(account ->  passwordEncoder.matches(password, account.getPassword()))
                .map(account -> new JWTTokenResponse(jwtTokenService.generateToken(username, account.getRoles())))
                .orElseThrow(() ->  new EntityNotFoundException("Account not found"));
    }
    
    
}