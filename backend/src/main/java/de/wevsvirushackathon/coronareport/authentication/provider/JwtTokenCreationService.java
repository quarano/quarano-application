package de.wevsvirushackathon.coronareport.authentication.provider;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import de.wevsvirushackathon.coronareport.authentication.Role;
import de.wevsvirushackathon.coronareport.client.Client;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * A class to create JWT token holding quarano specific user information
 * @author Patrick Otto
 *
 */
@Component
public class JwtTokenCreationService {

    private String secret;

    private Long expiration;
    private String roleClaimAttribute;
    private String clientIdClaimAttribute;

    public JwtTokenCreationService(@Value("${jwt.authentication"
    		+ ".secret}") String secret,
                           @Value("${jwt.provider.expiration}") Long expiration,
                           @Value("${jwt.authentication.claim.role}") String roleClaimAttribute,
                           @Value("${jwt.authentication.claim.clientid}") String clientIdClaimAttribute
                           ) {
        this.secret = secret;
        this.expiration = expiration;
        this.roleClaimAttribute = roleClaimAttribute;
        this.clientIdClaimAttribute = clientIdClaimAttribute;
    }

    public String generateToken(String username, List<Role> roles, Optional<Client> client ) {
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate);
        
        // map roles to a list of rolenames
        Map<String, Object> claims = new HashMap<>();
        claims.put(roleClaimAttribute, roles.stream().map(Role::toString).collect(Collectors.toList()));
        client.ifPresent(c -> claims.put(clientIdClaimAttribute, c.getClientId()));
       

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }
}

