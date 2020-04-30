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

import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.data.domain.Range;
import org.springframework.util.NumberUtils;

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

	/**
	 * Returns whether the current {@link BodyTemperature} exceeds the given one.
	 *
	 * @param that must not be {@literal null}.
	 * @return
	 */
	public boolean exceeds(BodyTemperature that) {

		Assert.notNull(that, "BodyTemperature must not be null!");

		return this.value > that.value;
	}

	public static boolean isValid(float value) {
		return VALID.contains(value);
	}

	/**
	 * Parses a {@link BodyTemperature} from the given source {@link String}. Required by Spring Boot's configuration
	 * properties binding.
	 *
	 * @param source must not be {@literal null} or empty.
	 * @return
	 */
	public static BodyTemperature from(String source) {

		Assert.hasText(source, "No body temperature source value given!");

		return new BodyTemperature(NumberUtils.parseNumber(source, Float.class));
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(Locale.GERMAN, "%.1f°C", value);
	}
}
