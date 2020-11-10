package quarano.core;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author Oliver Drotbohm
 */
@Embeddable
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class PhoneNumber {

	private static final String REMOVABLE_CHARACTERS_REGEX = "\\(\\)\\s\\-";
	private static final String REMOVABLE_CHARACTERS_SET = "[" + REMOVABLE_CHARACTERS_REGEX + "]";
	public static final String PATTERN = "^[\\+\\(\\)\\s\\-0-9]*?$";
	private static final Pattern REGEX = Pattern.compile(PATTERN);

	private final @Column(name = "phoneNumber") String value;

	public static PhoneNumber of(String number) {

		Assert.hasText(number, "Phone number must not be null or empty!");

		if (!isValid(number)) {
			throw new IllegalArgumentException(String.format("%s is not a valid phone number!", number));
		}

		return new PhoneNumber(number.replaceAll("\\s", "")
				.replace("(0)", "")
				.replaceAll(REMOVABLE_CHARACTERS_SET, "")
				.replace("+", "00")
				.replaceAll("^0049", "0"));
	}

	@Nullable
	@SuppressWarnings("null")
	public static PhoneNumber ofNullable(@Nullable String number) {
		return StringUtils.hasText(number) ? of(number) : null;
	}

	public static boolean isValid(@Nullable String candidate) {
		return StringUtils.hasText(candidate) && REGEX.matcher(candidate).matches();
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
