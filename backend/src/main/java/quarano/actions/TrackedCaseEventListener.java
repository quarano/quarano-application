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
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.CaseCreated;
import quarano.department.TrackedCase.CaseStatusUpdated;
import quarano.department.TrackedCase.CaseUpdated;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
public class TrackedCaseEventListener {

	private final @NonNull InitialCallHandler initialCallHandler;
	private final @NonNull MissingDetailsHandler missingDetailsHandler;

	@EventListener
	public void on(CaseCreated event) {
		handleCreatedOrUpdatedCase(event.getTrackedCase());
	}

	@EventListener
	public void on(CaseUpdated event) {
		handleCreatedOrUpdatedCase(event.getTrackedCase());
	}

	@EventListener
	public void on(CaseStatusUpdated event) {
		handleCreatedOrUpdatedCase(event.getTrackedCase());
	}

	private void handleCreatedOrUpdatedCase(TrackedCase trackedCase) {
		initialCallHandler.handleInitialCallOpen(trackedCase);
		missingDetailsHandler.handleTrackedCaseMissingDetails(trackedCase);
	}
}
