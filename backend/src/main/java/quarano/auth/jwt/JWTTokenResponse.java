package quarano.auth.jwt;

public class JWTTokenResponse {

	private String token;

	public JWTTokenResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
