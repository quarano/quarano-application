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
package quarano.diary.web;

import quarano.core.web.MappingCustomizer;
import quarano.diary.DiaryEntry;
import quarano.diary.web.DiaryRepresentations.DiaryEntryInput;
import quarano.reference.Symptom;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@Order(20)
class DiaryMappingConfiguration implements MappingCustomizer {

	/*
	 * (non-Javadoc)
	 * @see quarano.core.web.MappingCustomizer#customize(org.modelmapper.ModelMapper)
	 */
	@Override
	public void customize(ModelMapper mapper) {

		mapper.typeMap(DiaryEntryInput.class, DiaryEntry.class).addMappings(it -> {

			it.with(request -> new ArrayList<>()) //
					.<List<Symptom>> map(DiaryEntryInput::getSymptoms, (target, v) -> target.setSymptoms(v));
		});
	}
}
