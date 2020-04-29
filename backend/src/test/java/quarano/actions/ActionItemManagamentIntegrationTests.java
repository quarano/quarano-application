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

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPersonDataInitializer;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class ActionItemManagamentIntegrationTests {

	private final TrackedCaseRepository cases;
	private final ActionItemRepository repository;
	private final ActionItemsManagement management;

	@Test
	void resolvesOpenActionItemsAndAddsComment() {

		var nadineCase = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON5_ID_DEP1)
				.orElseThrow();

		var items = repository.findByCase(nadineCase);

		assertThat(items.hasUnresolvedItems()).isTrue();

		management.resolveItemsFor(nadineCase, "Some comment");

		items = repository.findByCase(nadineCase);

		assertThat(items.hasUnresolvedItems()).isFalse();
		assertThat(nadineCase.getComments()).hasSize(1);
	}
}
