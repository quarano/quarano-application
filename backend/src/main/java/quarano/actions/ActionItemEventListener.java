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
package quarano.actions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItem.ItemType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.Status;
import quarano.department.TrackedCase.TrackedCaseUpdated;
import quarano.tracking.TrackedPerson;

import java.time.LocalDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
public class ActionItemEventListener {

	private final @NonNull ActionItemRepository items;
	private final @NonNull AnomaliesProperties config;

	@EventListener
	void on(TrackedCaseUpdated event) {
		if (event.getTrackedCase().isIndexCase()) {

			handleIndexCaseEvent(event);

		} else {

			handleContactCaseEvent(event);

		}
	}

	private void handleIndexCaseEvent(TrackedCaseUpdated event) {
		var trackedCase = event.getTrackedCase();
		var person = trackedCase.getTrackedPerson();

		handleTrackedCaseInitialCallOpen(trackedCase, person);
		handleTrackedCaseMissingDetails(trackedCase, person);
	}

	private void handleTrackedCaseInitialCallOpen(TrackedCase trackedCase, TrackedPerson person) {
		if (trackedCase.isConcluded() //
				|| trackedCase.getEnrollment().isCompletedPersonalData()) {
			return;
		}

		// create "initial call open" action if applicable
		if (trackedCase.getMetadata().getCreated().isAfter(LocalDateTime.now().minusSeconds(5))) {

			if (trackedCase.getStatus().equals(Status.OPEN)) {
				items.save(new TrackedCaseActionItem(person.getId(), trackedCase.getId(), ItemType.PROCESS_INCIDENT,
						DescriptionCode.INITIAL_CALL_OPEN_INDEX));
			}
		}
	}

	private void handleTrackedCaseMissingDetails(TrackedCase trackedCase, TrackedPerson person) {
		// TODO: trackedCase.isConcluded(): einfach alle schließen
		if (trackedCase.isConcluded() //
				|| trackedCase.getEnrollment().isCompletedPersonalData()) {

			resolveItems(items.findByDescriptionCode(person.getId(), DescriptionCode.MISSING_DETAILS_INDEX));
			return;
		}

		// create "missing-detail-action" if data is not complete
		var detailsMissing = person.getPhoneNumber() == null //
				&& person.getMobilePhoneNumber() == null //
				|| person.getEmailAddress() == null //
				|| person.getDateOfBirth() == null;

		Streamable<ActionItem> actionItems = items.findByDescriptionCode(person.getId(),
				DescriptionCode.MISSING_DETAILS_INDEX);

		if (!detailsMissing) {
			resolveItems(actionItems);
			return;
		}

		if (actionItems.isEmpty()) {
			items.save(new TrackedCaseActionItem(person.getId(), trackedCase.getId(), ItemType.PROCESS_INCIDENT,
					DescriptionCode.MISSING_DETAILS_INDEX));
		}
	}

	private void handleContactCaseEvent(TrackedCaseUpdated event) {
		// TODO Auto-generated method stub

	}

	private void resolveItems(Streamable<? extends ActionItem> actionItems) {
		actionItems.map(ActionItem::resolve).forEach(items::save);
	}
}
