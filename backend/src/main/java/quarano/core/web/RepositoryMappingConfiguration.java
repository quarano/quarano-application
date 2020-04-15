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

import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.Converter;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.ErrorMessage;
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
class RepositoryMappingConfiguration {

	@SuppressWarnings("unchecked")
	RepositoryMappingConfiguration(ModelMapper mapper, ApplicationContext context, ConversionService conversions) {

		Repositories repositories = new Repositories(context);
		RepositoryInvokerFactory invokerFactory = new DefaultRepositoryInvokerFactory(repositories);

		@SuppressWarnings("rawtypes")
		Converter converter = new Converter<Object, Object>() {

			@Override
			public Object convert(MappingContext<Object, Object> context) {

				var sourceType = context.getSourceType();
				var information = repositories.getPersistentEntity(context.getDestinationType());

				Object domainId = Set.of(UUID.class, String.class).contains(sourceType) //
						? conversions.convert(context.getSource(), information.getIdProperty().getType()) //
						: context.getSource();

				var invoker = invokerFactory.getInvokerFor(context.getDestinationType());

				return invoker.invokeFindById(domainId) //
						.orElseThrow(() -> {
							Class<?> type = context.getParent().getSourceType();

							boolean isCollection = Collection.class.isAssignableFrom(type);
							var mappingContext = isCollection ? context.getParent() : context;
							var mapping = mappingContext.getMapping();

							return new AggregateReferenceMappingException(mapping.getPath(), context.getSource(),
									context.getDestinationType());
						});

			}
		};

		@SuppressWarnings("rawtypes")
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

	@Getter
	static class AggregateReferenceMappingException extends MappingException {

		private static final long serialVersionUID = 2554385939537893357L;

		private final String path;
		private final Object source;
		private final Class<?> targetType;
		private final String message;

		public AggregateReferenceMappingException(String path, Object source, Class<?> targetType) {

			super(List.of(new ErrorMessage(message(targetType, source))));

			this.path = cleanUp(path);
			this.source = source;
			this.targetType = targetType;
			this.message = message(targetType, source);
		}

		private static String message(Class<?> targetType, Object source) {
			return String.format("Invalid %s reference %s!", targetType.getName(), source);
		}

		private static String cleanUp(String path) {
			return !path.contains(".") ? path : path.substring(0, path.indexOf('.'));
		}
	}
}
