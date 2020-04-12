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
package quarano.tracking;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.data.domain.Range;

/**
 * @author Oliver Drotbohm
 */
@Embeddable
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class BodyTemperature {

	private static final Range<Float> VALID = Range.open(35.0f, 42.0f);

	@Column(name = "temperature") //
	private final @Getter float value;

	public static boolean isValid(float value) {
		return VALID.contains(value);
	}
}
