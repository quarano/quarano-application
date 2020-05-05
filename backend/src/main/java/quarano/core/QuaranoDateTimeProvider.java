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
package quarano.core;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
public class QuaranoDateTimeProvider implements DateTimeProvider {

	private Period delta = Period.ZERO;

	/**
	 * @param delta the delta to set
	 */
	public void setDelta(Period delta) {
		this.delta = delta;
	}

	public void reset() {
		this.delta = Period.ZERO;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.auditing.DateTimeProvider#getNow()
	 */
	@Override
	public Optional<TemporalAccessor> getNow() {
		return Optional.of(LocalDateTime.now().plus(delta));
	}
}
