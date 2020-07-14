package quarano.core.web;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jddd.core.types.Identifier;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

/**
 * A Spring {@link Converter} to convert {@link String} and {@link UUID} values to Quarano {@link Identifier}
 * implementations, assuming they expose a static factory method {@code of(UUID)}.
 *
 * @author Oliver Drotbohm
 */
@Component
class PrimitivesToQuaranoIdentifierConverter implements GenericConverter {

	private static final Map<Class<?>, Method> CACHE = new ConcurrentReferenceHashMap<>();

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#getConvertibleTypes()
	 */
	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {

		return Set.of(//
				new ConvertiblePair(String.class, Identifier.class),
				new ConvertiblePair(UUID.class, Identifier.class)
		);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#convert(java.lang.Object, org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
	 */
	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

		if (source == null) {
			return null;
		}

		UUID uuid = UUID.class.equals(sourceType.getType())
				? (UUID) source
				: UUID.fromString(source.toString());

		Method factoryMethod = CACHE.computeIfAbsent(targetType.getType(), it -> {

			Method method = ReflectionUtils.findMethod(it, "of", UUID.class);
			ReflectionUtils.makeAccessible(method);

			return method;
		});

		return ReflectionUtils.invokeMethod(factoryMethod, null, uuid);
	}
}
