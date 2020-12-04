package quarano.masterdata;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.core.EmailTemplates;

import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Integration tests for {@link DatabaseEmailTemplates}.
 *
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
@RequiredArgsConstructor
@QuaranoIntegrationTest
public class DatabaseEmailTemplatesIntegrationTests {

	private final DatabaseEmailTemplates templates;

	@Test // CORE-463
	void addsGermanTranslationIfLocaleIsNotTheDefaultOne() {

		assertThat(templates.expandTemplate(EmailTemplates.Keys.DIARY_REMINDER, Map.of(), Locale.GERMAN))
				.contains(TemplatedWithFallback.SEPARATOR); // temporary change because of CORE-550
		// .doesNotContain(EmailText.SEPARATOR);

		assertThat(templates.expandTemplate(EmailTemplates.Keys.DIARY_REMINDER, Map.of(), Locale.ENGLISH))
				.contains(TemplatedWithFallback.SEPARATOR);
	}
}
