package quarano.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.persistence.Embeddable;
import java.util.Random;
import java.util.regex.Pattern;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(staticName = "of")
@Value
@Embeddable
public class EventCode {
	private static final String DEFAULT_ALPHABET = "ABCDEFGHKLMNPQRSTUVWXYZ123456789";
	public static final String REGEX = "[" + DEFAULT_ALPHABET + "]{8}";
	private static final Pattern PATTERN = Pattern.compile(REGEX);
	private final String eventCode;

	/**
	 * Generates a random {@link EventCode} based on the configured alphabet.
	 *
	 * @return will never be {@literal null}.
	 */
	static EventCode generate() {
		StringBuffer buffer = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < 8; i++) {
			buffer.append(DEFAULT_ALPHABET.charAt(random.nextInt(DEFAULT_ALPHABET.length())));
		}
		return new EventCode(buffer.toString());
	}

	public static boolean isValid(String value) {
		return PATTERN.matcher(value).matches();
	}

	@Override
	public String toString() {
		return eventCode;
	}
}
