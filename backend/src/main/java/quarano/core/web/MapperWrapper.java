package quarano.core.web;

import io.vavr.control.Either;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.core.web.RepositoryMappingModule.AggregateReferenceMappingException;

import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

/**
 * Wrapper for {@link ModelMapper} to add a few convenience methods.
 *
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
public class MapperWrapper {

	private final @NonNull ModelMapper mapper;

	/**
	 * Maps the given source object to the given target and returns the target object.
	 *
	 * @param <T>
	 * @param source must not be {@literal null}.
	 * @param target must not be {@literal null}.
	 * @return
	 */
	public <T> T map(Object source, T target) {

		Assert.notNull(source, "Source must not be null!");
		Assert.notNull(target, "Target must not be null!");

		mapper.map(source, target);
		return target;
	}

	/**
	 * Maps the given source object to the given target capturing potentially occurring {@link MappingException}s in the
	 * given {@link Errors} instance.
	 *
	 * @param <T>
	 * @param source must not be {@literal null}.
	 * @param target must not be {@literal null}.
	 * @param errors must not be {@literal null}.
	 * @return an {@link Either} containing either the successfully mapped target object or an {@link Errors} with the
	 *         exceptions mapped to rejected properties.
	 */
	public <T> Either<T, Errors> map(Object source, T target, Errors errors) {

		Assert.notNull(source, "Source must not be null!");
		Assert.notNull(target, "Target must not be null!");
		Assert.notNull(errors, "Errors must not be null!");

		try {

			mapper.map(source, target);
			return Either.left(target);

		} catch (MappingException o_O) {
			return handle(o_O, errors);
		}
	}

	/**
	 * Maps the given source object to the given target type.
	 *
	 * @param <T>
	 * @param source must not be {@literal null}.
	 * @param target must not be {@literal null}.
	 * @return
	 */
	public <T> T map(Object source, Class<T> target) {
		return mapper.map(source, target);
	}

	/**
	 * Maps the given source object to the given target capturing potentially occurring {@link MappingException}s in the
	 * given {@link Errors} instance.
	 *
	 * @param <T>
	 * @param source must not be {@literal null}.
	 * @param target must not be {@literal null}.
	 * @param errors must not be {@literal null}.
	 * @return an {@link Either} containing either the successfully mapped target object or an {@link Errors} with the
	 *         exceptions mapped to rejected properties.
	 */
	public <T> Either<T, Errors> map(Object source, Class<T> target, Errors errors) {

		try {
			return Either.left(mapper.map(source, target));
		} catch (MappingException o_O) {
			return handle(o_O, errors);
		}
	}

	private <T> Either<T, Errors> handle(MappingException o_O, Errors errors) {

		o_O.getErrorMessages().stream()//
				.map(ErrorMessage::getCause)
				.forEach(it -> {

					if (AggregateReferenceMappingException.class.isInstance(it)) {
						handle((AggregateReferenceMappingException) it, errors);
					} else {
						handle((RuntimeException) it, errors);
					}
				});

		return Either.right(errors);
	}

	private void handle(RuntimeException o_O, Errors errors) {
		errors.reject("invalid", o_O.getMessage());
	}

	private void handle(AggregateReferenceMappingException o_O, Errors errors) {

		var path = o_O.getPath();
		var message = o_O.getErrorMessages().iterator().next();

		errors.rejectValue(path, String.format("Invalid.%s", path), message.getMessage());
	}
}
