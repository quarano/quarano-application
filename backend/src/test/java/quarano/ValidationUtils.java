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
package quarano;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import javax.validation.Validator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.PropertyDescriptor;

import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
public class ValidationUtils {

	private static final Collection<Class<?>> REQUIRED_ANNOTATION_TYPES = Set.of(NotNull.class, NotEmpty.class);
	private final Validator validator;

	public Stream<String> getRequiredProperties(Class<?> type) {

		return validator.getConstraintsForClass(type).getConstrainedProperties().stream() //
				.filter(it -> it.getConstraintDescriptors().stream()
						.anyMatch(Descriptor -> REQUIRED_ANNOTATION_TYPES.contains(Descriptor.getAnnotation().annotationType()))) //
				.map(PropertyDescriptor::getPropertyName);
	}
}
