package quarano.tracking;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Oliver Drotbohm
 */
@Embeddable
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ZipCode {

	public static final String PATTERN = "[0-9]{5}";
	private static final Pattern REGEX = Pattern.compile("[0-9]{5}");

	private final @Column(name = "zipcode") String value;

	/**
	 * Creates a new {@link ZipCode} from the given source string.
	 *
	 * @param source must not be {@literal null}.
	 * @return
	 */
	public static ZipCode of(String source) {

		if (!isValid(source)) {
			throw new IllegalArgumentException(String.format("%s is not a valid zip code!", source));
		}

		return new ZipCode(source);
	}

	public static boolean isValid(String source) {
		return REGEX.matcher(source).matches();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return value;
	}
}
