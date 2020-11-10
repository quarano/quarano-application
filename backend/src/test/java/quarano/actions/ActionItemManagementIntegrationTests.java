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
class ActionItemManagementIntegrationTests {

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
