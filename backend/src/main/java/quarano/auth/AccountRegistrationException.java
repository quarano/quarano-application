/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.auth;

import lombok.Getter;

/**
 * @author Oliver Drotbohm
 */
public class AccountRegistrationException extends RuntimeException {

	private static final long serialVersionUID = 8628598426318947526L;

	private final @Getter Problem problem;

	public AccountRegistrationException(String message) {
		this(Problem.GENERIC, message);
	}

	private AccountRegistrationException(Problem problem, String message) {

		super(message);

		this.problem = problem;
	}

	public static AccountRegistrationException forInvalidUsername(AccountRegistrationDetails details) {
		return new AccountRegistrationException(Problem.INVALID_USERNAME,
				"Invalid username " + details.getUsername() + "!");
	}

	public static AccountRegistrationException forInvalidBirthDay(AccountRegistrationDetails details) {
		return new AccountRegistrationException(Problem.INVALID_BIRTHDAY, "Invalid birth date!");
	}

	public enum Problem {
		INVALID_USERNAME, INVALID_BIRTHDAY, GENERIC;
	}
}
