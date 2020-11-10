package quarano.core.web;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.Converter;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.ErrorMessage;
import org.modelmapper.spi.MappingContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.repository.support.DefaultRepositoryInvokerFactory;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.repository.support.RepositoryInvokerFactory;
import org.springframework.lang.Nullable;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.util.Assert;

/**
 * ModelMapper module to allow the mapping of identifiers of variable kinds into aggregate references loadable via
 * Spring Data repositories. Failed aggregate resolution will be reported via an
 * {@link AggregateReferenceMappingException} unless the type has a dedicated {@link NullHandling} registered.
 *
 * @author Oliver Drotbohm
 */
public class RepositoryMappingModule implements org.modelmapper.Module {

	private final RepositoryInvokerFactory invokerFactory;
	private final Repositories repositories;
	private final ConversionService conversions;

	private final List<IdentifierProcessor> processors;
	private final List<Class<?>> exclusions;
	private final Map<Class<?>, NullHandling> nullHandling;

	/**
	 * Creates a new {@link RepositoryMappingModule} from the given {@link Repositories} and {@link ConversionService}.
	 *
	 * @param repositories must not be {@literal null}.
	 * @param conversions must not be {@literal null}.
	 */
	public RepositoryMappingModule(Repositories repositories, ConversionService conversions) {

		Assert.notNull(repositories, "Repositories must not be null!");
		Assert.notNull(conversions, "ConversionService must not be null!");

		this.invokerFactory = new DefaultRepositoryInvokerFactory(repositories);
		this.repositories = repositories;
		this.conversions = conversions;
		this.processors = new ArrayList<>();
		this.exclusions = new ArrayList<>();
		this.nullHandling = new HashMap<>();
	}

	/**
	 * Registers an {@link IdentifierProcessor} to be used to pre- and post-process identifiers during conversions.
	 *
	 * @param processor must not be {@literal null}.
	 * @return
	 */
	public RepositoryMappingModule register(IdentifierProcessor processor) {

		Assert.notNull(processor, "IdentifierProcessor must not be null!");

		this.processors.add(processor);

		return this;
	}

	/**
	 * Excludes the given domain type from the aggregate resolution mapping.
	 *
	 * @param type must not be {@literal null}.
	 * @return
	 */
	public RepositoryMappingModule exclude(Class<?> type) {

		Assert.notNull(type, "Type must not be null!");

		this.exclusions.add(type);

		return this;
	}

	public RepositoryMappingModule nullHandling(Class<?> type, NullHandling nullHandling) {

		Assert.notNull(type, "Type must not be null!");
		Assert.notNull(nullHandling, "NullHandling must not be null!");

		this.nullHandling.put(type, nullHandling);

		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.modelmapper.Module#setupModule(org.modelmapper.ModelMapper)
	 */
	@Override
	public void setupModule(ModelMapper mapper) {

		var registry = PluginRegistry.of(processors);

		@SuppressWarnings("rawtypes")
		Converter converter = new Converter<Object, Object>() {

			@Override
			@Nullable
			public Object convert(MappingContext<Object, Object> context) {

				var destinationType = context.getDestinationType();
				var information = repositories.getPersistentEntity(destinationType);
				var source = context.getSource();

				if (source == null) {
					return handleNull(null, context);
				}

				Object identifier = registry.getPluginFor(destinationType)
						.map(it -> it.preProcessIdentifier(context.getSource(), destinationType))
						.orElseGet(context::getSource);

				Object domainId = Set.of(UUID.class, String.class).contains(identifier.getClass())
						? conversions.convert(identifier, information.getRequiredIdProperty().getType())
						: context.getSource();

				var invoker = invokerFactory.getInvokerFor(destinationType);

				var result = Optional.ofNullable(domainId)
						.flatMap(it -> invoker.invokeFindById(it))
						.orElse(null);

				return result == null ? handleNull(domainId, context) : result;
			}

			@Nullable
			private Object handleNull(@Nullable Object id, MappingContext<?, ?> context) {

				var destinationType = context.getDestinationType();
				var handling = nullHandling.get(destinationType);

				if (handling == null || handling.equals(NullHandling.THROW_EXCEPTION)) {

					Class<?> type = context.getParent().getSourceType();

					boolean isCollection = Collection.class.isAssignableFrom(type);
					var mappingContext = isCollection ? context.getParent() : context;
					var mapping = mappingContext.getMapping();

					throw new AggregateReferenceMappingException(mapping.getPath(), id, destinationType);
				}

				return null;
			}
		};

		@SuppressWarnings("rawtypes")
		Converter toStringOrUuidConverter = new Converter<Object, Object>() {

			@Override
			@Nullable
			public Object convert(MappingContext<Object, Object> context) {

				var source = context.getSource();
				var sourceType = context.getSourceType();
				var targetType = context.getDestinationType();

				var id = repositories.getPersistentEntity(source.getClass())
						.getIdentifierAccessor(source)
						.getRequiredIdentifier();

				var processed = registry.getPluginFor(sourceType)
						.map(it -> it.postProcessIdentifier(id, sourceType))
						.orElse(id);

				return targetType.isInstance(processed)
						? processed
						: conversions.convert(processed, targetType);
			}
		};

		repositories.forEach(type -> {

			if (exclusions.stream().anyMatch(type::isAssignableFrom)) {
				return;
			}

			var information = repositories.getPersistentEntity(type);
			var domainIdType = information.getRequiredIdProperty().getType();
			var idTypes = new HashSet<Class<?>>();

			idTypes.addAll(Set.of(UUID.class, String.class));
			idTypes.add(domainIdType);

			registry.getPluginFor(type)
					.map(IdentifierProcessor::getAdditionalIdentifierTypes)
					.ifPresent(idTypes::addAll);

			idTypes.forEach(it -> {

				mapper.addConverter(converter, it, information.getType());
				mapper.addConverter(toStringOrUuidConverter, information.getType(), it);
			});
		});
	}

	@Getter
	public static class AggregateReferenceMappingException extends MappingException {

		private static final long serialVersionUID = 2554385939537893357L;

		private final String path;
		private final Object source;
		private final Class<?> targetType;
		private final String message;

		public AggregateReferenceMappingException(String path, @Nullable Object source, Class<?> targetType) {

			super(List.of(new ErrorMessage(message(targetType, source))));

			this.path = cleanUp(path);
			this.source = source;
			this.targetType = targetType;
			this.message = message(targetType, source);
		}

		private static String message(Class<?> targetType, @Nullable Object source) {
			return String.format("Invalid %s reference %s!", targetType.getName(), source);
		}

		private static String cleanUp(String path) {
			return !path.contains(".") ? path : path.substring(0, path.indexOf('.'));
		}
	}

	public enum NullHandling {
		RETURN_NULL, THROW_EXCEPTION;
	}
}
