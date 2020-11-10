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
package quarano.masterdata;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.core.web.MappingCustomizer;

import org.modelmapper.ModelMapper;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Jens Kutzsche
 */
@Component
@Order(10)
@RequiredArgsConstructor
class SymptomMappingConfiguration implements MappingCustomizer {

	private final @NonNull MessageSourceAccessor messages;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.web.MappingCustomizer#customize(org.modelmapper.ModelMapper)
	 */
	@Override
	public void customize(ModelMapper mapper) {

		mapper.addConverter(context -> {

			var symptom = context.getSource();
			var i18nedName = messages.getMessage("symptom." + symptom.getId());

			return new SymptomDto(symptom.getId(), i18nedName, symptom.isCharacteristic());
		}, Symptom.class, SymptomDto.class);
	}
}
