package quarano.department;

import lombok.Value;

import java.time.Period;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author Oliver Drotbohm
 */
@ConstructorBinding
@Value
@ConfigurationProperties("quarano.cases")
public class TrackedCaseProperties {

	private final boolean executeContactRetroForContactCases;
	private final Period quarantinePeriod;

	public Period getQuarantinePeriod() {
		return quarantinePeriod == null ? Period.ofWeeks(2) : quarantinePeriod;
	}
}
