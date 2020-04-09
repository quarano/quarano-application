package de.wevsvirushackathon.coronareport.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * An exception that indicates that there is no client for an account
 * @author Patrick Otto
 *
 */
@AllArgsConstructor
public class MissingClientException extends RuntimeException{

	private static final long serialVersionUID = -3488262976685592569L;
	
	@Getter @Setter
	private String username;
	
	public MissingClientException(String message, String username) {
		super(message);
		this.username = username;
	}
	

}
