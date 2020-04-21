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
package quarano.tracking.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.RequiredArgsConstructor;
import quarano.core.web.MapperWrapper;
import quarano.tracking.Diary;
import quarano.tracking.Diary.DiaryEntryDay;
import quarano.tracking.DiaryEntry;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
public class DiarySummaryDto {

	private final Diary diary;
	private final LocalDate startDate;
	private final MapperWrapper mapper;

	@JsonProperty("_embedded")
	public Map<String, Object> getEmbeddeds() {

		return Map.of("entries", getEntries().map(it -> {

			var fields = new LinkedHashMap<>();
			fields.put("date", it.getDate()); //
			fields.put("evening", mapEntry(it.getEvening(), it.allowCreation()));
			fields.put("morning", mapEntry(it.getMorning(), it.allowCreation())); //

			return fields;
		}));
	}

	private Object mapEntry(Optional<DiaryEntry> entry, boolean allowCreation) {

		return entry //
				.<Object> map(it -> DiaryEntryDetailsDto.of(it, mapper)) //
				.orElseGet(() -> NewDiaryEntryDto.of(allowCreation));
	}

	private Stream<DiaryEntryDay> getEntries() {
		return diary.toEntryDays(startDate);
	}

	@JsonProperty("_links")
	public Map<String, Object> getLinks() {

		if (!diary.containsCurrentEntry()) {
			return Map.of("create", Map.of("href", "/api/diary/form"));
		}

		return Collections.emptyMap();
	}

	@RequiredArgsConstructor(staticName = "of")
	private static class NewDiaryEntryDto {

		private final boolean allowCreation;

		@JsonInclude(Include.NON_EMPTY)
		@JsonProperty("_links")
		Map<String, Object> getLinks() {

			if (!allowCreation) {
				return Collections.emptyMap();
			}

			var addEntry = on(DiaryController.class).addDiaryEntry(null, null, null);

			return Map.of("create", Map.of("href", fromMethodCall(addEntry).toUriString()));
		}
	}
}
