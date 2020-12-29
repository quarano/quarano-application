package quarano.tracking;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Jens Kutzsche
 * @since 1.4
 */
@QuaranoWebIntegrationTest
@RequiredArgsConstructor
@SpringBootTest(properties = { "quarano.date-time.delta = -17d" })
class GDPRAnonymizationJobIntegrationTests {

	Predicate<String> anonym = Pattern.compile("^[#\\s]*$").asPredicate();
	Condition<Object> anonymized = new Condition<>(
			it -> it == null || (it instanceof String && (it.equals("###") || anonym.test((String) it))),
			"data is anonymized");

	private final GDPRAnonymizationJob gdprAnonymizationJob;
	private final ContactPersonRepository contacts;

	@Test // CORE-294
	void testGDPRAnonymizationJob() {

		var allContacts = contacts.findAll().map(ContactPerson::fillSampleData).toList();

		contacts.saveAll(allContacts);

		verifyContacts(not(anonymized));

		gdprAnonymizationJob.anonymizeContactsAndRelatedData();

		verifyContacts(anonymized);
	}

	private void verifyContacts(Condition<Object> condition) {
		assertThat(contacts.findAll())
				.first()
				.satisfies(it -> {

					assertThat(it.getFullName()).is(condition);
					assertThat(it.getPhoneNumber()).is(condition);
					assertThat(it.getMobilePhoneNumber()).is(condition);
					assertThat(it.getEmailAddress()).is(condition);
					assertThat(it.getAddress().getStreet()).is(condition);
					assertThat(it.getAddress().getHouseNumber()).is(condition);
					assertThat(it.getIdentificationHint()).is(condition);
					assertThat(it.getRemark()).is(condition);
				});
	}
}
