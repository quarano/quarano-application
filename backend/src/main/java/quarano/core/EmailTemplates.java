package quarano.core;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
public interface EmailTemplates {

	/**
	 * Expands the template with the given {@link Key} and placeholders in the language of the given locale.
	 *
	 * @param key must not be {@literal null}.
	 * @param placeholders must not be {@literal null}.
	 * @param locale can be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	String expandTemplate(Key key, Map<String, Object> placeholders, Locale locale);

	/**
	 * A template key.
	 *
	 * @author Oliver Drotbohm
	 * @author Jens Kutzsche
	 */
	interface Key {

		/**
		 * Resolves the current {@link Key} to a file that can be loaded via a
		 * {@link org.springframework.core.io.ResourceLoader}.
		 *
		 * @return
		 */
		String toFileName();

		/**
		 * @return The current {@link Key} as String.
		 */
		String getKey();
	}

	/**
	 * Predefined keys to be usable with the application.
	 *
	 * @author Oliver Drotbohm
	 * @author Jens Kutzsche
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
		 * @see quarano.core.EmailTemplates.Key#toFileName()
		 */
		@Override
		public String toFileName() {
			return determineName().concat(".txt");
		}

		@Override
		public String getKey() {
			return determineName();
		}

		private String determineName() {
			return name().toLowerCase(Locale.US).replace('_', '-');
		}
	}
}
