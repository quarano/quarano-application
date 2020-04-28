package quarano.actions;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.department.CaseType;
import quarano.department.Department;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseUpdated;
import quarano.tracking.EmailAddress;
import quarano.tracking.PhoneNumber;
import quarano.tracking.TrackedPerson;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.util.Streamable;

@QuaranoUnitTest
class ActionItemEventListenerTests {

	@Mock ActionItemRepository items;
	@Mock AnomaliesProperties config;

	ActionItemEventListener listener;

	@BeforeEach
	void setup() {
		listener = new ActionItemEventListener(items, config);
	}

	@TestFactory
	Stream<DynamicTest> createNewTrackedCaseActionItem() {

		var now = LocalDate.now();

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

		return DynamicTest.stream(trackedPersons.entrySet().iterator(),
				(entry) -> String.format("Saves MISSING_DETAILS_INDEX action item for %s", entry.getKey()), (entry) -> {

					var person = entry.getValue();
					var personId = person.getId();
					var trackedCase = new TrackedCase(person, CaseType.INDEX, new Department("test"));
					when(items.findByDescriptionCode(personId, DescriptionCode.MISSING_DETAILS_INDEX))
							.thenReturn(Streamable.empty());

					assertThatCode(() -> listener.on(TrackedCaseUpdated.of(trackedCase))) //
							.doesNotThrowAnyException();

					var actionItemCaptor = ArgumentCaptor.forClass(TrackedCaseActionItem.class);
					verify(items, times(1)).save(actionItemCaptor.capture());

					var actionItem = actionItemCaptor.getValue();

					assertThat(actionItem.getPersonIdentifier()).isEqualTo(personId);
					assertThat(actionItem.getCaseIdentifier()).isEqualTo(trackedCase.getId());
					assertThat(actionItem.getDescription().getCode()).isEqualTo(DescriptionCode.MISSING_DETAILS_INDEX);

					Mockito.reset(items, config);
				});
	}

	@Test
	void doesNotCreateNewTrackedCaseActionItemForExisting() {

		var person = new TrackedPerson("firstName", "lastName");
		var personId = person.getId();
		var trackedCase = new TrackedCase(person, CaseType.INDEX, new Department("test"));
		var event = TrackedCaseUpdated.of(trackedCase);

		when(items.findByDescriptionCode(personId, DescriptionCode.MISSING_DETAILS_INDEX))
				.thenReturn(Streamable.of(new TrackedCaseActionItem(null, null, ActionItem.ItemType.PROCESS_INCIDENT,
						DescriptionCode.MISSING_DETAILS_INDEX)));

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		verify(items, times(0)).save(any());
	}

	@Test
	void doesNotCreateNewTrackedCaseActionItemForWrongType() {

		var person = new TrackedPerson("firstName", "lastName");
		var trackedCase = new TrackedCase(person, CaseType.CONTACT, new Department("test"));
		var event = TrackedCaseUpdated.of(trackedCase);

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		verify(items, times(0)).findByDescriptionCode(any(), any());
		verify(items, times(0)).save(any());
	}

	@Test
	void doesNotCreateNewTrackedCaseActionItemForConcludedCase() {

		var person = new TrackedPerson("firstName", "lastName");
		var trackedCase = spy(new TrackedCase(person, CaseType.INDEX, new Department("test")));
		var event = TrackedCaseUpdated.of(trackedCase);

		when(trackedCase.isConcluded()).thenReturn(true);

		assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

		verify(items, times(0)).findByDescriptionCode(any(), any());
		verify(items, times(0)).save(any());
	}
}
