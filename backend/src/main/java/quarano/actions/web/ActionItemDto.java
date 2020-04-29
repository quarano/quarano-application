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
package quarano.actions.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItem;
import quarano.actions.ActionItem.ItemType;
import quarano.actions.DiaryEntryActionItem;
import quarano.tracking.DiaryEntry;
import quarano.tracking.web.DiaryController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.support.MessageSourceAccessor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
public class ActionItemDto {

	private final ActionItem item;
	private final MessageSourceAccessor messages;

	public LocalDateTime getDate() {
		return item.getMetadata().getLastModified();
	}

	public ItemType getType() {
		return item.getType();
	}

	public float getWeight() {
		return item.getWeight();
	}

	public String getDescription() {
		return messages.getMessage(item.getDescription());
	}

	@SuppressWarnings("null")
	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("_links")
	public Map<String, Object> getLinks() {

		var diaryController = on(DiaryController.class);
		var links = new HashMap<String, Object>();

		if (DiaryEntryActionItem.class.isInstance(item)) {

			DiaryEntry entry = ((DiaryEntryActionItem) item).getEntry();

			links.put("entry",
					Map.of("href", fromMethodCall(diaryController.getDiaryEntry(entry.getId(), null)).toUriString()));
		}

		return links;
	}
}
