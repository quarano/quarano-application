package quarano.auth.jwt;

import lombok.Data;

@Data
public class AuthenticationRequest {

	private String username;
	private String password;
}
