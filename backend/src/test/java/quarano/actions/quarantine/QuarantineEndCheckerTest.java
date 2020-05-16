package quarano.actions.quarantine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import org.joda.time.DateTimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Streamable;
import quarano.account.Department;
import quarano.actions.ActionItemRepository;
import quarano.actions.ActionItems;
import quarano.actions.DescriptionCode;
import quarano.department.CaseType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.Quarantine;
import quarano.tracking.TrackedPerson;

@ExtendWith(MockitoExtension.class)
class QuarantineEndCheckerTest {

	private static final Department DEPARTMENT = new Department("Department A");
	private static final LocalDate DATE_2020_05_10 = LocalDate.of(2020, 5, 10);
	private static final LocalDate DATE_2020_05_17 = LocalDate.of(2020, 5, 17);

	private static final LocalDate DATE_2020_05_5 = LocalDate.of(2020, 5, 15);
	private static final LocalDate DATE_2020_05_16 = LocalDate.of(2020, 5, 16);
	private static final Quarantine QUARANTINE = Quarantine.of(DATE_2020_05_5, DATE_2020_05_16);

	private static final TrackedCase TRACKED_CASE_A = new TrackedCase(
			new TrackedPerson("Erika", "Musterfrau"),
			CaseType.INDEX,
			DEPARTMENT
	).setQuarantine(QUARANTINE);

	private static final TrackedCase TRACKED_CASE_B = new TrackedCase(
			new TrackedPerson("Max", "Mustermann"),
			CaseType.INDEX,
			DEPARTMENT
	).setQuarantine(QUARANTINE);

	private static final TrackedCase TRACKED_CASE_C = new TrackedCase(
			new TrackedPerson("Olaf", "Beispiel"),
			CaseType.INDEX,
			DEPARTMENT
	).setQuarantine(QUARANTINE);

	private static final TrackedCase TRACKED_CASE_D = new TrackedCase(
			new TrackedPerson("Elfride", "Exampel"),
			CaseType.CONTACT,
			DEPARTMENT
	);
	private static final Streamable<TrackedCase> TRACKED_CASES =
			Streamable.of(TRACKED_CASE_A, TRACKED_CASE_B, TRACKED_CASE_C, TRACKED_CASE_D);
	private static final int ONCE = 1;
	private static final int NEVER = 0;
	private static final int THREE = 3;

	@Mock private TrackedCaseRepository cases;
	@Mock private ActionItemRepository items;

	private QuarantineEndChecker quarantineEndChecker;

	@BeforeEach
	void setUp() {
		quarantineEndChecker = new QuarantineEndChecker(cases, items);
		when(cases.findAll()).thenReturn(TRACKED_CASES);
	}

	@Test
	void expectToCreateThreeQuarantineEndingActionItem() {
		prepareTestDateTime(DATE_2020_05_17);
		prepareNonExistingActionItems();

		quarantineEndChecker.checkEndingQuarantinesPeriodically();

		ArgumentCaptor<QuarantineEndingActionItem> captor = verifyRepositorySaveInteraction(THREE);
		verifyCreatedQuarantineEndingActionItems(captor);
		verifyNonExistenceOfSuitableActionItems();
	}

	@Test
	void expectToCreateNoQuarantineEndingActionItemsDueToDateFiltering() {
		prepareTestDateTime(DATE_2020_05_10);

		quarantineEndChecker.checkEndingQuarantinesPeriodically();

		ArgumentCaptor<QuarantineEndingActionItem> captor = verifyRepositorySaveInteraction(NEVER);
		verifyCreatedQuarantineEndingActionItems(captor);
	}

	@Test
	void expectToCreateOneQuarantineEndingActionItemDueToExistingActionItems() {
		prepareTestDateTime(DATE_2020_05_17);
		prepareTwoExistingActionItems();

		quarantineEndChecker.checkEndingQuarantinesPeriodically();

		ArgumentCaptor<QuarantineEndingActionItem> captor = verifyRepositorySaveInteraction(ONCE);
		verifyCreatedQuarantineEndingActionItems(captor);
		verifyQuarantineEndingActionItemForTrackedCaseC(captor);
	}

	private void prepareTwoExistingActionItems() {
		when(items.findQuarantineEndingActionItemsFor(TRACKED_CASE_A.getTrackedPerson().getId()))
				.thenReturn(ActionItems.of(new QuarantineEndingActionItem()));
		when(items.findQuarantineEndingActionItemsFor(TRACKED_CASE_B.getTrackedPerson().getId()))
				.thenReturn(ActionItems.of(new QuarantineEndingActionItem()));
		when(items.findQuarantineEndingActionItemsFor(TRACKED_CASE_C.getTrackedPerson().getId()))
				.thenReturn(ActionItems.empty());
	}

	private void verifyQuarantineEndingActionItemForTrackedCaseC(
			ArgumentCaptor<QuarantineEndingActionItem> captor) {
		assertThat(captor.getValue())
				.isEqualTo(new QuarantineEndingActionItem(TRACKED_CASE_C.getTrackedPerson().getId()));
	}

	private void prepareTestDateTime(LocalDate date20200510) {
		DateTimeUtils.setCurrentMillisFixed(date20200510.toEpochSecond(LocalTime.MIN, ZoneOffset.UTC));
	}

	private void verifyCreatedQuarantineEndingActionItems(
			ArgumentCaptor<QuarantineEndingActionItem> items) {
		items.getAllValues()
				.forEach(actionItem ->
						assertThat(actionItem.getDescription().getCode())
								.isEqualTo(DescriptionCode.QUARANTINE_ENDING));
	}

	private void verifyNonExistenceOfSuitableActionItems() {
		verify(items).findQuarantineEndingActionItemsFor(TRACKED_CASE_A.getTrackedPerson().getId());
		verify(items).findQuarantineEndingActionItemsFor(TRACKED_CASE_B.getTrackedPerson().getId());
		verify(items).findQuarantineEndingActionItemsFor(TRACKED_CASE_C.getTrackedPerson().getId());
	}

	private void prepareNonExistingActionItems() {
		when(items.findQuarantineEndingActionItemsFor(TRACKED_CASE_A.getTrackedPerson().getId()))
				.thenReturn(
						ActionItems.empty());
		when(items.findQuarantineEndingActionItemsFor(TRACKED_CASE_B.getTrackedPerson().getId()))
				.thenReturn(ActionItems.empty());
		when(items.findQuarantineEndingActionItemsFor(TRACKED_CASE_C.getTrackedPerson().getId()))
				.thenReturn(ActionItems.empty());
	}

	private ArgumentCaptor<QuarantineEndingActionItem> verifyRepositorySaveInteraction(int three) {
		ArgumentCaptor<QuarantineEndingActionItem> quarantineEnding =
				forClass(QuarantineEndingActionItem.class);
		verify(items, times(three)).save(quarantineEnding.capture());
		return quarantineEnding;
	}
}
