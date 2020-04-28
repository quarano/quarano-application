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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItemRepository;
import quarano.actions.ActionItemsManagement;
import quarano.auth.web.LoggedIn;
import quarano.core.web.ErrorsDto;
import quarano.department.Department;
import quarano.department.Department.DepartmentIdentifier;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseRepository;
import quarano.department.web.ExternalTrackedCaseRepresentations;

import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Oliver Drotbohm
 */
@RestController
@RequiredArgsConstructor
public class ActionItemController {

	private final @NonNull ActionItemRepository items;
	private final @NonNull ActionItemsManagement actionItems;
	private final @NonNull MessageSourceAccessor messages;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull ExternalTrackedCaseRepresentations trackedCaseRepresentations;

	@GetMapping("/api/hd/actions/{identifier}")
	HttpEntity<?> allActions(@PathVariable TrackedCaseIdentifier identifier, //
			@LoggedIn DepartmentIdentifier department) {

		var trackedCase = cases.findById(identifier) //
				.filter(it -> it.belongsTo(department)) //
				.orElse(null);

		if (trackedCase == null) {
			return ResponseEntity.notFound().build();
		}

		var id = trackedCase.getTrackedPerson().getId();

		return ResponseEntity.ok(CaseActionsRepresentation.of(trackedCase, items.findByTrackedPerson(id), messages));
	}

	@PutMapping("/api/hd/actions/{identifier}/resolve")
	HttpEntity<?> resolveActions(@PathVariable TrackedCaseIdentifier identifier, //
			@Valid @RequestBody ActionsReviewed payload, //
			Errors errors, //
			@LoggedIn DepartmentIdentifier department) {

		TrackedCase trackedCase = cases.findById(identifier) //
				.filter(it -> it.belongsTo(department)) //
				.orElse(null);

		if (trackedCase == null) {
			return ResponseEntity.notFound().build();
		}

		if (errors.hasErrors()) {
			return ErrorsDto.of(errors, messages).toBadRequest();
		}

		actionItems.resolveItemsFor(trackedCase, payload.getComment());

		return allActions(identifier, department);
	}

	@GetMapping("/api/hd/actions")
	Stream<?> getActions(@LoggedIn Department department) {

		return cases.findByDepartmentId(department.getId()) //
				.map(it -> new CaseActionSummary(it, items.findByTrackedPerson(it.getTrackedPerson().getId()),
						trackedCaseRepresentations.toSummary(it))) //
				.stream() //
				.filter(c -> c.getNumberOfActionItems() > 0)
				.sorted((c1, c2) -> Float.compare(c2.getPriority(), c1.getPriority())); // highest priority first
	}
}
