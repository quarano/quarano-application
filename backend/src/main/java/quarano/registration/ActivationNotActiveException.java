package quarano.registration;

public class ActivationNotActiveException extends Exception {
	
	private static final long serialVersionUID = 9159848892630282041L;
	
	ActivationNotActiveException(String message){
		super(message);
	}
}
