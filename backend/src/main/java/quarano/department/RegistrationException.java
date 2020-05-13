package quarano.department;

import lombok.Getter;

/**
 * @author Oliver Drotbohm
 */
public class RegistrationException extends RuntimeException {

	private static final long serialVersionUID = 8628598426318947526L;

	private final @Getter Problem problem;

	public RegistrationException(String message) {
		this(Problem.GENERIC, message);
	}

	private RegistrationException(Problem problem, String message) {

		super(message);

		this.problem = problem;
	}

	public static RegistrationException forInvalidUsername(RegistrationDetails details) {
		return new RegistrationException(Problem.INVALID_USERNAME,
				"Invalid username " + details.getUsername() + "!");
	}

	public static RegistrationException forInvalidBirthDay(RegistrationDetails details) {
		return new RegistrationException(Problem.INVALID_BIRTHDAY, "Invalid birth date!");
	}

	public enum Problem {
		INVALID_USERNAME, INVALID_BIRTHDAY, GENERIC;
	}
}
