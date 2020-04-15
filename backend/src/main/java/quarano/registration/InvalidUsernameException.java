package quarano.registration;

public class InvalidUsernameException extends RuntimeException {

	private static final long serialVersionUID = -2465635229074867677L;

	public InvalidUsernameException(String message) {
		super(message);
	}

}
