package quarano.registration;

public class CodeNotFoundException extends Exception {

	private static final long serialVersionUID = 9159848892630282041L;
	
	private String message;
	
	CodeNotFoundException(String message){
		super(message);
	}

}
