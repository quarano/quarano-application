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
package quarano.actions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import quarano.core.EnumMessageSourceResolvable;
import quarano.tracking.BodyTemperature;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.context.MessageSourceResolvable;

/**
 * @author Oliver Drotbohm
 */
@Embeddable
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Description implements MessageSourceResolvable {
	@Enumerated(EnumType.STRING)
	private final @Getter DescriptionCode code;
	private final String arguments;

	public static Description of(DescriptionCode code, Object... arguments) {

		var collect = Arrays.stream(arguments) //
				.map(Object::toString) //
				.collect(Collectors.joining("|"));

		return new Description(code, collect);
	}

	public static Description forIncreasedTemperature(BodyTemperature temperature, BodyTemperature reference) {
		return of(DescriptionCode.INCREASED_TEMPERATURE, temperature, reference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.MessageSourceResolvable#getCodes()
	 */
	@Override
	public String[] getCodes() {
		return EnumMessageSourceResolvable.of(code).getCodes();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.MessageSourceResolvable#getArguments()
	 */
	@Override
	public Object[] getArguments() {
		return arguments.split("\\|");
	}
}
