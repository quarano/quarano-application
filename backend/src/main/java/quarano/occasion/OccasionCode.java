package quarano.occasion;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.Embeddable;

import org.springframework.util.Assert;

/**
 * @author David Bauknecht
 * @author Oliver Drotbohm
 * @since 1.4
 */
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(staticName = "of")
@Value
@Embeddable
public class OccasionCode {

	private static final String ALPHABET = "ABCDEFGHKLMNPQRSTUVWXYZ23456789";
	public static final String REGEX = "[" + ALPHABET + "]{8}";
	private static final Pattern PATTERN = Pattern.compile(REGEX);

	private final String occasionCode;

	/**
	 * Returns whether the given {@link String} is a valid {@link OccasionCode}.
	 *
	 * @param candidate must not be {@literal null}.
	 * @return
	 */
	public static boolean isValid(String candidate) {

		Assert.notNull(candidate, "Candidate must not be null!");

		return PATTERN.matcher(candidate).matches();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return occasionCode;
	}

	/**
	 * Generates a random {@link OccasionCode} based on the configured alphabet.
	 *
	 * @return will never be {@literal null}.
	 */
	static OccasionCode generate() {

		var random = new Random();
		var maxIndex = ALPHABET.length();

		return new OccasionCode(IntStream.range(0, 8)
				.mapToObj(__ -> ALPHABET.charAt(random.nextInt(maxIndex)))
				.map(Object::toString)
				.collect(Collectors.joining()));
	}
}
