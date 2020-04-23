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
package quarano.tracking;

import quarano.tracking.Address.HouseNumber;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Oliver Drotbohm
 */
@Converter(autoApply = true)
class HouseNumberAttributeConverter implements AttributeConverter<HouseNumber, String> {

	/*
	 * (non-Javadoc)
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public String convertToDatabaseColumn(HouseNumber number) {
		return number == null || HouseNumber.NONE.equals(number) ? null : number.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.persistence.AttributeConverter#convertToEntityAttribute(java.lang.Object)
	 */
	@Override
	public HouseNumber convertToEntityAttribute(String source) {
		return source == null ? HouseNumber.NONE : HouseNumber.of(source);
	}
}
