package quarano.tracking;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
class BodyTemperatureUnitTests {

	@Test
	void rendersToStringWithComma() {

		BodyTemperature temperature = BodyTemperature.of(37.5f);

		assertThat(temperature.toString()).isEqualTo("37,5°C");
	}
}
