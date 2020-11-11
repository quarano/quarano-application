package quarano.masterdata;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;

import java.util.Locale;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoIntegrationTest
public class FrontendTextRepositoryIntegrationTests {

	private final FrontendTextRepository texts;

	@Test // CORE-463
	void looksUpFrontendTextByKey() {
		assertThat(texts.findByTextKey(FrontendText.Keys.WELCOME_INDEX, Locale.GERMAN)).isPresent();
	}
}
