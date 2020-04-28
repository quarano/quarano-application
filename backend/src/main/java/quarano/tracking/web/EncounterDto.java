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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import quarano.tracking.Diary;
import quarano.tracking.Encounter;
import quarano.tracking.TrackedPerson;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Drotbohm
 */
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class EncounterDto {

	private Encounter encounter;
	private Diary diary;
	private TrackedPerson person;

	public LocalDate getDate() {
		return encounter.getDate();
	}

	public String getFirstName() {
		return encounter.getContact().getFirstName();
	}

	public String getLastName() {
		return encounter.getContact().getLastName();
	}

	@JsonProperty("_links")
	Map<String, Object> getLinks() {

		var contactId = encounter.getContact().getId();
		var contactHandlerMethod = on(ContactPersonController.class).getContact(null, contactId);
		var encounterUri = on(TrackingController.class).getEncounter(encounter.getId(), person);

		var links = new HashMap<String, Object>();

		links.put("self", Map.of("href", fromMethodCall(encounterUri).toUriString()));
		links.put("contact", Map.of("href", fromMethodCall(contactHandlerMethod).toUriString()));

		diary.getEntryFor(encounter).ifPresent(it -> {
			var handlerMethod = on(DiaryController.class).getDiaryEntry(it.getId(), person);
			links.put("diaryEntry", Map.of("href", fromMethodCall(handlerMethod).toUriString()));
		});

		return links;
	}
}
