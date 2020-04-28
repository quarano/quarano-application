package quarano.actions;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import quarano.QuaranoUnitTest;
import quarano.department.CaseStatus;
import quarano.department.CaseType;
import quarano.department.Department;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseUpdated;
import quarano.tracking.EmailAddress;
import quarano.tracking.PhoneNumber;
import quarano.tracking.TrackedPerson;

import java.time.LocalDate;
import java.util.Collections;
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
class ActionItemEventListenerTest {
    @Mock ActionItemRepository items;
    @Mock AnomaliesProperties config;

    private ActionItemEventListener listener;

    @BeforeEach
    void setup() {
        listener = new ActionItemEventListener(items, config);
    }

    @TestFactory
    Stream<DynamicTest> createNewTrackedCaseActionItem() {
        var trackedPersons = Map.of(
                "only firstName and lastName", new TrackedPerson("firstName", "lastName")
                , "empty phoneNumber and mobilenumber", new TrackedPerson("firstName", "lastName", EmailAddress.of("test@test.de"), null, null, null, LocalDate.now(), null, Collections.emptyList(), Collections.emptyList())
                , "empty emailAddress", new TrackedPerson("firstName", "lastName", null, PhoneNumber.of("0123901"), PhoneNumber.of("0123901293"), null, LocalDate.now(), null, Collections.emptyList(), Collections.emptyList())
                , "empty dateOfBirth", new TrackedPerson("firstName", "lastName", EmailAddress.of("test@test.de"), PhoneNumber.of("0123901"), PhoneNumber.of("0123901293"), null, null, null, Collections.emptyList(), Collections.emptyList())
        );

        return DynamicTest.stream(
                trackedPersons.entrySet().iterator(),
                (entry) -> String.format("Saves MISSING_DETAILS_INDEX action item for %s", entry.getKey()),
                (entry) -> {
                    var person = entry.getValue();
                    var personId = person.getId();
                    var trackedCase = new TrackedCase(person, CaseType.INDEX, new Department("test"));
                    var caseId = trackedCase.getId();
                    var event = TrackedCaseUpdated.of(trackedCase);

                    when(items.findByDescriptionCode(personId, DescriptionCode.MISSING_DETAILS_INDEX)).thenReturn(Streamable.empty());

                    assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

                    var actionItemCaptor = ArgumentCaptor.forClass(TrackedCaseActionItem.class);
                    verify(items, times(1)).save(actionItemCaptor.capture());

                    var actionItem = actionItemCaptor.getValue();

                    assertThat(actionItem.getPersonIdentifier()).isEqualTo(personId);
                    assertThat(actionItem.getCaseIdentifier()).isEqualTo(caseId);
                    assertThat(actionItem.getDescription().getCode()).isEqualTo(DescriptionCode.MISSING_DETAILS_INDEX);

                    Mockito.reset(items, config);
                }
        );
    }

    @Test
    void doesNotCreateNewTrackedCaseActionItemForExisting() {
        var person = new TrackedPerson("firstName", "lastName");
        var personId = person.getId();
        var trackedCase = new TrackedCase(person, CaseType.INDEX, new Department("test"));
        var caseId = trackedCase.getId();
        var event = TrackedCaseUpdated.of(trackedCase);

        when(items.findByDescriptionCode(personId, DescriptionCode.MISSING_DETAILS_INDEX)).thenReturn(Streamable.of(
                new TrackedCaseActionItem(null, null, ActionItem.ItemType.PROCESS_INCIDENT, DescriptionCode.MISSING_DETAILS_INDEX)
        ));

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
    void doesNotCreateNewTrackedCaseActionItemForStoppedCase() {
        var person = new TrackedPerson("firstName", "lastName");
        var trackedCase = spy(new TrackedCase(person, CaseType.INDEX, new Department("test")));
        var event = TrackedCaseUpdated.of(trackedCase);

        when(trackedCase.resolveStatus()).thenReturn(CaseStatus.STOPPED);

        assertThatCode(() -> listener.on(event)).doesNotThrowAnyException();

        verify(items, times(0)).findByDescriptionCode(any(), any());
        verify(items, times(0)).save(any());
    }
}
