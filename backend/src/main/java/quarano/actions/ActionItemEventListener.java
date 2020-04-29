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
import quarano.auth.ActivationCodeService;
import quarano.department.TrackedCase.TrackedCaseUpdated;
import quarano.tracking.DiaryEntry.DiaryEntryAdded;

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
	private final @NonNull ActivationCodeService activations;

	@EventListener
	void on(DiaryEntryAdded event) {

		var entry = event.getEntry();
		var person = entry.getTrackedPersonId();

		// Body temperature exceeds reference

		if (entry.getBodyTemperature().exceeds(config.getTemperatureThreshold())) {

			Description description = Description.of(DescriptionCode.INCREASED_TEMPERATURE, //
					entry.getBodyTemperature(), //
					config.getTemperatureThreshold());

			items.save(new DiaryEntryActionItem(person, entry, ItemType.MEDICAL_INCIDENT, description));
		}

		// First characteristic symptom reported

		if (entry.getSymptoms().hasCharacteristicSymptom() && //
				items.findByDescriptionCode(person, DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM).isEmpty()) {

			items.save(new DiaryEntryActionItem(person, entry, ItemType.MEDICAL_INCIDENT,
					Description.of(DescriptionCode.FIRST_CHARACTERISTIC_SYMPTOM)));
		}
	}

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

		if (trackedCase.isConcluded() //
				|| trackedCase.getEnrollment().isCompletedPersonalData()) {
			return;
		}
		
		var person = trackedCase.getTrackedPerson();

		// create "initial call open" action if applicable
		if (trackedCase.getMetadata().getCreated().isAfter(LocalDateTime.now().minusSeconds(5))) {
			
			// check if case is already in registration
			if(activations.getPendingActivationCode(person.getId()).isEmpty()){
			
				items.save(new TrackedCaseActionItem(person.getId(), trackedCase.getId(), ItemType.PROCESS_INCIDENT,
						DescriptionCode.INITIAL_CALL_OPEN_INDEX));
			}
		}

		// create "missing-detail-action" if data is not complete
		var detailsMissing = person.getPhoneNumber() == null //
				&& person.getMobilePhoneNumber() == null //
				|| person.getEmailAddress() == null //
				|| person.getDateOfBirth() == null;

		Streamable<ActionItem> item = items.findByDescriptionCode(person.getId(), DescriptionCode.MISSING_DETAILS_INDEX);

		if (!detailsMissing) {

			item.stream() //
					.map(ActionItem::resolve) //
					.forEach(items::save);

			return;
		}

		if (item.isEmpty()) {
			items.save(new TrackedCaseActionItem(person.getId(), trackedCase.getId(), ItemType.PROCESS_INCIDENT,
					DescriptionCode.MISSING_DETAILS_INDEX));
		}
	}

	private void handleContactCaseEvent(TrackedCaseUpdated event) {
		// TODO Auto-generated method stub

	}
}
