package quarano.actions;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.account.Department;
import quarano.core.AuditingMetadata;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.department.CaseType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.CaseUpdated;
import quarano.tracking.TrackedPerson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

@QuaranoUnitTest
class TrackedCaseEventListenerTests {

	@Mock ActionItemRepository items;

	TrackedCaseEventListener listener;

	@BeforeEach
	void setup() {
		listener = new TrackedCaseEventListener(items);
	}

	@AfterEach
	void resetMocks() {
		reset(items);
	}

	@TestFactory
	Stream<DynamicTest> createNewMissingDetailsActionItemOnFirstSaveForIndexCases() {
		return testFactoryForMissingDetails(CaseType.INDEX);
	}

	@TestFactory
	Stream<DynamicTest> createNewMissingDetailsActionItemOnFirstSaveForContactCases() {
		return testFactoryForMissingDetails(CaseType.CONTACT);
	}

	private Stream<DynamicTest> testFactoryForMissingDetails(final CaseType caseType) {
		final var descriptionCode = caseType == CaseType.INDEX ? DescriptionCode.MISSING_DETAILS_INDEX
				: DescriptionCode.MISSING_DETAILS_CONTACT;

		return DynamicTest.stream(createTrackedPersons(LocalDate.now()).entrySet().iterator(),
				(entry) -> String.format("Saves %s action item for %s", descriptionCode, entry.getKey()), (entry) -> {

					var person = entry.getValue();
					var personId = person.getId();
					var trackedCase = trackedCase(person, caseType);
					when(items.findByDescriptionCode(personId, descriptionCode)).thenReturn(ActionItems.empty());

					assertThatCode(() -> listener.on(CaseUpdated.of(trackedCase))) //
							.doesNotThrowAnyException();

					var actionItemCaptor = ArgumentCaptor.forClass(TrackedCaseActionItem.class);
					verify(items, atLeastOnce()).save(actionItemCaptor.capture());

					var actionItem = actionItemCaptor.getValue();

					assertThat(actionItem.getPersonIdentifier()).isEqualTo(personId);
					assertThat(actionItem.getCaseIdentifier()).isEqualTo(trackedCase.getId());
					assertThat(actionItem.getDescription().getCode()).isEqualTo(descriptionCode);
				});
	}

	@Test
	void doesNotCreateNewTrackedCaseActionItemForExisting() {

		var person = new TrackedPerson("firstName", "lastName");
		var personId = person.getId();
		var trackedCase = trackedCase(person, CaseType.INDEX);
		var event = CaseUpdated.of(trackedCase);

		when(items.findByDescriptionCode(personId, DescriptionCode.MISSING_DETAILS_INDEX))
				.thenReturn(ActionItems.of(new TrackedCaseActionItem(null, null, ActionItem.ItemType.PROCESS_INCIDENT,
						DescriptionCode.MISSING_DETAILS_INDEX)));

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		var itemCaptor = ArgumentCaptor.forClass(ActionItem.class);

		verify(items).save(itemCaptor.capture());

		assertThat(itemCaptor.getAllValues()).hasSize(1);
		assertThat(itemCaptor.getValue().getDescription().getCode())
				.isNotEqualByComparingTo(DescriptionCode.MISSING_DETAILS_INDEX);
	}

	@Test
	void doesNotCreateNewTrackedCaseActionItemForConcludedCase() {

		var person = new TrackedPerson("firstName", "lastName");
		var trackedCase = trackedCase(person, CaseType.INDEX);
		when(trackedCase.getStatus()).thenReturn(TrackedCase.Status.CONCLUDED);
		when(trackedCase.isConcluded()).thenReturn(true);

		var event = CaseUpdated.of(trackedCase);

		when(trackedCase.isConcluded()).thenReturn(true);

		Stream.of(DescriptionCode.INITIAL_CALL_OPEN_INDEX, DescriptionCode.MISSING_DETAILS_INDEX) //
				.forEach(it -> doReturn(ActionItems.empty()).when(items) //
						.findByDescriptionCode(person.getId(), it));

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		verify(items, times(2)).findByDescriptionCode(any(), any());
		verify(items, times(0)).save(any());
	}

