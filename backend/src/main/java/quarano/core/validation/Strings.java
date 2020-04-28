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
package quarano.core.validation;

/**
 * @author Oliver Drotbohm
 */
public class Strings {

	static final String LETTERS = "[\\p{L}\\s]*";
	static final String LETTERS_AND_NUMBERS = "[\\p{L}\\s\\d]*";
	static final String TEXTUAL = "[\\p{L}\\s\\d\\.\\,\\?\\!\\(\\)\\-\\n\\r]*";

	private static final String LETTERS_AND_SPACES = "\\p{L}\\s";
	private static final String DOTS = "\\.";
	private static final String DASHES = "\\-";
	private static final String PARENTHESIS = "\\(\\)";
	private static final String SLASHES = "\\/";

	public static final String CITY = "[" + LETTERS_AND_SPACES + DOTS + DASHES + PARENTHESIS + SLASHES + "]*";
	public static final String STREET = "[" + LETTERS_AND_SPACES + DOTS + DASHES + "]*";
}
