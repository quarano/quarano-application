package quarano.actions;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.*;
import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.account.Department;
import quarano.actions.ActionItem.ItemType;
import quarano.department.CaseType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.Quarantine;
import quarano.tracking.TrackedPerson;

import java.time.LocalDate;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.util.Streamable;

@QuaranoUnitTest
@MockitoSettings(strictness = Strictness.LENIENT)
class QuarantineEndCheckerTest {

	private static final Department DEPARTMENT = new Department("Department A");

	private static final LocalDate TODAY = LocalDate.now();
	private static final LocalDate TWO_WEEKS_AGO = TODAY.minusWeeks(2);
	private static final LocalDate THREE_WEEKS_AGO = TODAY.minusWeeks(3);
	private static final LocalDate SIX_DAYS_AGO = TODAY.minusDays(6);

	private static final TrackedCase IN_QUARANTINE = new TrackedCase(new TrackedPerson("Erika", "Musterfrau"),
			CaseType.INDEX, DEPARTMENT).setQuarantine(createQuarantine(SIX_DAYS_AGO));

	private static final TrackedCase QUARANTINE_OVER = new TrackedCase(new TrackedPerson("Max", "Mustermann"),
			CaseType.INDEX, DEPARTMENT).setQuarantine(createQuarantine(THREE_WEEKS_AGO));

	private static final TrackedCase QUARANTINE_ENDS_TODAY = new TrackedCase(new TrackedPerson("Olaf", "Beispiel"),
			CaseType.INDEX, DEPARTMENT).setQuarantine(createQuarantine(TWO_WEEKS_AGO));

	private static final Streamable<TrackedCase> TRACKED_CASES = Streamable //
			.of(IN_QUARANTINE, QUARANTINE_OVER, QUARANTINE_ENDS_TODAY);

	private static final int ONCE = 1;
	private static final int NEVER = 0;

	@InjectMocks QuarantineEndChecker quarantineEndChecker;

	@Mock TrackedCaseRepository cases;
	@Mock ActionItemRepository items;

	@BeforeEach
	void setUp() {

		when(cases.findAll()).thenReturn(TRACKED_CASES.map(it -> {

			var spy = spy(it);
			when(spy.isTracking()).thenReturn(true);

			return spy;
		}));
	}

	@Test // CORE-70
	void expectActionItemForCaseOutOfQuarantine() {

		prepareNonExistingActionItems();

		quarantineEndChecker.checkEndingQuarantinesPeriodically();

		var values = verifyRepositorySaveInteraction(ONCE).getAllValues();

		assertThat(values).hasSize(1);
		assertThat(values.get(0)).satisfies(isQuarantineEndedActionItemFor(QUARANTINE_OVER));
	}

	@Test // CORE-70
	void expectNoActionItemAsOneAlreadyExists() {

		prepareExistingActionItems(QUARANTINE_OVER);

		quarantineEndChecker.checkEndingQuarantinesPeriodically();

		var values = verifyRepositorySaveInteraction(NEVER).getAllValues();

		assertThat(values).isEmpty();
	}

	private void prepareNonExistingActionItems(TrackedCase... cases) {

		(cases.length == 0 ? TRACKED_CASES : Streamable.of(cases)).forEach(it -> {
			doReturn(ActionItems.empty()).when(items).findQuarantineEndingActionItemsFor(it);
		});
	}

	private void prepareExistingActionItems(TrackedCase... cases) {

		(cases.length == 0 ? TRACKED_CASES : Streamable.of(cases)).forEach(it -> {
			doReturn(ActionItems.of(createQuarantineEndedActionItem(it))) //
					.when(items).findQuarantineEndingActionItemsFor(it);
		});
	}

	private ArgumentCaptor<TrackedCaseActionItem> verifyRepositorySaveInteraction(int three) {
		ArgumentCaptor<TrackedCaseActionItem> quarantineEnding = forClass(TrackedCaseActionItem.class);
		verify(items, times(three)).save(quarantineEnding.capture());
		return quarantineEnding;
	}

	private static Quarantine createQuarantine(LocalDate startDate) {
		return Quarantine.of(startDate, startDate.plusWeeks(2));
	}

	private static TrackedCaseActionItem createQuarantineEndedActionItem(TrackedCase trackedCase) {
		return new TrackedCaseActionItem(trackedCase, ItemType.PROCESS_INCIDENT, DescriptionCode.QUARANTINE_ENDING);
	}

	private static Condition<TrackedCaseActionItem> isQuarantineEndedActionItemFor(TrackedCase trackedCase) {

		return new Condition<TrackedCaseActionItem>() {

			@Override
			public boolean matches(TrackedCaseActionItem item) {

				assertThat(item.getCaseIdentifier()).isEqualTo(trackedCase.getId());
				assertThat(item.getType()).isEqualTo(ItemType.PROCESS_INCIDENT);
				assertThat(item.getDescription().getCode()).isEqualTo(DescriptionCode.QUARANTINE_ENDING);

				return true;
			};
		};
	}
}
