package quarano.actions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import quarano.tracking.BodyTemperature;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author Oliver Drotbohm
 */
@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "quarano.anomalies")
public class AnomaliesProperties {

	private final BodyTemperature temperatureThreshold;
}
