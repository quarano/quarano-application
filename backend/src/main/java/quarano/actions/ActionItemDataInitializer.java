package quarano.actions;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItem.ItemType;
import quarano.core.DataInitializer;
import quarano.department.TrackedCase;
import quarano.diary.DiaryManagement;
import quarano.diary.Slot;
import quarano.tracking.BodyTemperature;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;

/**
 * @author Oliver Drotbohm
 */
@Order(800)
@Component
@RequiredArgsConstructor
public class ActionItemDataInitializer implements DataInitializer {

	private final TrackedPersonRepository people;
	private final DiaryManagement diaries;
	private final ActionItemRepository items;
	private final AnomaliesProperties config;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		TrackedPerson sandra = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2).orElseThrow();
		TrackedPerson jessica = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON4_ID_DEP2).orElseThrow();
		TrackedPerson gustav = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON4_ID_DEP1).orElseThrow();
		TrackedPerson nadine = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON5_ID_DEP1).orElseThrow();

		TrackedPerson joel = people.findById(TrackedPersonDataInitializer.INDEX_PERSON_DELETE1_DEP1).orElseThrow();
		TrackedPerson carlos = people.findById(TrackedPersonDataInitializer.CONTACT_PERSON_DELETE1_DEP1).orElseThrow();

		Slot previous = Slot.now().previous();
		items.save(new DiaryEntryMissingActionItem(sandra, getCaseOf(sandra), previous));
		items.save(new DiaryEntryMissingActionItem(jessica, getCaseOf(jessica), previous));
		items.save(new DiaryEntryActionItem(gustav, getCaseOf(gustav), diaries.findDiaryFor(gustav).iterator().next(),
				ItemType.MEDICAL_INCIDENT,
				Description.forIncreasedTemperature(BodyTemperature.of(40.1f), config.getTemperatureThreshold())));
		items.save(new DiaryEntryActionItem(nadine, getCaseOf(nadine), diaries.findDiaryFor(nadine).iterator().next(),
				ItemType.MEDICAL_INCIDENT,
				Description.forIncreasedTemperature(BodyTemperature.of(40.1f), config.getTemperatureThreshold())));

		items.save(new DiaryEntryMissingActionItem(joel, getCaseOf(joel), previous));
		items.save(new DiaryEntryMissingActionItem(carlos, getCaseOf(carlos), previous));
	}

	private TrackedCase getCaseOf(TrackedPerson sandra) {
		var trackedCase = sandra.getTrackedCases().get(0);
		Assert.notNull(trackedCase, "Person has no tracked case relation");
		return trackedCase;
	}
}
