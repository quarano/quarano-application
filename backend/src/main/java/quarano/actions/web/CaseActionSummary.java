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

import quarano.actions.ActionItem;
import quarano.actions.ActionItem.ItemType;
import quarano.actions.ActionItems;
import quarano.actions.Description;
import quarano.actions.DescriptionCode;
import quarano.department.TrackedCase;
import quarano.department.web.TrackedCaseSummaryDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Drotbohm
 * @author Patrick Otto
 */
@JsonAutoDetect(getterVisibility = Visibility.NON_PRIVATE)
public class CaseActionSummary {

	private final ActionItems items;
	private final TrackedCaseSummaryDto trackedCaseDto;
	private final TrackedCase trackedCase;

	public String getCaseId() {
		return trackedCaseDto.getCaseId();
	}

	public String getFirstName() {
		return trackedCaseDto.getFirstName();
	}

	public String getLastName() {
		return trackedCaseDto.getLastName();
	}

	public String getZipCode() {
		return trackedCaseDto.getZipCode();
	}

	public String getDateOfBirth() {
		return trackedCaseDto.getDateOfBirth();
	}

	public String getEmail() {
		return trackedCaseDto.getEmail();
	}

	public String getCaseType() {
		return trackedCaseDto.getCaseType();
	}

	public String getPrimaryPhoneNumber() {
		return trackedCaseDto.getPrimaryPhoneNumber();
	}

	public Map<String, Object> getQuarantine() {
		return trackedCaseDto.getQuarantine();
	}

	public static CaseActionSummary of(TrackedCase trackedCase, ActionItems items) {
		return new CaseActionSummary(trackedCase, items);
	}

	private CaseActionSummary(TrackedCase trackedCase, ActionItems items) {
		this.trackedCase = trackedCase;
		this.items = items;
		this.trackedCaseDto = TrackedCaseSummaryDto.of(trackedCase);
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

	public int getNumberOfActionItems() {
		return getDescriptionCodes(ItemType.MEDICAL_INCIDENT).size()
				+ getDescriptionCodes(ItemType.PROCESS_INCIDENT).size();
	}

	@JsonProperty("_links")
	Map<String, Object> getLinks() {

		var detailsLink = on(ActionItemController.class).allActions(trackedCase.getId());

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