	@Test
	void createInitialCallOpenActionItemForIndex() {

		var trackedPerson = createTrackedPersonWithMinimalData();

		var trackedCase = trackedCase(trackedPerson, CaseType.INDEX);
		var event = CaseUpdated.of(trackedCase);

		when(items.findByDescriptionCode(trackedPerson.getId(), DescriptionCode.MISSING_DETAILS_INDEX))
				.thenReturn(ActionItems.empty());

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		var actionItemCaptor = ArgumentCaptor.forClass(TrackedCaseActionItem.class);
		// one for "initial-call-action" and one for "missing-data-action"
		verify(items, times(2)).save(actionItemCaptor.capture());

		var actionItems = actionItemCaptor.getAllValues();

		assertThat(actionItems.stream().findAny()
				.filter(it -> it.getDescription().getCode() == DescriptionCode.INITIAL_CALL_OPEN_INDEX).isPresent());
		assertThat(actionItems.stream()).allMatch(it -> it.getCaseIdentifier().equals(trackedCase.getId()));
	}

	@Test
	void createInitialCallOpenActionItemForContacts() {

		var trackedPerson = createTrackedPersonWithMinimalData();

		var trackedCase = trackedCase(trackedPerson, CaseType.CONTACT);
		var event = CaseUpdated.of(trackedCase);

		when(items.findByDescriptionCode(trackedPerson.getId(), DescriptionCode.MISSING_DETAILS_CONTACT))
				.thenReturn(ActionItems.empty());

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		var actionItemCaptor = ArgumentCaptor.forClass(TrackedCaseActionItem.class);
		// one for "initial-call-action" and one for "missing-data-action"
		verify(items, times(2)).save(actionItemCaptor.capture());

		var actionItems = actionItemCaptor.getAllValues();

		assertThat(actionItems.stream().findAny()
				.filter(it -> it.getDescription().getCode() == DescriptionCode.INITIAL_CALL_OPEN_CONTACT).isPresent());
		assertThat(actionItems.stream()).allMatch(it -> it.getCaseIdentifier().equals(trackedCase.getId()));
	}

	@Test
	void createNoInitialCallOpenActionItemIfStatusIsNotOpen() {

		var trackedPerson = createTrackedPersonWithMinimalData();

		var trackedCase = trackedCase(trackedPerson, CaseType.INDEX);
		when(trackedCase.getStatus()).thenReturn(TrackedCase.Status.REGISTERED);
		var event = CaseUpdated.of(trackedCase);

		Stream.of(DescriptionCode.INITIAL_CALL_OPEN_INDEX, DescriptionCode.MISSING_DETAILS_INDEX) //
				.forEach(it -> doReturn(ActionItems.empty()).when(items) //
						.findByDescriptionCode(trackedPerson.getId(), it));

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		var actionItemCaptor = ArgumentCaptor.forClass(TrackedCaseActionItem.class);
		// one for "initial-call-action" and one for "missing-data-action"
		verify(items).save(actionItemCaptor.capture());

		var actionItem = actionItemCaptor.getValue();

		assertThat(actionItem.getCaseIdentifier()).isEqualTo(trackedCase.getId());
		assertThat(actionItem.getDescription().getCode()).isNotEqualTo(DescriptionCode.INITIAL_CALL_OPEN_INDEX);
	}

	private static TrackedPerson createTrackedPersonWithMinimalData() {

		return new TrackedPerson("firstName", "lastName") //
				.setMobilePhoneNumber(PhoneNumber.of("0125125464565"));
	}

	private static Map<String, TrackedPerson> createTrackedPersons(LocalDate now) {

		var trackedPersons = Map.of(//
				"only firstName and lastName", //
				new TrackedPerson("firstName", "lastName"), //
				"empty phoneNumber and mobilenumber", //
				new TrackedPerson("firstName", "lastName") //
						.setEmailAddress(EmailAddress.of("test@test.de")) //
						.setDateOfBirth(now), //
				"empty emailAddress", new TrackedPerson("firstName", "lastName") //
						.setPhoneNumber(PhoneNumber.of("0123901")) //
						.setMobilePhoneNumber(PhoneNumber.of("0123901293")) //
						.setDateOfBirth(now), //
				"empty dateOfBirth", new TrackedPerson("firstName", "lastName") //
						.setEmailAddress(EmailAddress.of("test@test.de")) //
						.setPhoneNumber(PhoneNumber.of("0123901")) //
						.setMobilePhoneNumber(PhoneNumber.of("0123901293")));

		return trackedPersons;
	}

	private static TrackedCase trackedCase(TrackedPerson person, CaseType caseType) {

		AuditingMetadata auditingMetadata = mock(AuditingMetadata.class);
		lenient().when(auditingMetadata.getCreated()).then((invocation) -> LocalDateTime.now());

		TrackedCase trackedCase = spy(new TrackedCase(person, caseType, new Department("test")));
		lenient().when(trackedCase.getMetadata()).thenReturn(auditingMetadata);

		return trackedCase;
	}
}
