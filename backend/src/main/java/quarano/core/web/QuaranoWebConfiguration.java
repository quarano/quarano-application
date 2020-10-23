package quarano.core.web;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jmolecules.ddd.types.Identifier;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @author Oliver Drotbohm
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
class QuaranoWebConfiguration implements WebMvcConfigurer {

	@Bean
	Module quaranoModule(MessageSourceAccessor accessor) {

		var module = new SimpleModule();
		module.setMixInAnnotation(Identifier.class, IdentifierMixin.class);
		module.addSerializer(new MessageSourceResolvableSerializer(accessor));
		module.addSerializer(new ErrorsSerializer());
		module.addDeserializer(String.class, new EmptyStringDeserializer());

		return module;
	}

	@JsonSerialize(using = ToStringSerializer.class)
	static abstract class IdentifierMixin {}

	/**
	 * A Jackson serializer triggering message resolution via a {@link MessageSourceAccessor} for
	 * {@link MessageSourceResolvable} instances about to be serialized.
	 *
	 * @author Oliver Drotbohm
	 */
	static class MessageSourceResolvableSerializer extends StdSerializer<MessageSourceResolvable> {

		private static final long serialVersionUID = 4302540100251549622L;

		private final MessageSourceAccessor accessor;

		/**
		 * Creates a new {@link MessageSourceResolvableSerializer} for the given {@link MessageSourceAccessor}.
		 *
		 * @param accessor must not be {@literal null}.
		 */
		public MessageSourceResolvableSerializer(MessageSourceAccessor accessor) {

			super(MessageSourceResolvable.class);

			this.accessor = accessor;
		}

		/*
		 * (non-Javadoc)
		 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
		 */
		@Override
		@SuppressWarnings("null")
		public void serialize(MessageSourceResolvable value, JsonGenerator gen, SerializerProvider provider)
				throws IOException {
			gen.writeString(accessor.getMessage(value));
		}
	}

	/**
	 * A Jackson serializer that translates an {@link Errors} instance into a {@link Map} keyed by field and the
	 * {@link FieldError}s as values.
	 *
	 * @author Oliver Drotbohm
	 */
	static class ErrorsSerializer extends StdSerializer<Errors> {

		private static final long serialVersionUID = 9136141790232694091L;

		ErrorsSerializer() {
			super(Errors.class);
		}

		/*
		 * (non-Javadoc)
		 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
		 */
		@Override
		@SuppressWarnings("null")
		public void serialize(Errors value, JsonGenerator gen, SerializerProvider provider) throws IOException {

			provider.findValueSerializer(ErrorsJson.class)
					.serialize(new ErrorsJson(value), gen, provider);
		}

		@RequiredArgsConstructor
		static class ErrorsJson {

			private final Errors errors;

			@JsonAnyGetter
			Map<String, Object> toMap() {

				return errors.getFieldErrors().stream()
						.collect(Collectors.toMap(FieldError::getField, Function.identity(), (l, r) -> r));
			}
		}
	}
}
