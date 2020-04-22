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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.Arrays;

import javax.persistence.Embeddable;

import org.springframework.util.Assert;

/**
 * Represents a time slot, a {@link DiaryEntry} is assigned to and lives in. I consists of a date and a
 * {@link TimeOfDay} that covers certain time spans (6am-4pm, 4pm-6am). Interesting about esp. the latter period is that
 * e.g. the Slot for e.g. 2am of a day is the evening slot of the day before. {@link Slot#of(LocalDateTime)} correctly
 * considers this.
 *
 * @author Oliver Drotbohm
 */
@Embeddable
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Slot {

	private final LocalDate date;
	private final TimeOfDay timeOfDay;

	/**
	 * Creates a new {@link Slot} for the morning of the given date.
	 *
	 * @param date must not be {@literal null}.
	 * @return
	 */
	static Slot morningOf(LocalDate date) {

		Assert.notNull(date, "Date must not be null!");

		return Slot.of(date, TimeOfDay.MORNING);
	}

	/**
	 * Creates a {@link Slot} for the evening of the given date.
	 *
	 * @param date must not be {@literal null}.
	 * @return
	 */
	static Slot eveningOf(LocalDate date) {

		Assert.notNull(date, "Date must not be null!");

		return Slot.of(date, TimeOfDay.EVENING);
	}

	/**
	 * Creates the {@link Slot} for the given date.
	 *
	 * @param date must not be {@literal null}.
	 * @return
	 */
	public static Slot of(LocalDateTime date) {

		Assert.notNull(date, "Date must not be null!");

		var tod = TimeOfDay.of(date);

		return new Slot(tod.toLocalDate(date), tod);
	}

	/**
	 * Creates a new {@link Slot} for the current date and time.
	 *
	 * @return
	 */
	public static Slot now() {
		return Slot.of(LocalDateTime.now());
	}

	public boolean isOlderThan(Period period) {

		var reference = date.plus(period);
		var now = LocalDate.now();

		if (reference.isBefore(now)) {
			return true;
		}

		return reference.equals(now) ? timeOfDay.isBefore(TimeOfDay.now()) : false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s of %s", timeOfDay, date);
	}

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public enum TimeOfDay {

		MORNING(LocalTime.of(6, 0, 0), LocalTime.of(15, 59, 59)) {

			/*
			 * (non-Javadoc)
			 * @see quarano.tracking.Diary.TimeOfDay#contains(java.time.LocalTime)
			 */
			@Override
			boolean contains(LocalTime reference) {
				return MORNING.from.isBefore(reference) && MORNING.to.isAfter(reference);
			}
		},

		EVENING(LocalTime.of(16, 0, 0), LocalTime.of(5, 59, 59)) {

			/*
			 * (non-Javadoc)
			 * @see quarano.tracking.Diary.TimeOfDay#contains(java.time.LocalTime)
			 */
			@Override
			boolean contains(LocalTime reference) {
				return reference.isAfter(EVENING.from) || reference.isBefore(EVENING.to);
			}

			/*
			 * (non-Javadoc)
			 * @see quarano.tracking.Diary.TimeOfDay#toLocalDate(java.time.LocalDateTime)
			 */
			@Override
			LocalDate toLocalDate(LocalDateTime date) {

				var time = date.toLocalTime();
				var result = date.toLocalDate();

				return time.isBefore(EVENING.to) ? result.minusDays(1) : result;
			}
		};

		private final LocalTime from;
		private final LocalTime to;

		abstract boolean contains(LocalTime reference);

		LocalDate toLocalDate(LocalDateTime date) {
			return date.toLocalDate();
		}

		public boolean isBefore(TimeOfDay timeOfDay) {
			return timeOfDay.equals(EVENING) ? this.equals(MORNING) : false;
		}

		public static TimeOfDay now() {
			return of(LocalTime.now());
		}

		private static TimeOfDay of(LocalDateTime reference) {
			return of(reference.toLocalTime());
		}

		private static TimeOfDay of(LocalTime reference) {

			return Arrays.stream(values()) //
					.filter(it -> it.contains(reference)) //
					.findFirst() //
					.orElseThrow(() -> new IllegalStateException("Should not happen!"));
		}
	}
}
