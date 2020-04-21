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
import quarano.reference.SymptomDto;
import quarano.tracking.ContactPerson;
import quarano.tracking.DiaryEntry;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
public class DiaryEntryDetailsDto {

	private final DiaryEntry entry;
	private final MapperWrapper mapper;

	public String getId() {
		return entry.getId().toString();
	}

	public LocalDateTime getDate() {
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
