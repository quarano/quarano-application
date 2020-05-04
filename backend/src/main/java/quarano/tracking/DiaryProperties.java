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

	private static final Duration DEFAULT_POST_SLOT = Duration.ofHours(24);
	private static final Duration DEFAULT_POST_CREATE = Duration.ofMinutes(15);

	/**
	 * Defines the {@link Duration} that {@link DiaryEntry} can still be edited relative to the official {@link Slot} end
	 * time they're assigned to.
	 */
	private final Duration editDuration;

	/**
	 * Defines the {@link Duration} that {@link DiaryEntry} can still be edited after they have been created. This
	 * potentially extends the value defined for {@link #editDuration} in case a DiaryEntry was created for a {@link Slot}
	 * that's just about to become uneditable.
	 */
	private final Duration editTolerance;

	/**
	 * Defines the number of slots, which may not be filled.
	 */
	private final int toleratedSlotCount;

	/**
	 * Defines the {@link Duration} that {@link DiaryEntry} can still be created.
	 */
	private final Duration createDuration;

	public boolean canBeEdited(DiaryEntry entry) {

		var postSlot = editDuration == null ? DEFAULT_POST_SLOT : editDuration;
		var postCreation = editTolerance == null ? DEFAULT_POST_CREATE : editTolerance;

		var slotEnd = entry.getSlot().getOfficialEnd().plus(postSlot);
		var creationEnd = entry.getReportedAt().plus(postCreation);
		var now = LocalDateTime.now();

		return slotEnd.isAfter(now) || creationEnd.isAfter(now);
	}

	public boolean canBeCreated(Slot slot) {

		var duration = createDuration == null ? DEFAULT_POST_SLOT : createDuration;

		return !slot.isOlderThan(duration);
	}

	public int getToleratedSlotCount() {
		return this.toleratedSlotCount;
	}
}
