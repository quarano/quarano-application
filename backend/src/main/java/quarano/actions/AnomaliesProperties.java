package quarano.actions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import quarano.department.CaseType;
import quarano.tracking.BodyTemperature;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author Oliver Drotbohm
 */
@Getter(value = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "quarano.anomalies")
public class AnomaliesProperties {

	private final BodyTemperature temperatureThresholdIndex;
	private final BodyTemperature temperatureThresholdContact;

	/**
	 * @param type The type for which the temperature should be returned.
	 * @return The temperature threshold
	 * @since 1.4
	 */
	public BodyTemperature getTemperatureThreshold(CaseType type) {

		return type.getPrimaryCaseType() == CaseType.INDEX
				? getTemperatureThresholdIndex()
				: getTemperatureThresholdContact();
	}
}
