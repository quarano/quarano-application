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

	public static final String NAME = "[\\p{L}\\s\\-\\'\\`\\´]*";
	public static final String LETTERS = "[\\p{L}\\s]*";
	public static final String LETTERS_AND_NUMBERS = "[\\p{L}\\s\\d]*";
	public static final String TEXTUAL = "[\\p{L}\\s\\d\\.\\,\\-\\n\\r]*";
}
