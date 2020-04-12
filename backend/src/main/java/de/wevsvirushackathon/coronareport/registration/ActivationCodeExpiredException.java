package de.wevsvirushackathon.coronareport.registration;

public class ActivationCodeExpiredException extends Exception {
	
	private static final long serialVersionUID = -6646142990200147897L;
	
	private String message;
	
	ActivationCodeExpiredException(String message){
		super(message);
	}
}
