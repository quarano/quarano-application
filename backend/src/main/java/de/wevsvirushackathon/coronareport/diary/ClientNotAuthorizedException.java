package de.wevsvirushackathon.coronareport.diary;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClientNotAuthorizedException extends Exception {

	private static final long serialVersionUID = -3225736346349533368L;
	
	private String clientCode;

	public ClientNotAuthorizedException(String clientCode, String message) {
		super(message);
	}

}
