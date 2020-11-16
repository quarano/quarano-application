package quarano.core.web;

import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import quarano.Quarano;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jmolecules.ddd.types.Identifier;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.TransformedResource;

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

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.addPathPrefix(
			"api",
			HandlerTypePredicate.forAnnotation(RestController.class)
			.and(HandlerTypePredicate.forBasePackageClass(Quarano.class))
		);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/static/")
				.resourceChain(true)
				.addResolver(new PathResourceResolver() {
					/* We have to help of our NG Frontend Router of our SPA!
					 * The concrete path resolution is done in our Frontend APP.
					 *
					 * In webserver speech: we're rewriting the paths of the files served
					 * if the file of the requested path exists, we're serving it
					 * if not, we serve the NG index.html.
					 */
					@Override
					protected Resource getResource(String resourcePath, Resource location) throws IOException {
						Resource requestedResource = location.createRelative(resourcePath);

						return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
								: new ClassPathResource("/static/index.html");
					}
				})
				.addTransformer((httpServletRequest, resource, resourceTransformerChain) -> {
					if (resource instanceof ClassPathResource) {
						var path = ((ClassPathResource) resource).getPath();
						/* We have to manipulate the baseHref of the NG app to match the current servers contextPath */
						if (path.equals("static/index.html")) {
							String html = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
							String newBaseHref = String.format("base href=\"%s/\"", httpServletRequest.getContextPath());
							html = html.replace("base href=\"/\"", newBaseHref);
							return new TransformedResource(resource, html.getBytes());
						}
					}
					return resource;
				});
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
}
