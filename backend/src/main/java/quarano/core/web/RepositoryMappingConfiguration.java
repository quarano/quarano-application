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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.repository.support.DefaultRepositoryInvokerFactory;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.repository.support.RepositoryInvokerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
public class RepositoryMappingConfiguration {

	public RepositoryMappingConfiguration(ModelMapper mapper, ApplicationContext context, ConversionService conversions) {

		Repositories repositories = new Repositories(context);
		RepositoryInvokerFactory invokerFactory = new DefaultRepositoryInvokerFactory(repositories);

		Converter converter = new Converter<Object, Object>() {

			@Override
			public Object convert(MappingContext<Object, Object> context) {

				var sourceType = context.getSourceType();
				var information = repositories.getPersistentEntity(context.getDestinationType());

				Object domainId = Set.of(UUID.class, String.class).contains(sourceType) //
						? conversions.convert(context.getSource(), information.getIdProperty().getType()) //
						: context.getSource();

				var invoker = invokerFactory.getInvokerFor(context.getDestinationType());

				return invoker.invokeFindById(domainId).orElse(null);
			}
		};

		Converter toStringOrUuidConverter = new Converter<Object, Object>() {

			@Override
			public Object convert(MappingContext<Object, Object> context) {

				var source = context.getSource();
				var targetType = context.getDestinationType();

				var id = repositories.getPersistentEntity(source.getClass()) //
						.getIdentifierAccessor(source) //
						.getRequiredIdentifier();

				return targetType.isInstance(id) //
						? id //
						: conversions.convert(id, targetType);
			}
		};

		repositories.forEach(type -> {

			var information = repositories.getPersistentEntity(type);
			var domainIdType = information.getIdProperty().getType();
			var idTypes = new HashSet<Class<?>>();

			idTypes.addAll(Set.of(UUID.class, String.class));
			idTypes.add(domainIdType);
			idTypes.forEach(it -> {
				mapper.addConverter(converter, it, information.getType());
				mapper.addConverter(toStringOrUuidConverter, information.getType(), it);
			});
		});
	}
}
