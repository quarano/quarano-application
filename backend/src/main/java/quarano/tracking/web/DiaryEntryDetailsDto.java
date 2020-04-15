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
import quarano.reference.NewSymptom;
import quarano.tracking.ContactPerson;
import quarano.tracking.DiaryEntry;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
public class DiaryEntryDetailsDto {

	private final DiaryEntry entry;

	public Stream<?> getContacts() {
		return entry.getContacts().stream() //
				.map(ContactSummary::new);
	}

	public Stream<?> getSymptoms() {

		return entry.getSymptoms().stream() //
				.map(SymptomSummary::new);
	}

	@JsonProperty("_links")
	public Map<String, Object> getLinks() {

		var selfLink = on(DiaryController.class).getDiaryEntry(entry.getId(), null);

		return Map.of("self", Map.of("href", fromMethodCall(selfLink).toUriString()));
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

	@RequiredArgsConstructor
	static class SymptomSummary {

		private final NewSymptom symptom;

		public UUID getId() {
			return symptom.getId();
		}

		public String getName() {
			return symptom.getName();
		}
	}
}
