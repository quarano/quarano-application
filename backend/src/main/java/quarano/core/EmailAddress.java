package quarano.core;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

import javax.persistence.Embeddable;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * @author Oliver Drotbohm
 */
@Embeddable
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class EmailAddress {

	public static final String PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
	private static final Pattern REGEX = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE);

	private final String email;

	/**
	 * Creates a new {@link EmailAddress} for the given {@link String} source.
	 *
	 * @param email
	 * @return
	 */
	public static EmailAddress of(String email) {

		if (!isValid(email)) {
			throw new IllegalArgumentException(String.format("%s is not a valid email address!", email));
		}

		return new EmailAddress(email);
	}

	@Nullable
	@SuppressWarnings("null")
	public static EmailAddress ofNullable(@Nullable String email) {
		return StringUtils.hasText(email) ? of(email) : null;
	}

	/**
	 * Returns whether the given email address candidate is a valid one.
	 *
	 * @param candidate can be {@literal null}.
	 * @return
	 */
	public static boolean isValid(@Nullable String candidate) {
		return candidate != null && REGEX.matcher(candidate).matches();
	}

	/**
	 * Returns whether the email address ends with given {@link String}.
	 *
	 * @param suffix can be {@literal null}.
	 * @return
	 */
	public boolean endsWith(@Nullable String suffix) {
		return suffix != null && email.endsWith(suffix);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return email;
	}
}
