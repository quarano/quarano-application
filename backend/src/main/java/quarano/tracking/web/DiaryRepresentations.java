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

import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import quarano.core.web.MapperWrapper;
import quarano.reference.SymptomDto;
import quarano.tracking.ContactPerson;
import quarano.tracking.Diary;
import quarano.tracking.Diary.DiaryEntryDay;
import quarano.tracking.DiaryEntry;
import quarano.tracking.Slot;
import quarano.tracking.Slot.TimeOfDay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
public class DiaryRepresentations {

	private final MapperWrapper mapper;

	DiarySummary toSummary(Diary diary, LocalDate startDate) {
		return new DiarySummary(diary, startDate, Slot.now());
	}

	DiarySummary toSummary(Diary diary, LocalDate startDate, Slot reference) {
		return new DiarySummary(diary, startDate, reference);
	}

	DiaryEntryRepresentation toRepresentation(DiaryEntry entry) {
		return new DiaryEntryRepresentation(entry, mapper, false);
	}

	DiaryEntryRepresentation toSummaryRepresentation(DiaryEntry entry) {
		return new DiaryEntryRepresentation(entry, mapper, true);
	}

	Either<DiaryEntry, Errors> from(DiaryEntryInput input, Errors errors) {
		return mapper.map(input, DiaryEntry.class, errors);
	}

	Either<DiaryEntry, Errors> from(DiaryEntryInput input, DiaryEntry existing, Errors errors) {
		return mapper.map(input, existing, errors);
	}

	@Data
	@Accessors(chain = true)
	@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
	@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
	static class DiaryEntryInput {

		private final @NotNull LocalDate date;
		private final @NotNull TimeOfDay timeOfDay;

		DiaryEntryInput(Slot slot) {

			this.date = slot.getDate();
			this.timeOfDay = slot.getTimeOfDay();
		}

		private float bodyTemperature;
		private List<UUID> symptoms = new ArrayList<>();
		private List<UUID> contacts = new ArrayList<>();

		public Slot getSlot() {
			return Slot.of(date, timeOfDay);
		}
	}

	@RequiredArgsConstructor(staticName = "of")
	static class DiaryEntryRepresentation {

		private final DiaryEntry entry;
		private final MapperWrapper mapper;
		private final boolean summary;

		public String getId() {
			return entry.getId().toString();
		}

		@JsonInclude(Include.NON_EMPTY)
		public Map<String, Object> getSlot() {

			if (summary) {
				return Collections.emptyMap();
			}

			var slot = entry.getSlot();

			return Map.of("date", slot.getDate(), //
					"timeOfDay", slot.getTimeOfDay().name().toLowerCase(Locale.US));
		}

		public LocalDateTime getReportedAt() {
			return entry.getDateTime();
		}

		public float getBodyTemperature() {
			return entry.getBodyTemperature().getValue();
		}

		public Stream<?> getContacts() {
			return entry.getContacts().stream() //
					.map(ContactSummary::new);
		}

		public Stream<SymptomDto> getSymptoms() {

			return entry.getSymptoms().stream() //
					.map(it -> mapper.map(it, SymptomDto.class));
		}

		@JsonProperty("_links")
		public Map<String, Object> getLinks() {

			var selfLink = on(DiaryController.class).getDiaryEntry(entry.getId(), null);
			var selfUri = Map.of("href", fromMethodCall(selfLink).toUriString());

			Map<String, Object> links = new HashMap<>();
			links.put("self", selfUri);

			if (!entry.getSlot().isOlderThan(Period.ofDays(2))) {
				links.put("edit", selfUri);
			}

			return links;
		}

		@RequiredArgsConstructor
		static class ContactSummary {

			private final ContactPerson contact;

			public String getId() {
				return contact.getId().toString();
			}

			public String getFirstName() {
				return contact.getFirstName();
			}

			public String getLastName() {
				return contact.getLastName();
			}

			@JsonProperty("_links")
			public Map<String, Object> getLinks() {

				var itemResource = on(ContactPersonController.class).getContact(null, contact.getId());

				return Map.of("self", Map.of("href", fromMethodCall(itemResource).toUriString()));
			}
		}
	}

	@RequiredArgsConstructor // (staticName = "of")
	class DiarySummary {

		private final Diary diary;
		private final LocalDate startDate;
		private final Slot reference;

		@JsonProperty("_embedded")
		public Map<String, Object> getEmbeddeds() {

			return Map.of("entries", getEntries().map(it -> {

				var fields = new LinkedHashMap<>();
				fields.put("date", it.getDate()); //
				fields.put("evening", mapEntry(it.getEvening(), it.allowCreation() && !reference.isMorningOf(it.getDate())));
				fields.put("morning", mapEntry(it.getMorning(), it.allowCreation())); //

				return fields;
			}));
		}

		private Object mapEntry(Optional<DiaryEntry> entry, boolean allowCreation) {

			return entry //
					.<Object> map(it -> DiaryRepresentations.this.toSummaryRepresentation(it)) //
					.orElseGet(() -> new NewDiaryEntryDto(allowCreation));
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

		@RequiredArgsConstructor
		private class NewDiaryEntryDto {

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

}
