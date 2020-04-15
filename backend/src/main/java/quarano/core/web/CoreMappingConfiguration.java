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

import java.util.List;
import java.util.UUID;

import org.jddd.core.types.Identifier;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.stereotype.Component;

/**
 * Registers a {@link ModelMapper} {@link Converter} to convert from {@link Identifier} instances to {@link UUID} or
 * {@link String} values by delegating to {@link QuaranoIdentifierToPrimitivesConverter}.
 *
 * @author Oliver Drotbohm
 */
@Component
class CoreMappingConfiguration {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	CoreMappingConfiguration(ModelMapper mapper, JpaMetamodelMappingContext context) {

		new PersistentEntities(List.of(context)) //
				.filter(it -> it.hasIdProperty()) //
				.map(it -> it.getIdProperty()) //
				.map(PersistentProperty::getType) //
				.filter(it -> Identifier.class.isAssignableFrom(it)) //
				.forEach(it -> {
					QuaranoIdentifierToPrimitivesConverter.getIdPrimitives().forEach(target -> {
						mapper.addConverter((Converter) IdentifierToUuidOrStringConverter.INSTANCE, it, target);
					});
				});
	}

	private enum IdentifierToUuidOrStringConverter implements Converter<Identifier, Object> {

		INSTANCE;

		private final QuaranoIdentifierToPrimitivesConverter delegate = new QuaranoIdentifierToPrimitivesConverter();

		/*
		 * (non-Javadoc)
		 * @see org.modelmapper.Converter#convert(org.modelmapper.spi.MappingContext)
		 */
		@Override
		public Object convert(MappingContext<Identifier, Object> context) {

			var source = TypeDescriptor.valueOf(context.getSourceType());
			var target = TypeDescriptor.valueOf(context.getDestinationType());

			return delegate.convert(context.getSource(), source, target);
		}
	};

}
