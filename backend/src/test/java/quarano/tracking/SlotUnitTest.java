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

import static org.assertj.core.api.Assertions.*;

import quarano.tracking.Slot.TimeOfDay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
class SlotUnitTest {

	@Test
	void detectsOldSlots() {

		Slot eveningTwoDaysAgo = Slot.of(LocalDate.now().minusDays(2), TimeOfDay.EVENING);

		assertThat(eveningTwoDaysAgo.isOlderThan(Period.ofDays(1))).isTrue();
		assertThat(eveningTwoDaysAgo.isOlderThan(Period.ofDays(2))).isFalse();
	}

	@Test
	void detectsContainsDateTime() {

		var threeThirtyThree = LocalDateTime.now().withHour(3).withMinute(33);

		assertThat(Slot.of(threeThirtyThree).contains(threeThirtyThree)).isTrue();
	}
}
