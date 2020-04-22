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

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
public class DiaryUnitTests {

	@Test
	void createsDiaryEntryDays() {

		var now = LocalDateTime.now();

		DiaryEntry first = new DiaryEntry(Slot.of(now));
		DiaryEntry second = new DiaryEntry(Slot.of(now.minusHours(12)));
		DiaryEntry third = new DiaryEntry(Slot.of(now.minusHours(24)));

		var diary = Diary.of(List.of(first, second, third));

		assertThat(diary.toEntryDays(Slot.now().getDate().minusDays(4))).hasSize(5);
	}
}
