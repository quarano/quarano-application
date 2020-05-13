package quarano.department;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

import javax.persistence.Embeddable;

/**
 * @author Oliver Drotbohm
 */
@Embeddable
@Getter
@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class TestResult {

	private final @Getter boolean infected;
	private final @Getter LocalDate testDate;

	public static TestResult infected(LocalDate date) {
		return new TestResult(true, date);
	}

	public static TestResult notInfected(LocalDate date) {
		return new TestResult(true, date);
	}
}
