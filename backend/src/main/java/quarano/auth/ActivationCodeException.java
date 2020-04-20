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

import quarano.auth.ActivationCode.ActivationCodeIdentifier;

/**
 * @author Oliver Drotbohm
 */
public class ActivationCodeException extends RuntimeException {

	private static final long serialVersionUID = -1862988574924745449L;

	private final ActivationCodeIdentifier code;
	private final Status status;

	public static ActivationCodeException notFound(ActivationCodeIdentifier id) {
		return new ActivationCodeException(id, Status.NOT_FOUND, "Could not find activation code " + id.toString());
	}

	public static ActivationCodeException expired(ActivationCode code) {
		return new ActivationCodeException(code.getId(), Status.EXPIRED, "Activation code " + code.getId() + " expired!");
	}

	public static ActivationCodeException usedOrCanceled(ActivationCode code) {
		return new ActivationCodeException(code.getId(), Status.USED_OR_CANCELED,
				"Activation code " + code.getId() + " was already used or canceled!");
	}

	private ActivationCodeException(ActivationCodeIdentifier code, Status status, String message) {

		super(message);
		this.code = code;
		this.status = status;
	}

	enum Status {
		NOT_FOUND, EXPIRED, USED_OR_CANCELED;
	}
}
