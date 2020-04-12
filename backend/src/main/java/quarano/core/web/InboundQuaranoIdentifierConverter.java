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

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jddd.core.types.Identifier;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

/**
 * @author Oliver Drotbohm
 */
@Component
class InboundQuaranoIdentifierConverter implements GenericConverter {

	private static final Map<Class<?>, Method> CACHE = new ConcurrentReferenceHashMap<>();

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#getConvertibleTypes()
	 */
	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {

		return Set.of(//
				new ConvertiblePair(String.class, Identifier.class), //
				new ConvertiblePair(UUID.class, Identifier.class) //
		);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#convert(java.lang.Object, org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
	 */
	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

		UUID uuid = UUID.class.equals(sourceType.getType()) //
				? (UUID) source //
				: UUID.fromString(source.toString());

		Method factoryMethod = CACHE.computeIfAbsent(targetType.getType(), it -> {

			Method method = ReflectionUtils.findMethod(it, "of", UUID.class);
			ReflectionUtils.makeAccessible(method);

			return method;
		});

		return ReflectionUtils.invokeMethod(factoryMethod, null, uuid);
	}
}
