package quarano.registration;

public class InvalidAuthentificationDataException extends Exception {
	
	private static final long serialVersionUID = 5308644966382607572L;
	
	InvalidAuthentificationDataException(String message){
		super(message);
	}
}
