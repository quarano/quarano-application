package quarano.core.web;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jmolecules.ddd.types.Identifier;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
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
	@NonNull
	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {

		return Set.of(//
				new ConvertiblePair(String.class, Identifier.class),
				new ConvertiblePair(UUID.class, Identifier.class));
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.GenericConverter#convert(java.lang.Object, org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
	 */
	@Nullable
	@Override
	public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

		if (source == null) {
			return null;
		}

		UUID uuid = UUID.class.equals(sourceType.getType())
				? (UUID) source
				: UUID.fromString(source.toString());

		Method factoryMethod = CACHE.computeIfAbsent(targetType.getType(), it -> {

			Method method = ReflectionUtils.findMethod(it, "of", UUID.class);

			if (method == null) {
				throw new IllegalStateException(String.format("No static of(UUID) method found on type %s!", it.getName()));
			}

			ReflectionUtils.makeAccessible(method);

			return method;
		});

		return ReflectionUtils.invokeMethod(factoryMethod, null, uuid);
	}
}
