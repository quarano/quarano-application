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
import quarano.department.TrackedCaseRepository;

import javax.transaction.Transactional;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Oliver Drotbohm
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ActionItemsManagement {

	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull ActionItemRepository items;

	@SuppressWarnings("null")
	public void resolveItemsFor(TrackedCase trackedCase, @Nullable String comment) {

		if (StringUtils.hasText(comment)) {
			trackedCase = cases.save(trackedCase.addComment(comment));
		}

		items.findByCase(trackedCase) //
				.getUnresolvedItems() //
				.map(ActionItem::resolve) //
				.map(items::save) //
				.toList();
	}
}
