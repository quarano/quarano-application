package quarano.department;

import static org.assertj.core.api.Assertions.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.actions.ActionItem;
import quarano.actions.ActionItemRepository;
import quarano.diary.DiaryEntry;
import quarano.diary.DiaryEntryRepository;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Jens Kutzsche
 * @since 1.4
 */
@QuaranoWebIntegrationTest
@RequiredArgsConstructor
@SpringBootTest(properties = { "quarano.date-time.delta = -6m-1d" })
class GDPRAnonymizationJobIntegrationTests {

	Predicate<String> anonym = Pattern.compile("^[#\\s]*$").asPredicate();
	Condition<Object> anonymized = new Condition<>(
			it -> it == null || (it instanceof String && (it.equals("###") || anonym.test((String) it))),
			"data is anonymized");

	private final GDPRAnonymizationJob gdprAnonymizationJob;
	private final TrackedCaseRepository cases;
	private final @NonNull ActionItemRepository actions;
	private final @NonNull DiaryEntryRepository diaries;

	@Test // CORE-294
	void testGDPRAnonymizationJob() {

		var allCases = cases.findAll().map(TrackedCase::fillSampleData).toList();
		cases.saveAll(allCases);

		var allDiaries = StreamSupport.stream(diaries.findAll().spliterator(), false).map(DiaryEntry::fillSampleData)
				.collect(Collectors.toList());
		diaries.saveAll(allDiaries);

		var allActions = actions.findAll().map(ActionItem::fillSampleData).toList();
		actions.saveAll(allActions);

		verifyCases(not(anonymized));
		verifyDiaries(not(anonymized));
		verifyActions(not(anonymized));

		gdprAnonymizationJob.anonymizeCasesAndRelatedData();

		verifyCases(anonymized);
		verifyDiaries(anonymized);
		verifyActions(anonymized);
	}

	private void verifyCases(Condition<Object> condition) {
		assertThat(cases.findAll())
				.filteredOn(it -> it.getTrackedPerson().getAccount().isPresent())
				.filteredOn(it -> it.getQuestionnaire() != null)
				.first()
				.satisfies(it -> {

					assertThat(it.getExtReferenceNumber()).is(condition);

					assertThat(it.getTrackedPerson()).satisfies(p -> {

						assertThat(p.getFullName()).is(condition);
						assertThat(p.getDateOfBirth()).is(condition);
						assertThat(p.getPhoneNumber()).is(condition);
						assertThat(p.getMobilePhoneNumber()).is(condition);
						assertThat(p.getEmailAddress()).is(condition);
						assertThat(p.getAddress().getStreet()).is(condition);
						assertThat(p.getAddress().getHouseNumber()).is(condition);
					});

					assertThat(it.getTrackedPerson().getAccount()).hasValueSatisfying(a -> {

						assertThat(a.getFullName()).is(condition);
						assertThat(a.getUsername()).is(condition);
						assertThat(a.getEmail()).is(condition);
						assertThat(a.getPassword().toString()).is(condition);
					});

					assertThat(it.getComments()).allSatisfy(c -> {

						assertThat(c.getAuthor()).is(condition);
						assertThat(c.getText()).is(condition);
					});

					assertThat(it.getQuestionnaire()).satisfies(q -> {

						assertThat(q.getFamilyDoctor()).is(condition);
						assertThat(q.getGuessedOriginOfInfection()).is(condition);
						assertThat(q.getHasPreExistingConditionsDescription()).is(condition);
						assertThat(q.getBelongToMedicalStaffDescription()).is(condition);
						assertThat(q.getHasContactToVulnerablePeopleDescription()).is(condition);
					});
				});
	}

	private void verifyDiaries(Condition<Object> condition) {
		assertThat(diaries.findAll()).first().satisfies(it -> assertThat(it.getNote()).is(condition));
	}

	private void verifyActions(Condition<Object> condition) {
		assertThat(actions.findAll()).first()
				.satisfies(it -> assertThat(it.getDescription().getArguments()).allMatch(condition::matches));
	}
}
