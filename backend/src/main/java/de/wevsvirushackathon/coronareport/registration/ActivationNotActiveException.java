package de.wevsvirushackathon.coronareport.registration;

public class ActivationNotActiveException extends Exception {
	
	private static final long serialVersionUID = 9159848892630282041L;
	
	private String message;
	
	ActivationNotActiveException(String message){
		super(message);
	}
}
