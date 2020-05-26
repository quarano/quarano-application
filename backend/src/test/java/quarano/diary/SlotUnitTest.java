package quarano.diary;

import static org.assertj.core.api.Assertions.*;

import quarano.diary.Slot.TimeOfDay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
class SlotUnitTest {

	@Test
	@Disabled
	void detectsOldSlots() {

		Slot eveningTwoDaysAgo = Slot.of(LocalDate.now().minusDays(2), TimeOfDay.EVENING);

		assertThat(eveningTwoDaysAgo.isOlderThan(Period.ofDays(1))).isTrue();
		assertThat(eveningTwoDaysAgo.isOlderThan(Period.ofDays(2)))
				.isEqualTo(!TimeOfDay.EVENING.isInOvertime(LocalTime.now()));
		assertThat(eveningTwoDaysAgo.isOlderThan(Period.ofDays(3))).isFalse();
	}

	@Test
	void detectsContainsDateTime() {

		var threeThirtyThree = LocalDateTime.now().withHour(3).withMinute(33);

		assertThat(Slot.of(threeThirtyThree).contains(threeThirtyThree)).isTrue();
	}

	@Test
	void timeOfDayCalculatedCorrectly() {

		assertThat(TimeOfDay.of(LocalTime.of(15, 45))).isEqualTo(TimeOfDay.MORNING);
	}
}
