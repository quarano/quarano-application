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
import quarano.actions.ActionItems;
import quarano.actions.Description;
import quarano.actions.DescriptionCode;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
@JsonAutoDetect(getterVisibility = Visibility.NON_PRIVATE)
public class CaseActionSummary {

	private final TrackedCase trackedCase;
	private final ActionItems items;

	TrackedCaseIdentifier getCaseId() {
		return trackedCase.getId();
	}

	String getName() {
		return trackedCase.getTrackedPerson().getFullName();
	}

	float getPriority() {
		return items.getPriority() * 100.00f / 100.00f;
	}

	List<DescriptionCode> getHealthSummary() {
		return getDescriptionCodes(ItemType.MEDICAL_INCIDENT);
	}

	List<DescriptionCode> getProcessSummary() {
		return getDescriptionCodes(ItemType.PROCESS_INCIDENT);
	}

	@JsonProperty("_links")
	Map<String, Object> getLinks() {

		var detailsLink = on(ActionItemController.class).allActions(getCaseId());

		return Map.of("self", Map.of("href", fromMethodCall(detailsLink).toUriString()));
	}

	private List<DescriptionCode> getDescriptionCodes(ItemType type) {

		return items.stream() //
				.filter(it -> it.getType().equals(type)) //
				.map(ActionItem::getDescription) //
				.map(Description::getCode) //
				.distinct() //
				.collect(Collectors.toUnmodifiableList());
	}
}
