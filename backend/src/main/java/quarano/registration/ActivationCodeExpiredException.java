package quarano.registration;

public class ActivationCodeExpiredException extends Exception {
	
	private static final long serialVersionUID = -6646142990200147897L;
	
	ActivationCodeExpiredException(String message){
		super(message);
	}
}
