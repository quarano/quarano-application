package de.wevsvirushackathon.coronareport.infrastructure.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * An exception which indicates that a header, body, url or header argument did not contain a valid value
 * @author Patrick Otto
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class InvalidArgumentException extends Exception {

	private static final long serialVersionUID = -5844107806228764791L;
		
	/**
	 * The name of the argument which contained the invalid value
	 */
	private String argumentName;
	/**
	 * The specific error message that lead to this exception.
	 */
	private String message;
	/**
	 * An indicator which kind of REST parameter coontained the error
	 */
	private ArgumentType type;
	/**
	 * The given value in a string representation
	 */
	private String value;

}
