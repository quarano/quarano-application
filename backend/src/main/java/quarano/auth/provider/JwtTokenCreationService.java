package quarano.auth.provider;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import quarano.auth.Role;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

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

    public String generateToken(String username, List<Role> roles, TrackedPersonIdentifier trackedPersonId ) {
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate);
        
        // map roles to a list of rolenames
        Map<String, Object> claims = new HashMap<>();
        claims.put(roleClaimAttribute, roles.stream().map(Role::toString).collect(Collectors.toList()));
        if(trackedPersonId != null) {
        	claims.put(clientIdClaimAttribute, trackedPersonId);
        }

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

