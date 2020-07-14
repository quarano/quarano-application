package quarano;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.validation.Validator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.PropertyDescriptor;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
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
		return getRequiredProperties(type, null);
	}

	public Stream<String> getRequiredProperties(Class<?> type, @Nullable Class<?> group) {

		return validator.getConstraintsForClass(type).getConstrainedProperties().stream()
				.filter(it -> it.getConstraintDescriptors().stream().anyMatch(descriptor -> {

					var annotation = descriptor.getAnnotation();

					if (!REQUIRED_ANNOTATION_TYPES.contains(annotation.annotationType())) {
						return false;
					}

					var groups = List.of((Class[]) AnnotationUtils.getValue(annotation, "groups"));

					return group == null ? groups.isEmpty() : groups.contains(group);

				}))
				.map(PropertyDescriptor::getPropertyName);
	}
}
