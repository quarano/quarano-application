/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
