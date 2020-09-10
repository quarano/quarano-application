package quarano.core;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Oliver Drotbohm
 */
public interface EmailTemplates {

	/**
	 * Expands the template with the given {@link Key} and placeholders.
	 *
	 * @param key must not be {@literal null}.
	 * @param placeholders must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	String expandTemplate(Key key, Map<String, Object> placeholders);

	/**
	 * A template key.
	 *
	 * @author Oliver Drotbohm
	 */
	interface Key {

		/**
		 * Resolves the current {@link Key} to a file that can be loaded via a
		 * {@link org.springframework.core.io.ResourceLoader}.
		 *
		 * @param pattern must not be {@literal null} or empty.
		 * @return
		 */
		String toFileName(String pattern);
	}

	/**
	 * Predefined keys to be usable with the application.
	 *
	 * @author Oliver Drotbohm
	 */
	enum Keys implements Key {

		REGISTRATION_INDEX, REGISTRATION_CONTACT, NEW_CONTACT_CASE, DIARY_REMINDER;

		/**
		 * Returns all {@link Key}s as a {@link Stream}.
		 *
		 * @return
		 */
		public static Stream<Key> stream() {
			return Arrays.stream(values());
		}

		/*
		 * (non-Javadoc)
		 * @see quarano.core.EmailTemplates.Key#toFileName(java.lang.String)
		 */
		@Override
		public String toFileName(String pattern) {

			var localName = name().toLowerCase(Locale.US).replace('_', '-').concat(".txt");

			return String.format(pattern, localName);
		}
	}
}
