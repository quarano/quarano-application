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
import quarano.actions.ActionItems;
import quarano.auth.web.LoggedIn;
import quarano.department.Department;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPerson;

import java.util.Comparator;
import java.util.stream.Stream;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Oliver Drotbohm
 */
@RestController
@RequiredArgsConstructor
public class ActionItemController {

	private final @NonNull ActionItemRepository items;
	private final @NonNull MessageSourceAccessor messages;

	private final @NonNull TrackedCaseRepository cases;

	@GetMapping("/api/hd/actions/{identifier}")
	Stream<?> allActions(@PathVariable TrackedCaseIdentifier identifier) {

		return cases.findById(identifier) //
				.map(TrackedCase::getTrackedPerson) //
				.map(TrackedPerson::getId) //
				.map(items::findByTrackedPerson) //
				.map(ActionItems::stream) //
				.orElseGet(() -> Stream.empty()) //
				.map(it -> ActionItemDto.of(it, messages)); //
	}

	@GetMapping("/api/hd/actions")
	Stream<?> getActions(@LoggedIn Department department) {

		return cases.findByDepartmentId(department.getId()) //
				.map(it -> CaseActionSummary.of(it, items.findByTrackedPerson(it.getTrackedPerson().getId()))) //
				.stream() //
				.sorted(Comparator.comparing(CaseActionSummary::getPriority));
	}
}
