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
package quarano.actions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor()
public enum DescriptionCode {

	INCREASED_TEMPERATURE,

	FIRST_CHARACTERISTIC_SYMPTOM(2.0f),

	DIARY_ENTRY_MISSING,

	MISSING_DETAILS_INDEX,
	
	INITIAL_CALL_OPEN_INDEX;

	private final @Getter float multiplier;

	private DescriptionCode() {
		this(1f);
	}
}
