package de.wevsvirushackathon.coronareport.infrastructure.errorhandling;

import lombok.AllArgsConstructor;

public class InconsistentDataException extends Exception {
	public InconsistentDataException(String message) {
		super(message);
	}

}
