package quarano.department.web;

import lombok.RequiredArgsConstructor;
import quarano.department.TrackedCaseProperties;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
@JsonAutoDetect(getterVisibility = Visibility.NON_PRIVATE)
class TrackedCaseDefaults {

	private final LocalDate now = LocalDate.now();
	private final TrackedCaseProperties properties;

	LocalDate getTestDate() {
		return now;
	}

	LocalDate getQuarantineStart() {
		return now;
	}

	LocalDate getQuarantineEnd() {
		return now.plus(properties.getQuarantinePeriod());
	}
}
