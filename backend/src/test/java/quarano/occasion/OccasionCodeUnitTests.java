package quarano.occasion;

import static org.assertj.core.api.Assertions.*;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

/**
 * Unit tests for {@link OccasionCode}.
 *
 * @author Oliver Drotbohm
 */
class OccasionCodeUnitTests {

	@TestFactory // CORE-631
	Stream<DynamicTest> doesNotContainAmbiguousCharacters() {

		var ambiguousCharacters = Stream.of("O", "0", "I", "J", "1");

		return DynamicTest.stream(ambiguousCharacters, Function.identity(), it -> {

			IntStream.range(0, 10000).forEach(__ -> {
				assertThat(OccasionCode.generate().getOccasionCode()).doesNotContain(it);
			});
		});
	}
}
