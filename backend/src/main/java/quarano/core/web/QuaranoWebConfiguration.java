package quarano.core.web;

import lombok.RequiredArgsConstructor;
import quarano.Quarano;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jmolecules.ddd.types.Identifier;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
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
class QuaranoWebConfiguration implements WebMvcConfigurer {

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.addPathPrefix(
				"api",
				HandlerTypePredicate.forAnnotation(RestController.class)
						.and(HandlerTypePredicate.forBasePackageClass(Quarano.class))
		);
	}

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

	/**
	 * An {@link HttpMessageConverter} that immediately renders {@link MessageSourceResolvable} instances returned from a
	 * Spring MVC controller as plain text if {@link MediaType#TEXT_PLAIN} is requested.
	 *
	 * @author Oliver Drotbohm
	 * @since 1.4
	 */
	@Component
	static class MessageSourceResolvableHttpMessageConverter
			extends AbstractHttpMessageConverter<MessageSourceResolvable> {

		private final MessageSourceAccessor messages;

		/**
		 * Creates a new {@link MessageSourceResolvableHttpMessageConverter} for the given {@link MessageSourceAccessor}.
		 *
		 * @param messages must not be {@literal null}.
		 */
		MessageSourceResolvableHttpMessageConverter(MessageSourceAccessor messages) {

			super(MediaType.TEXT_PLAIN);

			Assert.notNull(messages, "Messages must not be null!");

			this.messages = messages;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.http.converter.AbstractHttpMessageConverter#supports(java.lang.Class)
		 */
		@Override
		protected boolean supports(Class<?> clazz) {
			return MessageSourceResolvable.class.isAssignableFrom(clazz);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.http.converter.AbstractHttpMessageConverter#writeInternal(java.lang.Object, org.springframework.http.HttpOutputMessage)
		 */
		@Override
		protected void writeInternal(MessageSourceResolvable t, HttpOutputMessage outputMessage)
				throws IOException, HttpMessageNotWritableException {
			outputMessage.getBody().write(messages.getMessage(t).getBytes());
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.http.converter.AbstractHttpMessageConverter#canRead(org.springframework.http.MediaType)
		 */
		@Override
		protected boolean canRead(MediaType mediaType) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.http.converter.AbstractHttpMessageConverter#readInternal(java.lang.Class, org.springframework.http.HttpInputMessage)
		 */
		@Override
		protected MessageSourceResolvable readInternal(Class<? extends MessageSourceResolvable> clazz,
				HttpInputMessage inputMessage)
				throws IOException, HttpMessageNotReadableException {
			throw new UnsupportedOperationException();
		}
	}
}
