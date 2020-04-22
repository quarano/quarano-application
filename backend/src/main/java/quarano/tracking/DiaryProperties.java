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

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author Oliver Drotbohm
 */
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("quarano.diary")
public class DiaryProperties {

	private static final Duration DEFAULT_DURATION = Duration.ofHours(24);

	private final Duration editDuration;
	private final Duration createDuration;

	public boolean canBeEdited(DiaryEntry entry) {

		var duration = editDuration == null ? DEFAULT_DURATION : editDuration;

		return entry.getReportedAt().plus(duration).isAfter(LocalDateTime.now());
	}

	public boolean canBeCreated(Slot slot) {

		var duration = createDuration == null ? DEFAULT_DURATION : createDuration;

		return !slot.isOlderThan(duration);
	}
}
