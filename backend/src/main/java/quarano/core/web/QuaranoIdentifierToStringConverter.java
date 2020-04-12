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
package quarano.core.web;

import java.lang.reflect.Field;
import java.util.Map;

import org.jddd.core.types.Identifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

/**
 * @author Oliver Drotbohm
 */
@Component
public class QuaranoIdentifierToStringConverter implements Converter<Identifier, String> {

	private static final Map<Class<?>, Field> CACHE = new ConcurrentReferenceHashMap<>();

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public String convert(Identifier source) {

		Field idField = CACHE.computeIfAbsent(source.getClass(), it -> {
			var field = ReflectionUtils.findField(it, "id");
			ReflectionUtils.makeAccessible(field);
			return field;
		});

		return ReflectionUtils.getField(idField, source).toString();
	}
}
